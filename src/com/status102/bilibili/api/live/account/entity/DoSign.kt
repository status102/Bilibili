package com.status102.bilibili.api.live.account.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable
//{"code":0,"message":"0","ttl":1,"data":{"text":"6000点用户经验,4根辣条","specialText":"","allDays":30,"hadSignDays":22,"isBonusDay":0}}
data class DoSign(
	@SerializedName("code")
	var code: Int, // 0
	@SerializedName("message")
	var message: String,// "0"
	@SerializedName("ttl")
	var ttl: Int, // 1
	@SerializedName("data")
	var data: Data
) : Serializable {
	data class Data(
		@SerializedName("text")
		var text: String,// "6000点用户经验,4根辣条",
		@SerializedName("specialText")
		var specialText: String,// "",
		@SerializedName("allDays")
		var allDays: Int,// 30,
		@SerializedName("hadSignDays")
		var hadSignDays: Int,// 22,
		@SerializedName("isBonusDay")
		var isBonusDay: Int// 0
	) : Serializable
}