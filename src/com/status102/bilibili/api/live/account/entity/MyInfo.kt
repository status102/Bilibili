package com.status102.bilibili.api.live.account.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MyInfo(
	@SerializedName("code")
	var code: Int, // 0
	@SerializedName("message")
	var message: String,
	@SerializedName("ttl")
	var ttl: Int, // 1
	@SerializedName("data")
	var data: Data
) : Serializable {
	data class Data(
		@SerializedName("uid")
		var uid: Long,// 55411725
		@SerializedName("uname")
		var userName: String,// "雾中灯火"
		@SerializedName("face")
		var faceUrl: String,// "https://i2.hdslb.com/bfs/face/a9fd7e530ceca83f07247a7e9d444d991c57c8d4.jpg"
		@SerializedName("billCoin")
		var billCoin: Double,// "billCoin":1819.7
		@SerializedName("silver")
		var silver: Long,// "silver":2783438
		@SerializedName("gold")
		var gold: Long,// 300
		@SerializedName("achieve")
		var achieve: Long,// 670
		@SerializedName("vip")
		var vip: Int,// 1
		@SerializedName("svip")
		var svip: Int,// 1
		@SerializedName("user_level")
		var userLevel: Int,// 50
		@SerializedName("user_next_level")
		var userNextLevel: Int,// 51
		@SerializedName("user_intimacy")
		var userIntimacy: Long,// 70804680
		@SerializedName("user_next_intimacy")
		var userNextIntimacy: Long,// 200000000
		@SerializedName("is_level_top")
		var isLevelTop: Int,// 0
		@SerializedName("user_level_rank")
		var userLevelRank: String,// "4924"
		@SerializedName("user_charged")
		var userCharged: Int,// 0
		@SerializedName("identification")
		var indentification: Int// 1
	) : Serializable
}