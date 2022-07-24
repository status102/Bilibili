package com.status102.bilibili.api.live.gift.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

//{"code":0,"msg":"ok","message":"ok","data":{"result":1}}
data class DailyBagStatus(
	@SerializedName("code")
	var code: Int,
	@SerializedName("msg")
	var msg: String,
	@SerializedName("message")
	var message: String,
	@SerializedName("data")
	var data: Data
) : Serializable {
	data class Data(
		@SerializedName("result")
		var result: Int
	) : Serializable
}