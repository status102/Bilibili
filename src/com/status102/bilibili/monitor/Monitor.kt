package com.status102.bilibili.monitor

import com.status102.bilibili.PCController
import com.status102.bilibili.api.live.area.LiveAreaApi
import com.status102.bilibili.api.live.lottery.LiveLotteryApi
import com.status102.bilibili.monitor.bilibili.LiveClient
import com.status102.bilibili.monitor.bilive.BiliveAnalyzer
import com.status102.bilibili.monitor.bilive.BiliveClient
import io.netty.channel.nio.NioEventLoopGroup
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Monitor {
	companion object {
		private val eventLoopGroup by lazy { NioEventLoopGroup() }
		val biliveClient by lazy { BiliveClient(eventLoopGroup) }

		const val DEFAULT_AREA_COUNT = 5
		var areaIDMap: MutableMap<Int, String> = mutableMapOf()
		var mode: Long = -1

		val roomList = mutableListOf<LiveClient>()

		const val waitTime = 5000L
		var checkSet = mutableSetOf<Long>()
	}

	init {
		if (biliveClient.connect()) {
			PCController.printlnMsg("${BiliveAnalyzer.HEAD}连接启动")
		} else {
			PCController.printlnMsg("${BiliveAnalyzer.HEAD}连接失败")
		}
		GlobalScope.launch { refreshAreaIDMap() }
	}

	suspend fun initBiliLiveConnect(count: Long) {
		if (count == -1L) {
			mode = -1L
			if (areaIDMap.size < DEFAULT_AREA_COUNT) {
				refreshAreaIDMap()
			}
			checkAreaList()
		} else {
			//TODO 大批量连接房间检查房间数是否足够
		}
	}

	public fun resetBiliveClient(){
		biliveClient.disConnect()
		if (biliveClient.connect()) {
			PCController.printlnMsg("${BiliveAnalyzer.HEAD}连接启动")
		} else {
			PCController.printlnMsg("${BiliveAnalyzer.HEAD}连接失败")
		}
	}
	private suspend fun refreshAreaIDMap(): Unit {
		val areaIDMap = LiveAreaApi.getAreaIDMap()
		if (!areaIDMap.isNullOrEmpty()) {
			PCController.printlnMsg("获取直播分区成功：共计${areaIDMap.size}分区")
			Monitor.areaIDMap = areaIDMap
		}
	}

	private suspend fun checkAreaList() {
		if (mode == -1L) {
			val areaMap: MutableMap<Int, String> = areaIDMap
			if (areaMap.size < DEFAULT_AREA_COUNT) {
				val areaSet = (1..DEFAULT_AREA_COUNT).toSet().toMutableSet()
				if (roomList.isNotEmpty()) {
					roomList.forEach {
						areaSet.remove(it.areaId)
						if (areaSet.isEmpty()) {
							return
						}
					}
				}
				areaSet.forEach {
					addAreaRoom(it)
				}
			} else {
				if (roomList.isNotEmpty()) {
					roomList.forEach {
						areaMap.remove(it.areaId)
						if (areaMap.isEmpty()) {
							return
						}
					}
				}
				areaMap.forEach {
					addAreaRoom(it.key)
				}
			}
		} else {
		}
	}

	suspend fun addAreaRoom(areaID: Int) {
		LiveAreaApi.getAreaIDMap()
	}

	fun addCheckRoom(roomId: Long, checkImmediately: Boolean = false) {
		if (checkSet.isNotEmpty()) {
			checkSet.add(roomId)
		} else {
			checkSet.add(roomId)
			GlobalScope.launch {
				if (!checkImmediately) {
					delay(waitTime)
				}
				checkRoomLotteryInfo()
			}
		}
	}

	private suspend fun checkRoomLotteryInfo() {
		checkSet.toSet().forEach {
			checkSet.remove(it)
			checkRoomLotteryInfo(it)
		}
		if (checkSet.isNotEmpty()) {
			delay(waitTime * (1..5).random())
			checkRoomLotteryInfo()
		}
	}

	private fun checkRoomLotteryInfo(roomId: Long, randomDelay: Boolean = true) {
		PCController.workerPool.execute {
			GlobalScope.launch {
				if (randomDelay) {
					delay((0..30000).random().toLong())
				}
				LiveLotteryApi.checkLotteryInfo(PCController.userList, roomId)
			}
			/*PCController.userList.forEach {
				GlobalScope.launch {
					if (randomDelay) {
						delay((0..30000).random().toLong())
					}
					LiveLotteryApi.checkLotteryInfo(it, roomId)
				}
			}*/
		}
	}
}