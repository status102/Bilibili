package com.status102.bilibili.api.live.account.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

//{"code":0,"message":"0","ttl":1,"data":{"is_signed":false,"days":30,"sign_days":21,"h5_url":"https://live.bilibili.com/p/html/live-app-register/index.html","days_award":[{"id":1,"award":"silver","count":666,"day":5,"text":"666银瓜子","img":{"src":"http://i0.hdslb.com/bfs/live/7341feb37dc9a4f6c37b6bcf76be2012f4994ffc.png@111h_96w_1e","width":96,"height":111}},{"id":2,"award":"vip","count":3,"day":10,"text":"3天月费老爷","img":{"src":"http://i0.hdslb.com/bfs/live/e20c7e4fc4137a848dba9302714b84e0a2804535.png@111h_96w_1e","width":96,"height":111}},{"id":3,"award":"gift","count":50,"day":20,"text":"50根辣条","img":{"src":"http://i0.hdslb.com/bfs/live/a0f676e76e0be08d2e94cfe86fcb1b7541de3585.png@111h_96w_1e","width":96,"height":111}},{"id":4,"award":"title","count":1,"day":30,"text":"月老头衔","img":{"src":"http://i0.hdslb.com/bfs/live/9ec2ccd91a619abd041996ec8b41c2b61ce0ffe0.png@111h_96w_1e","width":96,"height":111}}],"awards":[{"count":6000,"text":"用户经验","award":"score","img":{"src":"http://i0.hdslb.com/bfs/live/6f26c0dbad0d49986dbc463f183ddcab76f20126.png","width":192,"height":144}},{"count":4,"text":"辣条","award":"gift","img":{"src":"http://i0.hdslb.com/bfs/live/9e770637e8155a6efe902a5bef26ad080b8918a8.png","width":192,"height":144}}]}}

class SignInfo(
	@SerializedName("code")
	var code: Int, // 0
	@SerializedName("message")
	var message: String,// "0"
	@SerializedName("ttl")
	var ttl: Int, // 1
	@SerializedName("data")
	var data: Data
) : Serializable {
	data class Data(
		@SerializedName("is_signed")
		var isSigned: Boolean,// false,
		/**
		 * 本月多少天
		 */
		@SerializedName("days")
		var days: Int,// 30,
		@SerializedName("sign_days")
		var signDays: Int,// 21,
		@SerializedName("h5_url")
		var h5Url: String,// "https://live.bilibili.com/p/html/live-app-register/index.html",
		@SerializedName("days_award")
		var daysAward: List<DaysAward>,
		@SerializedName("awards")
		var awards: List<Awards>
	) : Serializable {
		data class DaysAward(
			@SerializedName("id")
			var id: Int,// 1,
			@SerializedName("award")
			var award: String,// "silver",
			@SerializedName("count")
			var count: Int,// 666,
			@SerializedName("day")
			var day: Int,// 5,
			@SerializedName("text")
			var text: String,// "666银瓜子",
			@SerializedName("img")
			var img: Img
		) : Serializable {
			data class Img(
				@SerializedName("src")
				var src: String,// "http://i0.hdslb.com/bfs/live/7341feb37dc9a4f6c37b6bcf76be2012f4994ffc.png@111h_96w_1e",
				@SerializedName("width")
				var width: Int,// 96,
				@SerializedName("height")
				var height: Int// 111
			) : Serializable
		}
		data class Awards(
			@SerializedName("count")
			var count: Int,// 6000,
			@SerializedName("text")
			var text: String,// "用户经验",
			@SerializedName("award")
			var award: String,// "score",
			@SerializedName("img")
			var img: Img
		) : Serializable {
			data class Img(
				@SerializedName("src")
				var src: String,// "http://i0.hdslb.com/bfs/live/7341feb37dc9a4f6c37b6bcf76be2012f4994ffc.png@111h_96w_1e",
				@SerializedName("width")
				var width: Int,// 96,
				@SerializedName("height")
				var height: Int// 111
			) : Serializable
		}
	}
}