package com.status102.bilibili.monitor.bilive

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.status102.bilibili.PCController
import com.status102.bilibili.account.User
import com.status102.bilibili.monitor.bilive.entity.BeatStormMessage
import com.status102.bilibili.monitor.bilive.entity.LotteryMessage
import com.status102.bilibili.monitor.bilive.entity.RaffleMessage
import com.status102.utils.TimeUtils

object BiliveAnalyzer {
	const val HEAD = "抽奖广播公用服务器"

	/**
	 * {"cmd":"sysmsg","msg":"一个莫得感情的服务器，欢迎投喂"}
	 * smallTV  对应 raffle
	 * pklottery  对应 lottery
	 * {"cmd":"lottery","roomID":21395965,"id":2021030,"type":"guard","title":"舰队抽奖","time":1200}
	 * {"cmd":"beatStorm","roomID":15007670,"num":1,"id":2021031374974,"type":"beatStorm","title":"节奏风暴","time":1581386591342}
	 * {"cmd":"raffle","roomID":21761267,"id":705716,"type":"GIFT_20003","title":"摩天大楼抽奖","time":120,"max_time":120,"time_wait":60}
	 */
	fun analyze(string: String?) {
		val userList: List<User> = PCController.userList
		val jsonElement = JsonParser().parse(string)
		val jsonObject =
			if (jsonElement.isJsonNull) null else jsonElement.asJsonObject
		if (jsonObject != null && jsonObject.keySet().contains("cmd")) {
			val gson = Gson()
			when (jsonObject["cmd"].asString) {
				"sysmsg" -> PCController.printlnMsg("${HEAD}：服务器消息-${jsonObject["msg"].asString}")
				"beatStorm" -> {
					val beatStormMessage: BeatStormMessage = gson.fromJson(jsonElement, BeatStormMessage::class.java)
						.apply {
							PCController.printlnMsg("${HEAD}：${roomID}房开启节奏风暴抽奖[$id]")
						}
					PCController.workerPool.execute {
						for (user in userList) {
							if (user.account.setting.isStormLottery) {
								/*
								user.getWorkerPool().execute({
									LiveLottery.joinStorm(
										user,
										beatStormMessage.roomID.toInt(),
										beatStormMessage.id
									)
								})*/
							}
						}
					}

				}
				"raffle" -> {
					gson.fromJson(jsonElement, RaffleMessage::class.java)
						.apply {
							PCController.printlnMsg("${HEAD}：${roomID}房开启[$id]${title}")
							PCController.monitor.addCheckRoom(roomID)
						}
				}
				"lottery", "pklottery" -> {
					gson.fromJson(jsonElement, LotteryMessage::class.java)
						.apply {
							PCController.printlnMsg("${HEAD}：${roomID}房开启[$id]${title}")
							PCController.monitor.addCheckRoom(roomID)
						}

				}
				"anchor" -> {
				}//todo 完善天选抽奖
				// {"cmd":"anchor","roomID":7983476,"id":214154,"award_name":"2元滴红包~","award_num":1,"danmu":"一起关注澪绫~有条件的可爱上个船船吧~","gift_id":0,"gift_name":"","gift_num":1,"gift_price":0,"require_type":1,"require_value":0,"type":"anchor","title":"天选时刻","time":599}

				else -> PCController.printlnMsg(
					"${HEAD}收到未知抽奖${jsonObject}"
				)
			}
		}
	}
}