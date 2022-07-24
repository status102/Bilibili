package com.status102.bilibili.api.live.lottery.entity.silverbox

import com.google.gson.annotations.SerializedName
import java.io.Serializable

//{"code":0,"msg":"","message":"","data":{"minute":9,"silver":190,"time_start":1588306875,"time_end":1588307415,"times":1,"max_times":5}}
data class SilverBoxTask(
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
		@SerializedName("minute")
		var minute:Int,
		@SerializedName("silver")
		var silver:Int,
		@SerializedName("time_start")
		var timeStart:Long,
		@SerializedName("time_end")
		var timeEnd:Long,
		@SerializedName("times")
		var times:Int,
		@SerializedName("max_times")
		var maxTimes:Int
	) : Serializable
}