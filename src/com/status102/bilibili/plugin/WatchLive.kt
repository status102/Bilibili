package com.status102.bilibili.plugin

import com.status102.bilibili.PCController
import com.status102.bilibili.account.User
import com.status102.bilibili.api.live.area.LiveAreaApi
import com.status102.bilibili.api.live.lottery.LiveLotteryApi
import com.status102.bilibili.api.live.relation.LiveRelationApi
import com.status102.bilibili.api.live.room.LiveRoomApi
import com.status102.bilibili.monitor.Monitor
import com.status102.bilibili.retrofit.exception.ApiFailureException
import com.status102.bilibili.retrofit.exception.ResponseConvertException
import com.status102.bilibili.tool.Tool
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class WatchLive(var user: User) {
	private var roomId by Delegates.notNull<Long>()
	private var work: ScheduledFuture<*>? = null
	private var heartBeat: ScheduledFuture<*>? = null

	val isWorking: Boolean
		get() = work == null

	init {
		enterWatchRoom()
	}

	fun enterWatchRoom() = GlobalScope.launch(PCController.workerDispatcher) {
		var str = "模拟观看-获取关注表中正在直播的房间"
		try {
			LiveRelationApi.getSubscribeLiveRoomList(user).data.run {
				if (totalCount == 0 || rooms.isNullOrEmpty()) {
					str = "模拟观看-获取分区中正在直播的房间"
					LiveAreaApi.getRoomListOfArea(Monitor.areaIDMap.run {
						if (isNotEmpty()) keys.random()
						else (1..5).random()
					}, 1).data.random().roomId.run {
						enterWatchRoom(this)
					}
				} else {
					enterWatchRoom(rooms.random().roomId)
				}
			}
		} catch (e: ApiFailureException) {
			e.jsonObject.run {
				user.addLog("${str}失败${toString()}")
				user.println("${str}失败${toString()}")
			}
		} catch (e: ResponseConvertException) {
			user.addLog("${str}失败${e.response.message}")
			user.println("${str}失败${e.response.message}")
		} catch (e: HttpException) {
			user.addLog("${str}失败${Tool.getStackTraceInfo(e)}")
			user.println("${str}失败${Tool.getStackTraceInfo(e)}")
		} catch (e: IOException) {
			user.addLog("${str}失败${Tool.getStackTraceInfo(e)}")
			user.println("${str}失败${Tool.getStackTraceInfo(e)}")
		}
	}

	fun enterWatchRoom(roomId: Long) {
		if (work != null) {
			work?.cancel(true)
		}
		this.roomId = roomId
		work = PCController.bossPool.schedule({ checkIndexInfo() }, (5..300).random() * 1000L, TimeUnit.MILLISECONDS)
	}

	fun checkIndexInfo() = GlobalScope.launch {
		val str = "模拟观看-获取直播间进房数据"
		try {
			LiveRoomApi.getInfoByRoom(user, roomId).data.run {
				user.addLog("${str}成功")
				work = PCController.bossPool.schedule({ enterRoom() }, (5..60).random() * 1000L, TimeUnit.MILLISECONDS)
			}
		} catch (e: ApiFailureException) {
			e.jsonObject.run {
				user.addLog("${str}失败${toString()}")
				user.println("${str}失败${toString()}")
			}
		} catch (e: ResponseConvertException) {
			user.addLog("${str}失败${e.response.message}")
			user.println("${str}失败${e.response.message}")
		} catch (e: HttpException) {
			user.addLog("${str}失败${Tool.getStackTraceInfo(e)}")
			user.println("${str}失败${Tool.getStackTraceInfo(e)}")
		} catch (e: IOException) {
			user.addLog("${str}失败${Tool.getStackTraceInfo(e)}")
			user.println("${str}失败${Tool.getStackTraceInfo(e)}")
		}
	}

	private fun enterRoom() = GlobalScope.launch {
		val str = "模拟观看-进房"
		supervisorScope {
			try {
				LiveRoomApi.APP.enterRoom(user, roomId).run {
					user.addLog("${str}成功")
					heartBeat?.cancel(true)
					heartBeat = PCController.bossPool.scheduleAtFixedRate(
						{ GlobalScope.launch { LiveRoomApi.APP.heartBeat(user, roomId) } },
						300L, 300L,
						TimeUnit.SECONDS
					)
					checkSilverBoxInfo()
				}
			} catch (e: ApiFailureException) {
				e.jsonObject.run {
					user.addLog("${str}失败${toString()}")
					user.println("${str}失败${toString()}")
				}
				work = null
				return@supervisorScope
			} catch (e: ResponseConvertException) {
				user.addLog("${str}失败${e.response.message}")
				user.println("${str}失败${e.response.message}")
				work = null
				return@supervisorScope
			} catch (e: HttpException) {
				user.addLog("${str}失败${Tool.getStackTraceInfo(e)}")
				user.println("${str}失败${Tool.getStackTraceInfo(e)}")
				work = null
				return@supervisorScope
			} catch (e: IOException) {
				user.addLog("${str}失败${Tool.getStackTraceInfo(e)}")
				user.println("${str}失败${Tool.getStackTraceInfo(e)}")
				work = null
				return@supervisorScope
			}
		}
	}

	fun checkSilverBoxInfo() = GlobalScope.launch {
		val str = "模拟观看-进房获取银瓜子宝箱信息"
		try {
			LiveLotteryApi.getLotteryInfo(user, roomId).run {
				data.silverBox?.run {
					user.addLog(
						"${str}成功：%s".format(
							if (status == 1 && timeStart == 0L && timeEnd == 0L) "今日银瓜子宝箱已领完"
							else (if (status == 0) "第${times}/${maxTimes}轮，${minute}分钟后领取${silver}银瓜子" else "银瓜子宝箱状态异常${this}")
						)
					)
					work =
						PCController.bossPool.schedule({ receiveSilverBox(timeStart, timeEnd) }, minute * 60L + (0..120).random(), TimeUnit.SECONDS)
				} ?: {
					user.addLog("${str}失败：silverBox为null，${this}")
					work = null
				}.invoke()
			}
		} catch (e: ApiFailureException) {
			e.jsonObject.run {
				user.addLog("${str}失败${toString()}")
				user.println("${str}失败${toString()}")
			}
			work = null
			return@launch
		} catch (e: ResponseConvertException) {
			user.addLog("${str}失败${e.response.message}")
			user.println("${str}失败${e.response.message}")
			work = null
			return@launch
		} catch (e: HttpException) {
			user.addLog("${str}失败${Tool.getStackTraceInfo(e)}")
			user.println("${str}失败${Tool.getStackTraceInfo(e)}")
			work = null
			return@launch
		} catch (e: IOException) {
			user.addLog("${str}失败${Tool.getStackTraceInfo(e)}")
			user.println("${str}失败${Tool.getStackTraceInfo(e)}")
			work = null
			return@launch
		}
	}

	fun getSilverBoxTask(): Job = GlobalScope.launch {
		val str = "模拟观看-获取银瓜子宝箱任务信息"
		try {
			LiveLotteryApi.getSilverBoxTask(user).data.run {
				user.addLog(
					"${str}成功：%s".format(
						if (times == 0 && timeStart == 0L && timeEnd == 0L) "今日银瓜子宝箱已领完"
						else (if (times != 0) "第${times}/${maxTimes}轮，${minute}分钟后领取${silver}银瓜子" else "银瓜子宝箱状态异常${this}")
					)
				)
				work =
					PCController.bossPool.schedule({ receiveSilverBox(timeStart, timeEnd) }, minute * 60L + (0..120).random(), TimeUnit.SECONDS)
			}
		} catch (e: ApiFailureException) {
			e.jsonObject.run {
				user.addLog("${str}失败${toString()}")
				user.println("${str}失败${toString()}")
			}
			work = null
			return@launch
		} catch (e: ResponseConvertException) {
			user.addLog("${str}失败${e.response.message}")
			user.println("${str}失败${e.response.message}")
			work = null
			return@launch
		} catch (e: HttpException) {
			user.addLog("${str}失败${Tool.getStackTraceInfo(e)}")
			user.println("${str}失败${Tool.getStackTraceInfo(e)}")
			work = null
			return@launch
		} catch (e: IOException) {
			user.addLog("${str}失败${Tool.getStackTraceInfo(e)}")
			user.println("${str}失败${Tool.getStackTraceInfo(e)}")
			work = null
			return@launch
		}
	}

	fun receiveSilverBox(timeStart: Long, timeEnd: Long): Job = GlobalScope.launch {
		val str = "模拟观看-领取银瓜子宝箱"
		try {
			LiveLotteryApi.receiveSilverBox(user, timeStart, timeEnd).data.run {
				user.addLog("${str}成功：获得${awardSilver}银瓜子，持有${silver}银瓜子，今日银瓜子宝箱${if (isEnd == 0) "未" else "已"}领完")
				if (isEnd == 0) {
					work = PCController.bossPool.schedule({ getSilverBoxTask() }, 0L, TimeUnit.MILLISECONDS)
				}
			}
		} catch (e: ApiFailureException) {
			e.jsonObject.run {
				user.addLog("${str}失败${toString()}")
				user.println("${str}失败${toString()}")
			}
			work = null
			return@launch
		} catch (e: ResponseConvertException) {
			user.addLog("${str}失败${e.response.message}")
			user.println("${str}失败${e.response.message}")
			work = null
			return@launch
		} catch (e: HttpException) {
			user.addLog("${str}失败${Tool.getStackTraceInfo(e)}")
			user.println("${str}失败${Tool.getStackTraceInfo(e)}")
			work = null
			return@launch
		} catch (e: IOException) {
			user.addLog("${str}失败${Tool.getStackTraceInfo(e)}")
			user.println("${str}失败${Tool.getStackTraceInfo(e)}")
			work = null
			return@launch
		}
	}
}