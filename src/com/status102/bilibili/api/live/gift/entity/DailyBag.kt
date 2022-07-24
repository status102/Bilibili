package com.status102.bilibili.api.live.gift.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

//{"code":0,"msg":"ok","message":"ok","data":[{"type":1,"taskimg":"https://i0.hdslb.com/group1/M00/5B/83/oYYBAFazCHeAL699AAAgnkVWQzE397.png","gift_list":[{"gift_id":"1","gift_num":100,"expireat":"今天","img":"https://s1.hdslb.com/bfs/static/blive/live-assets/mobile/gift/mobilegift-static-icon/gift-1.png?20180514161652"}],"bag_name":"粉丝勋章礼包","bag_source":"「凤毛 20」","giftTypeName":"「凤毛20」<br/>粉丝勋章礼包"}]}

data class DailyBag(
	@SerializedName("code")
	var code: Int,
	@SerializedName("msg")
	var msg: String,
	@SerializedName("message")
	var message: String,
	@SerializedName("data")
	var data: List<Data>
) : Serializable {
	data class Data(
		@SerializedName("type")
		var type: Int,// 1
		@SerializedName("taskimg")
		var taskImg: String,// "https://i0.hdslb.com/group1/M00/5B/83/oYYBAFazCHeAL699AAAgnkVWQzE397.png"
		@SerializedName("gift_list")
		var giftList: List<Gift>,
		@SerializedName("bag_name")
		var bagName: String,// 粉丝勋章礼包
		@SerializedName("bag_source")
		var bagSource: String,// 「凤毛 20」
		@SerializedName("giftTypeName")
		var giftTypeName: String// 「凤毛20」<br/>粉丝勋章礼包
	) : Serializable {
		data class Gift(
			@SerializedName("gift_id")
			var giftId: String,// "1"
			@SerializedName("gift_num")
			var giftNum: Int,// 100
			@SerializedName("expireat")
			var expireat: String,// 今天
			@SerializedName("img")
			var img: String// https://s1.hdslb.com/bfs/static/blive/live-assets/mobile/gift/mobilegift-static-icon/gift-1.png?20180514161652
		) : Serializable
	}
}