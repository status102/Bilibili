package com.status102.bilibili.api.live.lottery

import com.status102.bilibili.BilibiliProperty
import com.status102.bilibili.PCController
import com.status102.bilibili.account.User
import com.status102.bilibili.api.live.room.LiveRoomApi
import com.status102.bilibili.retrofit.RetrofitExtension
import com.status102.bilibili.retrofit.exception.ApiFailureException
import com.status102.bilibili.retrofit.exception.ResponseConvertException
import com.status102.bilibili.retrofit.interceptor.AddHeaderInterceptor
import com.status102.bilibili.retrofit.interceptor.AddParamInterceptor
import com.status102.bilibili.tool.Tool
import com.status102.utils.TimeUtils
import io.netty.handler.codec.http.HttpHeaderNames
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException
import java.time.Instant
import java.util.concurrent.TimeUnit

object LiveLotteryApi {
	data class LiveLotteryIdList(
		val raffleIdSet: MutableSet<Long> = mutableSetOf(),
		val stormIdSet: MutableSet<Long> = mutableSetOf(),
		val boxIdSet: MutableSet<Long> = mutableSetOf(),
		val pkIdSet: MutableSet<Long> = mutableSetOf(),
		val guardIdSet: MutableSet<Long> = mutableSetOf()
	)

	var checkMode = true
	private const val SPACE_STRING = "                    "

	suspend fun getLotteryInfo(user: User, roomId: Long) = RetrofitExtension.buildRetrofit(
		LiveLotteryService.BASE_URL,
		LiveLotteryService::class.java,
		user.addHeaderInterceptor,
		user.addParamInterceptor
	).getLotteryInfo(roomId)

	suspend fun getSilverBoxTask(user: User) = RetrofitExtension.buildRetrofit(
		LiveLotteryService.BASE_URL,
		LiveLotteryService.SilverBox::class.java,
		user.addHeaderInterceptor,
		user.addParamInterceptor
	).getCurrentTask()

	suspend fun receiveSilverBox(user: User, timeStart: Long, timeEnd: Long) = RetrofitExtension.buildRetrofit(
		LiveLotteryService.BASE_URL,
		LiveLotteryService.SilverBox::class.java,
		user.addHeaderInterceptor,
		user.addParamInterceptor
	).getAward(timeStart, timeEnd)

	suspend fun checkLotteryInfo(userList: List<User>, roomId: Long) {
		var str :StringBuilder= StringBuilder("公共-获取${roomId}房抽奖信息")
		try {
			RetrofitExtension.buildRetrofit(
				LiveLotteryService.BASE_URL,
				LiveLotteryService::class.java,
				AddHeaderInterceptor(
					HttpHeaderNames.COOKIE.toString() to { "" },
					BilibiliProperty.Header.DISPLAY_ID to { BilibiliProperty.buildVersionId + "-" + Instant.now().epochSecond.toString() },
					BilibiliProperty.Header.BUILD_VERSION_ID to { BilibiliProperty.buildVersionId },
					BilibiliProperty.Header.USER_AGENT to { BilibiliProperty.defaultUserAgent }),
				AddParamInterceptor(
					BilibiliProperty.Param.APP_KEY to { BilibiliProperty.appKey },
					BilibiliProperty.Param.BUILD to { BilibiliProperty.build },
					BilibiliProperty.Param.MOBILE_APP to { BilibiliProperty.platform },
					BilibiliProperty.Param.PLATFORM to { BilibiliProperty.platform },
					BilibiliProperty.Param.TIMESTAMP to { Instant.now().epochSecond.toString() }
				)
			).getLotteryInfo(roomId)
				.data.run {
					str.append("成功：\n")

					guard?.forEach {
						str.append("$SPACE_STRING${it.sender.uname}[UID:${it.sender.uid}]赠送的")
							.append(
								when (it.privilegeType) {
									1 -> "总督抽奖"
									2 -> "提督抽奖"
									3 -> "舰长抽奖"
									else -> "未知舰队抽奖[${it.privilegeType}]"
								}
							).append("[${it.id}] 到期时间：${TimeUtils.unixToStrTime(Instant.now().epochSecond + it.time)}")
					}
					giftList?.forEach {
						str.append(
							"$SPACE_STRING${it.fromUser.uname}赠送的${it.title}[${it.raffleId}]，抽奖时间：%s---%s ".format(
								TimeUtils.unixToStrTime(it.timeWait + Instant.now().epochSecond),
								TimeUtils.unixToStrTime(it.time + Instant.now().epochSecond)
							)
						)
					}
					activityBox?.run {
						str.append("${SPACE_STRING}开启宝箱抽奖[${activityId}]\n")
							.append("${SPACE_STRING}轮数：${currentRound}/${typeB.size}\n")
							.append("${SPACE_STRING}标题：${title}\n")
							.append("${SPACE_STRING}规则：${rule}\n")
					}
					pk?.forEach {
						str.append("${SPACE_STRING}开启PK抽奖[${it.pkId}]，剩余时间${it.time}s\n")
					}
					PCController.printlnMsg(str.toString())

					userList.forEach { user ->
						str = StringBuilder("公共-获取${roomId}房抽奖信息成功：\n")
						guard?.filter {
							str.append("$SPACE_STRING${it.sender.uname}[UID:${it.sender.uid}]赠送的")
								.append(
									when (it.privilegeType) {
										1 -> "总督抽奖"
										2 -> "提督抽奖"
										3 -> "舰长抽奖"
										else -> "未知舰队抽奖[${it.privilegeType}]"
									}
								).append("[${it.id}] 到期时间：${TimeUtils.unixToStrTime(Instant.now().epochSecond + it.time)}")

							if (!user.account.isLoggedIn) {
								user.login((5..60).random() * 1000L)
								str.append("(未登录)\n")
								false
							} else if (!user.canLottery || !user.account.setting.isGuardLottery) {
								str.append("(不参与)\n")
								false
							} else if (user.liveLotteryIdList.guardIdSet.contains(it.id)) {
								str.append("(已录入)\n")
								false
							} else {
								str.append("(成功录入)\n")
								true
							}
						}?.associateWith {
							user.liveLotteryIdList.guardIdSet.add(it.id)
							(0..it.time * 1000).random() % ((3..20).random() * 60 * 1000L)//对10分钟取余，防止等待过久占内存
						}?.toList()?.sortedBy { it.second }?.let {
							GlobalScope.launch {
								var wait: Long
								for (i: Int in it.indices) {
									wait = if (i == 0)
										it[i].second
									else
										it[i].second - it[i - 1].second

									delay(wait)
									user.liveLotteryIdList.guardIdSet.remove(it[i].first.id)
									joinGuardLottery(
										user,
										roomId,
										it[i].first.id,
										it[i].first.keyword,
										it[i].first.privilegeType
									)
								}
							}
						}
						/*guard?.forEach {
							val guardId = it.id
							var waitTime = it.time
							val type = it.keyword
							val guardType = it.privilegeType
							str.append("$SPACE_STRING${it.sender.uname}[UID:${it.sender.uid}]赠送的")
								.append(
									when (guardType) {
										1 -> "总督抽奖"
										2 -> "提督抽奖"
										3 -> "舰长抽奖"
										else -> "未知舰队抽奖[$guardType]"
									}
								)
							str.append("[${guardId}] 到期时间：${TimeUtils.unixToStrTime(Instant.now().epochSecond + waitTime)}")
							if (user.liveLotteryIdList.guardIdSet.contains(guardId)) {
								str.append("(已录入)\n")
							} else if (user.canLottery && user.account.setting.isGuardLottery) {
								user.liveLotteryIdList.guardIdSet.add(guardId)
								waitTime = (0..waitTime * 1000).random() % ((5..15).random() * 60 * 1000)//对10分钟取余，防止等待过久占内存
								str.append(
									"(成功录入，抽奖时间：${TimeUtils.currentToStrTime(System.currentTimeMillis() + waitTime)})\n"
								)
								GlobalScope.launch {
									delay(waitTime.toLong())
									user.liveLotteryIdList.guardIdSet.remove(guardId)
									joinGuardLottery(
										user,
										roomId,
										guardId,
										type,
										guardType
									)
								}
							} else {
								str.append("(不参与)\n")
							}
						}*/
						giftList?.filter {
							str.append(
								"$SPACE_STRING${it.fromUser.uname}赠送的${it.title}[${it.raffleId}]，抽奖时间：%s---%s ".format(
									TimeUtils.unixToStrTime(it.timeWait + Instant.now().epochSecond),
									TimeUtils.unixToStrTime(it.time + Instant.now().epochSecond)
								)
							)
							if (!user.account.isLoggedIn) {
								user.login((5..60).random() * 1000L)
								str.append("(未登录)\n")
								false
							} else if (!user.canLottery || !user.account.setting.isRaffleLottery) {
								str.append("(不参与)\n")
								false
							} else if (user.liveLotteryIdList.raffleIdSet.contains(it.raffleId)) {
								str.append("(已录入)\n")
								false
							} else {
								str.append("(成功录入)\n")
								true
							}
						}?.associateWith {
							{
								user.liveLotteryIdList.raffleIdSet.add(it.raffleId)
								if (it.timeWait > 0) it.timeWait * 1000 else 0
							}.run {
								(this.invoke()..(it.time * 1000)).random().toLong()
							}
						}?.toList()?.sortedBy { it.second }?.let {
							GlobalScope.launch {
								var wait: Long
								for (i: Int in it.indices) {
									wait = if (i == 0)
										it[i].second
									else
										it[i].second - it[i - 1].second

									delay(wait)
									user.liveLotteryIdList.raffleIdSet.remove(it[i].first.raffleId)
									joinRaffleLottery(
										user,
										roomId,
										it[i].first.raffleId,
										it[i].first.type,
										it[i].first.title,
										it[i].first.fromUser.uname
									)
								}
							}
						}
						/*giftList?.forEach {
							val raffleId = it.raffleId
							val type = it.type
							val sender = it.fromUser.uname
							val title = it.title
							var waitTime = it.timeWait
							val endTime = it.time
							str.append(
								"$SPACE_STRING ${sender}赠送的$title[$raffleId]，抽奖时间：%s---%s ".format(
									TimeUtils.unixToStrTime(waitTime + Instant.now().epochSecond),
									TimeUtils.unixToStrTime(endTime + Instant.now().epochSecond)
								)
							)
							if (user.liveLotteryIdList.raffleIdSet.contains(raffleId)) {
								str.append("(已录入)\n")
							} else if (user.account.isLoggedIn && user.account.setting.isRaffleLottery) {
								user.liveLotteryIdList.raffleIdSet.add(raffleId)
								waitTime = if (waitTime > 0) waitTime * 1000 else 0
								waitTime = (waitTime..endTime * 1000).random()
								str.append("(成功录入，等待${waitTime / 1000}s)\n")
								GlobalScope.launch {
									delay(waitTime.toLong())
									user.liveLotteryIdList.raffleIdSet.remove(raffleId)
									joinRaffleLottery(
										user,
										roomId,
										raffleId,
										type,
										title,
										sender
									)
								}
							} else {
								str.append("(不参与)\n")
							}
						}*/
						activityBox?.run {
							str.append("${SPACE_STRING}开启宝箱抽奖[${activityId}]\n")
								.append("${SPACE_STRING}轮数：${currentRound}/${typeB.size}\n")
								.append("${SPACE_STRING}标题：${title}\n")
								.append("${SPACE_STRING}规则：${rule}\n")

							if (typeB.isEmpty()) {
								str.append("${SPACE_STRING}进房未检测到宝箱每轮数据！\n")
							}
							if (!user.account.isLoggedIn) {
								user.login((5..60).random() * 1000L)
								str.append("${SPACE_STRING}抽奖状态：(未登录)\n")
							} else if (!user.canLottery || !user.account.setting.isBoxLottery) {
								str.append("${SPACE_STRING}抽奖状态：(不参与)\n")
							} else if (user.liveLotteryIdList.boxIdSet.contains(activityId)) {
								str.append("${SPACE_STRING}抽奖状态：(已录入)\n")
							} else {
								str.append("${SPACE_STRING}抽奖状态：(成功录入)\n")
								user.liveLotteryIdList.boxIdSet.add(activityId)
								GlobalScope.launch {
									delay((3..30).random() * 1000L)
									checkBoxLottery(user, roomId, activityId)
								}
							}
						}
						pk?.forEach {
							str.append("${SPACE_STRING}开启PK抽奖[${it.pkId}]，剩余时间${it.time}s\n")
							if (user.canLottery && user.account.setting.isPkLottery && !user.liveLotteryIdList.pkIdSet.contains(it.pkId)) {
								GlobalScope.launch {
									delay((0..(it.time)).random() * 990L)
									user.liveLotteryIdList.pkIdSet.remove(it.pkId)
									joinPKLottery(user, roomId, it.pkId)
								}
							}
						}

						user.addLog(str.toString())
					}
				}
		} catch (e: ApiFailureException) {
			e.jsonObject.run {
				PCController.printlnMsg("${str}失败${toString()}")
			}
		} catch (e: ResponseConvertException) {
			PCController.printlnMsg("${str}失败${e.response.message}")
		} catch (e: HttpException) {
			PCController.printlnMsg("${str}失败${Tool.getStackTraceInfo(e)}")
		} catch (e: IOException) {
			PCController.printlnMsg("${str}失败${Tool.getStackTraceInfo(e)}")
		}
	}

	suspend fun checkLotteryInfo(user: User, roomId: Long) {
		var str: StringBuilder = StringBuilder("获取${roomId}房抽奖信息-进房")
		supervisorScope {
			try {
				LiveRoomApi.APP.enterRoom(user, roomId)
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
		str = StringBuilder("获取${roomId}房抽奖信息")
		try {
			RetrofitExtension.buildRetrofit(
				LiveLotteryService.BASE_URL,
				LiveLotteryService::class.java,
				user.addHeaderInterceptor,
				user.addParamInterceptor
			).getLotteryInfo(roomId)
				.data.run {
					str.append("成功：\n")

					guard?.filter {
						str.append("$SPACE_STRING${it.sender.uname}[UID:${it.sender.uid}]赠送的")
							.append(
								when (it.privilegeType) {
									1 -> "总督抽奖"
									2 -> "提督抽奖"
									3 -> "舰长抽奖"
									else -> "未知舰队抽奖[${it.privilegeType}]"
								}
							).append("[${it.id}] 到期时间：${TimeUtils.unixToStrTime(Instant.now().epochSecond + it.time)}")

						if (!user.account.isLoggedIn) {
							user.login((5..60).random() * 1000L)
							str.append("(未登录)\n")
							false
						} else if (!user.canLottery || !user.account.setting.isGuardLottery) {
							str.append("(不参与)\n")
							false
						} else if (user.liveLotteryIdList.guardIdSet.contains(it.id)) {
							str.append("(已录入)\n")
							false
						} else {
							str.append("(成功录入)\n")
							true
						}
					}?.associateWith {
						user.liveLotteryIdList.guardIdSet.add(it.id)
						(0..it.time * 1000).random() % ((3..20).random() * 60 * 1000L)//对10分钟取余，防止等待过久占内存
					}?.toList()?.sortedBy { it.second }?.let {
						GlobalScope.launch {
							var wait: Long
							for (i: Int in it.indices) {
								wait = if (i == 0)
									it[i].second
								else
									it[i].second - it[i - 1].second

								delay(wait)
								user.liveLotteryIdList.guardIdSet.remove(it[i].first.id)
								joinGuardLottery(
									user,
									roomId,
									it[i].first.id,
									it[i].first.keyword,
									it[i].first.privilegeType
								)
							}
						}
					}
					/*guard?.forEach {
						val guardId = it.id
						var waitTime = it.time
						val type = it.keyword
						val guardType = it.privilegeType
						str.append("$SPACE_STRING${it.sender.uname}[UID:${it.sender.uid}]赠送的")
							.append(
								when (guardType) {
									1 -> "总督抽奖"
									2 -> "提督抽奖"
									3 -> "舰长抽奖"
									else -> "未知舰队抽奖[$guardType]"
								}
							)
						str.append("[${guardId}] 到期时间：${TimeUtils.unixToStrTime(Instant.now().epochSecond + waitTime)}")
						if (user.liveLotteryIdList.guardIdSet.contains(guardId)) {
							str.append("(已录入)\n")
						} else if (user.canLottery && user.account.setting.isGuardLottery) {
							user.liveLotteryIdList.guardIdSet.add(guardId)
							waitTime = (0..waitTime * 1000).random() % ((5..15).random() * 60 * 1000)//对10分钟取余，防止等待过久占内存
							str.append(
								"(成功录入，抽奖时间：${TimeUtils.currentToStrTime(System.currentTimeMillis() + waitTime)})\n"
							)
							GlobalScope.launch {
								delay(waitTime.toLong())
								user.liveLotteryIdList.guardIdSet.remove(guardId)
								joinGuardLottery(
									user,
									roomId,
									guardId,
									type,
									guardType
								)
							}
						} else {
							str.append("(不参与)\n")
						}
					}*/
					giftList?.filter {
						str.append(
							"$SPACE_STRING${it.fromUser.uname}赠送的${it.title}[${it.raffleId}]，抽奖时间：%s---%s ".format(
								TimeUtils.unixToStrTime(it.timeWait + Instant.now().epochSecond),
								TimeUtils.unixToStrTime(it.time + Instant.now().epochSecond)
							)
						)
						if (!user.account.isLoggedIn) {
							user.login((5..60).random() * 1000L)
							str.append("(未登录)\n")
							false
						} else if (!user.canLottery || !user.account.setting.isRaffleLottery) {
							str.append("(不参与)\n")
							false
						} else if (user.liveLotteryIdList.raffleIdSet.contains(it.raffleId)) {
							str.append("(已录入)\n")
							false
						} else {
							str.append("(成功录入)\n")
							true
						}
					}?.associateWith {
						{
							user.liveLotteryIdList.raffleIdSet.add(it.raffleId)
							if (it.timeWait > 0) it.timeWait * 1000 else 0
						}.run {
							(this.invoke()..(it.time * 1000)).random().toLong()
						}
					}?.toList()?.sortedBy { it.second }?.let {
						GlobalScope.launch {
							var wait: Long
							for (i: Int in it.indices) {
								wait = if (i == 0)
									it[i].second
								else
									it[i].second - it[i - 1].second

								delay(wait)
								user.liveLotteryIdList.raffleIdSet.remove(it[i].first.raffleId)
								joinRaffleLottery(
									user,
									roomId,
									it[i].first.raffleId,
									it[i].first.type,
									it[i].first.title,
									it[i].first.fromUser.uname
								)
							}
						}
					}
					/*giftList?.forEach {
						val raffleId = it.raffleId
						val type = it.type
						val sender = it.fromUser.uname
						val title = it.title
						var waitTime = it.timeWait
						val endTime = it.time
						str.append(
							"$SPACE_STRING ${sender}赠送的$title[$raffleId]，抽奖时间：%s---%s ".format(
								TimeUtils.unixToStrTime(waitTime + Instant.now().epochSecond),
								TimeUtils.unixToStrTime(endTime + Instant.now().epochSecond)
							)
						)
						if (user.liveLotteryIdList.raffleIdSet.contains(raffleId)) {
							str.append("(已录入)\n")
						} else if (user.account.isLoggedIn && user.account.setting.isRaffleLottery) {
							user.liveLotteryIdList.raffleIdSet.add(raffleId)
							waitTime = if (waitTime > 0) waitTime * 1000 else 0
							waitTime = (waitTime..endTime * 1000).random()
							str.append("(成功录入，等待${waitTime / 1000}s)\n")
							GlobalScope.launch {
								delay(waitTime.toLong())
								user.liveLotteryIdList.raffleIdSet.remove(raffleId)
								joinRaffleLottery(
									user,
									roomId,
									raffleId,
									type,
									title,
									sender
								)
							}
						} else {
							str.append("(不参与)\n")
						}
					}*/
					activityBox?.run {
						str.append("${SPACE_STRING}开启宝箱抽奖[${activityId}]\n")
							.append("${SPACE_STRING}轮数：${currentRound}/${typeB.size}\n")
							.append("${SPACE_STRING}标题：${title}\n")
							.append("${SPACE_STRING}规则：${rule}\n")

						if (typeB.isEmpty()) {
							str.append("${SPACE_STRING}进房未检测到宝箱每轮数据！\n")
						}
						if (!user.account.isLoggedIn) {
							user.login((5..60).random() * 1000L)
							str.append("${SPACE_STRING}抽奖状态：(未登录)\n")
						} else if (!user.canLottery || !user.account.setting.isBoxLottery) {
							str.append("${SPACE_STRING}抽奖状态：(不参与)\n")
						} else if (user.liveLotteryIdList.boxIdSet.contains(activityId)) {
							str.append("${SPACE_STRING}抽奖状态：(已录入)\n")
						} else {
							str.append("${SPACE_STRING}抽奖状态：(成功录入)\n")
							user.liveLotteryIdList.boxIdSet.add(activityId)
							GlobalScope.launch {
								delay((3..30).random() * 1000L)
								checkBoxLottery(user, roomId, activityId)
							}
						}
					}
					pk?.forEach {
						str.append("${SPACE_STRING}开启PK抽奖[${it.pkId}]，剩余时间${it.time}s\n")
						if (user.canLottery && user.account.setting.isPkLottery && !user.liveLotteryIdList.pkIdSet.contains(it.pkId)) {
							GlobalScope.launch {
								delay((0..(it.time)).random() * 990L)
								user.liveLotteryIdList.pkIdSet.remove(it.pkId)
								joinPKLottery(user, roomId, it.pkId)
							}
						}
					}

					user.addLog(str.toString())
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

	private suspend fun joinRaffleLottery(user: User, roomId: Long, raffleId: Long, type: String, title: String, senderName: String?) {
		val str = "参与${roomId}房${senderName}赠送的${title}[${raffleId}]"
		withContext(PCController.workerDispatcher) {
			try {
				RetrofitExtension.buildRetrofit(
					LiveLotteryService.BASE_URL,
					LiveLotteryService.Raffle::class.java,
					user.addHeaderInterceptor,
					user.addParamInterceptor
				).getAward(roomId, raffleId, type)
					.data.run {
						user.account.accountStatus = 0//todo 添加正常抽奖

						val msg = "${str}成功：获得${giftNum}个${giftName}~"
						user.addLog(msg)
						//user.println(msg)

						user.addAward(
							giftName, giftNum,
							"${TimeUtils.unixStrTime} 恭喜[${user.account.userNameStr}]在${roomId}房${senderName}赠送的${title}[${raffleId}]中获得${giftNum}个${giftName}"
						)
					}

			} catch (e: ApiFailureException) {
				e.jsonObject.run {
					if (this["msg"].asString.contains("访问被拒绝")) {
						user.addLog("${str}失败：${toString()}")
						user.account.accountStatus = 4
					} else if (this["msg"].asString.contains("请先登录")) {
						user.addLog("${str}失败：${toString()}")
						user.println("${str}失败：${toString()}")
						user.account.accountStatus = 1
						user.login((5..30).random() * 1000L, true)
					} else when (this["code"].asInt) {
						0 -> {
						}
						else -> {
							user.addLog("${str}失败：${toString()}")
							user.println("${str}失败：${toString()}")
						}
					}
				}
			} catch (e: ResponseConvertException) {
				user.addLog(e.response.message)
			} catch (e: HttpException) {
				user.addLog(Tool.getStackTraceInfo(e))
			} catch (e: IOException) {
				user.addLog(Tool.getStackTraceInfo(e))
			}
		}
	}

	private suspend fun joinGuardLottery(user: User, roomId: Long, guardId: Long, type: String, guardType: Int) {
		var str = when (guardType) {
			1 -> "总督抽奖"
			2 -> "提督抽奖"
			3 -> "舰长抽奖"
			else -> "未知舰队抽奖[$guardType]"
		}
		str = "参与${roomId}房${str}[$guardId]"
		withContext(PCController.workerDispatcher) {
			try {
				RetrofitExtension.buildRetrofit(
					LiveLotteryService.BASE_URL,
					LiveLotteryService.Guard::class.java,
					user.addHeaderInterceptor,
					user.addParamInterceptor
				).getAward(roomId, guardId, type)
					//.run { println(this) }
					.data.run {
						user.account.accountStatus = 0
						//todo 添加正常抽奖

						val msg = "${str}成功：${message}~"
						user.addLog(msg)
						//user.println(msg)

						user.addAward(
							if (awardId == "1") {
								"辣条"
							} else {
								"亲密度"
							}, Regex("\\d+").find(message)?.value?.toInt() ?: 0
						)
					}
			} catch (e: ApiFailureException) {
				e.jsonObject.run {
					str = "${str}失败：${toString()}"
					if (this["msg"].asString.contains("访问被拒绝")) {
						user.addLog(str)
						user.account.accountStatus = 4
					} else if (this["msg"].asString.contains("请先登录")) {
						user.addLog(str)
						user.println(str)
						user.account.accountStatus = 1
						user.login((5..30).random() * 1000L, true)
					} else when (this["code"].asInt) {
						0 -> {
						}
						else -> {
							user.addLog(str)
							user.println(str)
						}
					}
				}
			} catch (e: ResponseConvertException) {
				user.addLog(e.response.message)
			} catch (e: HttpException) {
				user.addLog(Tool.getStackTraceInfo(e))
			} catch (e: IOException) {
				user.addLog(Tool.getStackTraceInfo(e))
			}
		}
	}

	private suspend fun joinPKLottery(user: User, roomId: Long, pkId: Long) {
		var str = "参与${roomId}房PK抽奖[$pkId]"
		withContext(PCController.workerDispatcher) {
			try {
				RetrofitExtension.buildRetrofit(
					LiveLotteryService.BASE_URL,
					LiveLotteryService.PK::class.java,
					user.addHeaderInterceptor,
					user.addParamInterceptor
				).join(roomId, pkId).data.run {
					if (awardId == "1") {
						str = "${str}成功：获得辣条x${awardNum}"
						user.addAward("辣条", awardNum)
					} else {
						str = "${str}成功：获得特殊奖励-${awardText}"
					}
					user.addLog(str)
				}
			} catch (e: ApiFailureException) {
				e.jsonObject.run {
					str = "${str}失败：${toString()}"
					if (this["msg"].asString.contains("访问被拒绝")) {
						user.addLog(str)
						user.account.accountStatus = 4
					} else if (this["msg"].asString.contains("请先登录")) {
						user.addLog(str)
						user.println(str)
						user.account.accountStatus = 1
						user.login((5..30).random() * 1000L, true)
					} else when (this["code"].asInt) {
						0 -> {
						}
						else -> {
							user.addLog(str)
							user.println(str)
						}
					}
				}
			} catch (e: ResponseConvertException) {
				user.addLog("${str}失败：${e.response.message}")
			} catch (e: HttpException) {
				user.addLog("${str}失败：${Tool.getStackTraceInfo(e)}")
			} catch (e: IOException) {
				user.addLog("${str}失败：${Tool.getStackTraceInfo(e)}")
			}
		}
	}

	private suspend fun checkBoxLottery(user: User, roomId: Long, aid: Long) {
		val str = StringBuilder("获取${roomId}房宝箱抽奖[$aid]")
		withContext(PCController.workerDispatcher) {
			try {
				RetrofitExtension.buildRetrofit(
					LiveLotteryService.BASE_URL,
					LiveLotteryService.ActivityBox::class.java,
					user.addHeaderInterceptor,
					user.addParamInterceptor
				).getStatus(aid).data.run {
					str.append("成功：\n")
						.append("标题：${title}\n")
						.append("规则：${rule}\n")
						.append("轮数：${currentRound}/${typeB.size}\n")
					if (typeB.isEmpty()) {
						str.append("未检测到具体轮数信息\ndata=${this}")
						user.liveLotteryIdList.boxIdSet.remove(aid)
					} else {
						typeB.also {
							it.forEach { round ->
								str.append("第${round.roundNum}轮：\n")
									.append("\t抽奖时间：${TimeUtils.unixToStrTime(round.joinStartTime)}---${TimeUtils.unixToStrTime(round.joinEndTime)}\n")
									.append("\t开奖时间：${round.startTime}\n")
									.append(
										"\t状态(${round.status})：%s\n".format(
											when (round.status) {
												-1 -> "未开始"
												0 -> "未参与"
												1 -> "已参与"
												2 -> "已开奖，中奖"
												3 -> "已开奖，未参与"
												4 -> "已开奖，未中奖"
												else -> "未知的状态${round.status}"
											}
										)
									).append("\t奖品：\n")
								round.list.forEach { gift ->
									str.append("\t\t${gift.jpName} x ${gift.jpNum}个\n")
								}
							}
						}
						typeB[currentRound - 1].run {
							when (status) {
								-1 -> PCController.bossPool.schedule(
									{ GlobalScope.launch { checkBoxLottery(user, roomId, aid) } },
									(joinStartTime..joinEndTime).random() - Instant.now().epochSecond,
									TimeUnit.SECONDS
								)
								0 -> {
									joinBoxLotteryRound(user, roomId, aid, currentRound)
									PCController.bossPool.schedule(
										{ GlobalScope.launch { checkBoxLottery(user, roomId, aid) } },
										joinEndTime - Instant.now().epochSecond + (0..55).random(),
										TimeUnit.SECONDS
									)
								}
								2, 3, 4 -> {
									getBoxLotteryRoundWinner(user, roomId, aid, currentRound)
									if (currentRound < typeB.size) {
										PCController.bossPool.schedule(
											{ GlobalScope.launch { checkBoxLottery(user, roomId, aid) } },
											typeB[currentRound].run { (joinStartTime..joinEndTime).random() } - Instant.now().epochSecond,
											TimeUnit.SECONDS
										)
									}
								}
								else -> {
								}
							}
							Unit
						}
					}
					user.addLog(str.toString())
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
	}

	private suspend fun joinBoxLotteryRound(user: User, roomId: Long, aid: Long, round: Int) {
		val str = StringBuilder("参加${roomId}房宝箱抽奖[${aid}]第${round}轮")
		try {
			RetrofitExtension.buildRetrofit(
				LiveLotteryService.BASE_URL,
				LiveLotteryService.ActivityBox::class.java,
				user.addHeaderInterceptor,
				user.addParamInterceptor
			).draw(aid, round).run {
				user.addLog("${str}成功")
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

	private suspend fun getBoxLotteryRoundWinner(user: User, roomId: Long, aid: Long, round: Int) {
		val str = StringBuilder("获取${roomId}房宝箱抽奖[${aid}]第${round}轮中奖信息")
		try {
			RetrofitExtension.buildRetrofit(
				LiveLotteryService.BASE_URL,
				LiveLotteryService.ActivityBox::class.java,
				user.addHeaderInterceptor,
				user.addParamInterceptor
			).getWinnerGroup(aid, round).data.run {
				str.append("成功：")
					.append("%s中奖(无法判定是否已开奖)%s\n".format(if (status == 0) "未" else "", if (status == 1) "获得奖品：${giftTitle}" else ""))
				groups.forEach { group ->
					str.append("奖品：")
						.append(group.giftTitle)
						.append("\n中奖者：\n")
					group.list.forEach {
						str.append(it.uname).append("[${it.uid}]\n")
					}
				}
				user.addLog(str.toString())
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
}

