package com.status102.bilibili.api.live.lottery.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable
//{"code":0,"data":{"raffleId":1044624,"type":"small_tv","gift_id":"0","gift_name":"辣条","gift_num":5,"gift_from":"亦星离_","gift_type":0,"gift_rank":0,"gift_image":"http://i0.hdslb.com/bfs/live/da6656add2b14a93ed9eb55de55d0fd19f0fc7f6.png","sender_type":0,"gift_content":"","toast1":"","toast2":"","award_ex_time":1587571200},"message":"","msg":""}
data class RaffleAward(
	@SerializedName("code")
	var code: Int, // 0
	@SerializedName("message")
	var message: String?,
	@SerializedName("ttl")
	var ttl: Int, // 1
	@SerializedName("data")
	var data: Data
) : Serializable {
	data class Data(
		@SerializedName("raffleId")
		var raffleId: Long, // 376915
		@SerializedName("type")
		var type: String, // "GIFT_30282"
		@SerializedName("gift_id")
		var giftId: String, // "1"
		@SerializedName("gift_name")
		var giftName: String, // "辣条"
		@SerializedName("gift_num")
		var giftNum: Int, // 10
		@SerializedName("gift_from")
		var giftFrom: String, // "狐妖Mikan"
		@SerializedName("gift_type")
		var giftType: Int, // 0
		@SerializedName("gift_rank")
		var giftRank: Int, // 0
		@SerializedName("gift_image")
		var giftImage: String, // "http://i0.hdslb.com/bfs/live/da6656add2b14a93ed9eb55de55d0fd19f0fc7f6.png"
		@SerializedName("sender_type")
		var senderType: Int, // 1
		@SerializedName("gift_content")
		var giftContent: String, // ""
		@SerializedName("toast1")
		var toast1: String, // ""
		@SerializedName("toast2")
		var toast2: String // ""
	) : Serializable
}