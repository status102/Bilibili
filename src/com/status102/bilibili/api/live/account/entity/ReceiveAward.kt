package com.status102.bilibili.api.live.account.entity

import com.google.gson.JsonArray
import com.google.gson.annotations.SerializedName
import java.io.Serializable
//{"code":0,"msg":"success","message":"success","data":[]}
//{"code":-400,"msg":"奖励尚未完成","message":"奖励尚未完成","data":[]}
data class ReceiveAward(
	@SerializedName("code")
	var code: Int,
	@SerializedName("msg")
	var msg: String,
	@SerializedName("message")
	var message: String,
	@SerializedName("data")
	var data: JsonArray
):Serializable