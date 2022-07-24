package com.status102.bilibili.api.live.lottery.entity.silverbox

import com.google.gson.annotations.SerializedName
import java.io.Serializable

//{"code":0,"msg":"ok","message":"ok","data":{"silver":"2805568","awardSilver":"30","isEnd":0}}

data class SilverBoxAward(
	@SerializedName("code")
	var code: Int,
	@SerializedName("msg")
	var msg: String,
	@SerializedName("string")
	var message: String,
	@SerializedName("data")
	var data: Data
) : Serializable {
	data class Data(
		@SerializedName("silver")
		var silver: String,// "2805568"
		@SerializedName("awardSilver")
		var awardSilver: String,// "30"
		@SerializedName("isEnd")
		var isEnd: Int// 0
	) : Serializable
}