package com.status102.bilibili.api.live.lottery.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

//{"code":0,"message":"0","ttl":1,"data":{"id":988270,"gift_type":0,"award_id":"1","award_text":"辣条X1","award_image":"https://i0.hdslb.com/bfs/live/da6656add2b14a93ed9eb55de55d0fd19f0fc7f6.png","award_num":1,"title":"大乱斗获胜抽奖","award_ex_time":1588780800}}
data class PkAward(
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
		@SerializedName("id")
		var id: Long,// 988270
		@SerializedName("gift_type")
		var giftType: Int,// 0
		@SerializedName("award_id")
		var awardId: String,// "1"
		@SerializedName("award_text")
		var awardText: String,// "辣条X1"
		@SerializedName("award_image")
		var awardImage: String,
		@SerializedName("award_num")
		var awardNum: Int,// 1
		@SerializedName("title")
		var title: String,// "大乱斗获胜抽奖"
		@SerializedName("award_ex_time")
		var awardExTime: Long// 1588780800
	) : Serializable
}