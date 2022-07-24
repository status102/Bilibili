package com.status102.bilibili.api.live.lottery.entity

import com.google.gson.JsonArray
import com.google.gson.annotations.SerializedName
import java.io.Serializable

//{"code":0,"msg":"ok","message":"ok","data":[]}
//{"code":400,"msg":"访问被拒绝","message":"访问被拒绝","data":[]}
//{"code":-3,"msg":"已抽过啦","message":"已抽过啦","data":[]}
//{"code":-400,"msg":"参数错误","message":"参数错误","data":[]}
data class DrawActivityBox(
	@SerializedName("code")
	var coded: Int, // 0
	@SerializedName("msg")
	var msg: String,// "ok"
	@SerializedName("message")
	var message: String,// "ok",
	@SerializedName("data")
	var data: JsonArray// []
) : Serializable