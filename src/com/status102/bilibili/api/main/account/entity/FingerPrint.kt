package com.status102.bilibili.api.main.account.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

//{"code":0,"message":"0","ttl":1,"data":{"bili_deviceId":"f60fe87ee2b118254f46fdf841410d1720191217232359c6c424cafe379924d6"}}
data class FingerPrint(
	@SerializedName("code")
	var code: Int,// 0
	@SerializedName("message")
	var message: String?,
	@SerializedName("ttl")
	var ttl: String?,
	@SerializedName("data")
	var data: Data
) : Serializable {
	data class Data(
		@SerializedName("bili_deviceId")
		var biliDeviceId: String?// f60fe87ee2b118254f46fdf841410d1720191217232359c6c424cafe379924d6
	) : Serializable
}