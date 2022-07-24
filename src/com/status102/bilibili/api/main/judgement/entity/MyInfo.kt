package com.status102.bilibili.api.main.judgement.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable
//{"code":0,"message":"0","ttl":1,"data":{"caseTotal":9748,"face":"http://i2.hdslb.com/bfs/face/a9fd7e530ceca83f07247a7e9d444d991c57c8d4.jpg","restDays":1,"rightRadio":94,"status":1,"uname":"雾中灯火"}}
data class MyInfo(
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
		/**
		 * 投票数
		 */
		@SerializedName("caseTotal")
		var caseTotal: Int,
		@SerializedName("face")
		var face: String,
		/**
		 * 剩余资格
		 */
		@SerializedName("restDays")
		var restDays: Int,
		/**
		 * 不明意义的参数
		 */
		@SerializedName("rightRadio")
		var rightRadio: Int,// 94
		@SerializedName("status")
		var status: Int,// 1
		@SerializedName("uname")
		var uname: String
	) : Serializable
}