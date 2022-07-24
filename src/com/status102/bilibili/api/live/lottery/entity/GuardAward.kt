package com.status102.bilibili.api.live.lottery.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

//{"code":0,"data":{"id":2377337,"type":"guard","award_id":"1","award_type":2,"time":0,"message":"获得辣条X1（赠送后相当于1点粉丝勋章亲密度）","from":"今天某某学习了没","privilege_type":3,"award_list":[{"name":"获得辣条X1（赠送后相当于1点粉丝勋章亲密度）","img":"https://i0.hdslb.com/bfs/live/d57afb7c5596359970eb430655c6aef501a268ab.png","type":0,"content":""}]},"message":"ok","msg":"ok"}
//{"code":0,"data":{"id":2377336,"type":"guard","award_id":"intimacy","award_type":2,"time":0,"message":"当前佩戴的粉丝勋章亲密度+1","from":"鸭鸭炖板栗","privilege_type":3,"award_list":[{"name":"当前佩戴的粉丝勋章亲密度<%+1%>~","img":"https://i0.hdslb.com/bfs/live/bbe74fe3adad5b6fb2936ecb78be7827f35a7fa4.png","type":0,"content":""}]},"message":"ok","msg":"ok"}
data class GuardAward(
	@SerializedName("code")
	var code: Int, // 0
	@SerializedName("message")
	var message: String?,// ok
	@SerializedName("msg")
	var msg: String?,// ok
	@SerializedName("data")
	var data: Data
) : Serializable {
	data class Data(
		@SerializedName("id")
		var guardId: Long, // 2377337
		@SerializedName("type")
		var type: String, // "guard"
		/**
		 * 1是辣条，intimacy是亲密度
		 */
		@SerializedName("award_id")
		var awardId: String, //"1" "intimacy"
		@SerializedName("award_type")
		var awardType: Int,// 2
		@SerializedName("time")
		var time: Int, // 0
		@SerializedName("message")
		var message: String,// 获得辣条X1（赠送后相当于1点粉丝勋章亲密度）
		@SerializedName("from")
		var from: String, // 今天某某学习了没
		@SerializedName("privilege_type")
		var privilegeType: Int,// 3
		@SerializedName("award_list")
		var awardList: List<AwardList>
	) : Serializable {
		data class AwardList(
			@SerializedName("name")
			var name: String, // 获得辣条X1（赠送后相当于1点粉丝勋章亲密度）
			@SerializedName("img")
			var img: String,// https://i0.hdslb.com/bfs/live/d57afb7c5596359970eb430655c6aef501a268ab.png
			@SerializedName("type")
			var type: Int,// 0
			@SerializedName("content")
			var content: String // ""
		) : Serializable
	}
}