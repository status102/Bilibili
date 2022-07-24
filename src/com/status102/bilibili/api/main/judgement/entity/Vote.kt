package com.status102.bilibili.api.main.judgement.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable
//{"code":0,"message":"0","ttl":1}
data class Vote(
	@SerializedName("code")
	var code: Int,// 0
	@SerializedName("message")
	var message: String,
	@SerializedName("ttl")
	var ttl: Int
) : Serializable