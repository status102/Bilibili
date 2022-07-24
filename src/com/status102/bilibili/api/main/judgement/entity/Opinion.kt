package com.status102.bilibili.api.main.judgement.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable
//{"code":0,"message":"0","ttl":1,"data":{"count":0,"opinion":null}}
//{"code":0,"message":"0","ttl":1,"data":{"count":1,"opinion":[{"mid":282989501,"opid":218977560,"vote":2,"content":"个人认为只是玩梗，无伤大雅","attr":1,"hate":0,"like":0}]}}
data class Opinion(
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
		@SerializedName("count")
		var count: Int,// 0
		/**
		 * 无评论时为空
		 */
		@SerializedName("opinion")
		var opinion: List<Opinions>?
	) : Serializable {
		data class Opinions(
			@SerializedName("mid")
			var mid: Long,// 282989501
			@SerializedName("opid")
			var opid: Long,// 218977560
			@SerializedName("vote")
			var vote: Int,// 2
			@SerializedName("content")
			var content: String,// "个人认为只是玩梗，无伤大雅"
			@SerializedName("attr")
			var attr: Int,// 1
			@SerializedName("hate")
			var hate: Int,// 0
			@SerializedName("like")
			var like: Int// 0
		) : Serializable
	}
}