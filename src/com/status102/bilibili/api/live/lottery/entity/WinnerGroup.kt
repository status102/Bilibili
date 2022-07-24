package com.status102.bilibili.api.live.lottery.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class WinnerGroup(
	@SerializedName("code")
	var code: Int,// 0
	@SerializedName("msg")
	var msg: String,// "ok"
	@SerializedName("message")
	var message: String,// "ok"
	@SerializedName("data")
	var data: Data
) : Serializable {
	data class Data(
		@SerializedName("uid")
		var uid: Long,// 24434483,
		@SerializedName("status")
		var status: Int,// 1,
		@SerializedName("giftTitle")
		var giftTitle: String,// "小米10手机",
		@SerializedName("prize_pic")
		var prizePic: String,// "https://i0.hdslb.com/bfs/live/300c7264ef15d3711f04aa8cc6314f9bff5c3708.jpg",
		@SerializedName("type")
		var type: Int,// 1,
		@SerializedName("code")
		var code: String,// "",
		@SerializedName("totalNum")
		var totalNum: Int,// 12,
		@SerializedName("groups")
		var groups: List<Group>
	) : Serializable {
		data class Group(
			@SerializedName("giftTitle")
			var giftTitle: String,// "小米10手机",
			@SerializedName("list")
			var list: List<User>
		) : Serializable {
			data class User(
				@SerializedName("uid")
				var uid: Long,// 24434483,
				@SerializedName("uname")
				var uname: String,// "旺仔牛奶117",
				@SerializedName("head_pic")
				var headPic: String,// "https://i2.hdslb.com/bfs/face/768586384cb64a98bb1b91b8518806d0dc034452.jpg",
				@SerializedName("abox_type")
				var aboxType: Int,// 0,
				@SerializedName("abox_pic")
				var aboxPic: String,// "",
				@SerializedName("iden_type")
				var idenType: Int,// 2,
				@SerializedName("iden_pic")
				var idenPic: String,// 这张照片是大会员角标 "https://i0.hdslb.com/bfs/vc/2bce18ae8d91635da822f7ef013b07e52d054478.png",
				@SerializedName("is_vip")
				var isVip: Int// 1
			) : Serializable
		}
	}
}