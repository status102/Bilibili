package com.status102.bilibili.api.live.room.entity.app

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/*
{
	"code": 0,
	"message": "0",
	"ttl": 1,
	"data": {
		"refresh_row_factor": 0.125,
		"refresh_rate": 100,
		"max_delay": 5000,
		"token": "8QpG9jboPQberUKHC8dP9xIt9HeuOQftYd9M-7-Z1uk9BaPjcVBMe9Ba2acGr6mxos_sTaJwaKRCKpYh0Z-OVmtt7D00yiLTfhBoyxaw",
		"host_list": [{
			"host": "tx-bj4-live-comet-01.chat.bilibili.com",
			"port": 2243,
			"wss_port": 443,
			"ws_port": 2244
		}, {
			"host": "tx-gz3-live-comet-01.chat.bilibili.com",
			"port": 2243,
			"wss_port": 443,
			"ws_port": 2244
		}, {
			"host": "broadcastlv.chat.bilibili.com",
			"port": 2243,
			"wss_port": 443,
			"ws_port": 2244
		}],
		"ip_list": [{
			"host": "62.234.173.147",
			"port": 2243
		}, {
			"host": "134.175.51.44",
			"port": 2243
		}, {
			"host": "broadcastlv.chat.bilibili.com",
			"port": 2243
		}, {
			"host": "62.234.173.147",
			"port": 80
		}, {
			"host": "134.175.51.44",
			"port": 80
		}, {
			"host": "broadcastlv.chat.bilibili.com",
			"port": 80
		}]
	}
}
 */
data class DanmuInfo(
	@SerializedName("code")
	var code: Int,// 0
	@SerializedName("message")
	var message: String,// "0"
	@SerializedName("ttl")
	var ttl: Int,// 1
	var data: Data
) : Serializable {
	data class Data(
		@SerializedName("refresh_row_factor")
		var refreshRowFactor: Double,// 0.125
		@SerializedName("refresh_rate")
		var refreshRate: Int,// 100
		@SerializedName("max_delay")
		var maxDelay: Int,// 5000
		@SerializedName("token")
		var token: String,//8QpG9jboPQberUKHC8dP9xIt9HeuOQftYd9M-7-Z1uk9BaPjcVBMe9Ba2acGr6mxos_sTaJwaKRCKpYh0Z-OVmtt7D00yiLTfhBoyxaw
		@SerializedName("host_list")
		var hostList: List<HostData>,
		@SerializedName("ip_list")
		var ipList: List<IpData>
	) : Serializable {
		data class HostData(
			@SerializedName("host")
			var host: String, // tx-bj4-live-comet-01.chat.bilibili.com
			@SerializedName("port")
			var port: Int,// 2243
			@SerializedName("wss_port")
			var wssPort: Int, // 443
			@SerializedName("ws_port")
			var wsPort: Int// 2244
		) : Serializable

		data class IpData(
			@SerializedName("host")
			var host: String,// 62.234.173.147
			@SerializedName("port")
			var port: Int// 2243
		) : Serializable
	}
}