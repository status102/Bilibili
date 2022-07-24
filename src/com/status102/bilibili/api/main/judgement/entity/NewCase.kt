package com.status102.bilibili.api.main.judgement.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

//{"code":0,"message":"0","ttl":1,"data":{"id":1017720}}
//{"code":25008,"message":"真给力 , 移交众裁的举报案件已经被处理完了","ttl":1}
data class NewCase(
	@SerializedName("code")
	var code: Int,// 0
	@SerializedName("message")
	var message: String,
	@SerializedName("ttl")
	var ttl: Int,
	@SerializedName("data")
	var data: Data
) : Serializable {
	data class Data(
		@SerializedName("id")
		var id: Long
	) : Serializable
}