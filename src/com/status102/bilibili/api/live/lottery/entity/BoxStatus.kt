package com.status102.bilibili.api.live.lottery.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

//{"code":0,"msg":"ok","message":"ok","data":{"title":"荣耀20S新品发布会","rule":"第一波抽奖：19:00点参与，19:30开奖 荣耀畅玩8A 1部；\n第二波抽奖：19:30参与，20:30开奖，荣耀V20 1部。\n第三波抽奖：20:30参与，21:00开奖，荣耀20S 1部。","background":"https://i0.hdslb.com/bfs/live/84cd59bcb1e977359df618dbeb0f7828751f457c.png","typeB":[{"startTime":"2019-09-04 19:30:00","imgUrl":"https://i0.hdslb.com/bfs/vc/6f879abee6780918e1f6983e522ab894262c87d3.png","join_start_time":1567594800,"join_end_time":1567596600,"round_num":1,"list":[{"jp_id":"1847","jp_name":"荣耀畅玩8A","jp_pic":"https://i0.hdslb.com/bfs/vc/6f879abee6780918e1f6983e522ab894262c87d3.png","jp_num":"1","jp_type":"2","ex_text":""}],"status":4},{"startTime":"2019-09-04 20:30:00","imgUrl":"https://i0.hdslb.com/bfs/vc/285a02188a3fbca576c03a3cb1728572bb15126a.png","join_start_time":1567596600,"join_end_time":1567600200,"round_num":2,"list":[{"jp_id":"1848","jp_name":"荣耀V20","jp_pic":"https://i0.hdslb.com/bfs/vc/285a02188a3fbca576c03a3cb1728572bb15126a.png","jp_num":"1","jp_type":"2","ex_text":""}],"status":0},{"startTime":"2019-09-04 21:00:00","imgUrl":"https://i0.hdslb.com/bfs/vc/6abba21e8ccd5a53d40620784e8c00298d05d735.jpg","join_start_time":1567600200,"join_end_time":1567602000,"round_num":3,"list":[{"jp_id":"1849","jp_name":"荣耀20S","jp_pic":"https://i0.hdslb.com/bfs/vc/6abba21e8ccd5a53d40620784e8c00298d05d735.jpg","jp_num":"1","jp_type":"2","ex_text":""}],"status":-1}],"current_round":2,"activity_id":405,"activity_pic":"https://i0.hdslb.com/bfs/live/c3ed87683f6e87d256d1f5fdddbfb220fc4c2cdf.png","title_color":"#FFFFFF","closeable":0,"weight":20,"jump_url":"https://live.bilibili.com/p/html/live-app-treasurebox/index.html?is_live_half_webview=1&hybrid_biz=live-app-treasurebox&hybrid_rotate_d=1&hybrid_half_ui=1,3,100p,70p,0,0,30,100;2,2,375,100p,0,0,30,100;3,3,100p,70p,0,0,30,100;4,2,375,100p,0,0,30,100;5,3,100p,70p,0,0,30,100;6,3,100p,70p,0,0,30,100;7,3,100p,70p,0,0,30,100&aid=405"}}

data class BoxStatus(
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
		@SerializedName("title")
		var title: String,// "荣耀20S新品发布会",
		@SerializedName("rule")
		var rule: String,// "rule": "第一波抽奖：19:00点参与，19:30开奖 荣耀畅玩8A 1部；\n第二波抽奖：19:30参与，20:30开奖，荣耀V20 1部。\n第三波抽奖：20:30参与，21:00开奖，荣耀20S 1部。",
		@SerializedName("background")
		var background: String,// "https://i0.hdslb.com/bfs/live/84cd59bcb1e977359df618dbeb0f7828751f457c.png",
		@SerializedName("typeB")
		var typeB: List<Round>,
		@SerializedName("current_round")
		var currentRound: Int,// 2,
		@SerializedName("activity_id")
		var activityId: Long,// 405,
		@SerializedName("activity_pic")
		var activityPic: String, // "https://i0.hdslb.com/bfs/live/c3ed87683f6e87d256d1f5fdddbfb220fc4c2cdf.png",
		@SerializedName("title_color")
		var titleColor: String,// "#FFFFFF",
		@SerializedName("closeable")
		var closeable: Int,// 0,
		@SerializedName("weight")
		var weight: Int,// 20,
		@SerializedName("jump_url")
		var jumpUrl: String// "https://live.bilibili.com/p/html/live-app-treasurebox/index.html?is_live_half_webview=1&hybrid_biz=live-app-treasurebox&hybrid_rotate_d=1&hybrid_half_ui=1,3,100p,70p,0,0,30,100;2,2,375,100p,0,0,30,100;3,3,100p,70p,0,0,30,100;4,2,375,100p,0,0,30,100;5,3,100p,70p,0,0,30,100;6,3,100p,70p,0,0,30,100;7,3,100p,70p,0,0,30,100&aid=405"
	) : Serializable {
		data class Round(
			/**
			 * 开奖时间，而非抽奖开始时间
			 */
			@SerializedName("startTime")
			var startTime: String,// "2019-09-04 19:30:00",
			@SerializedName("imgUrl")
			var imgUrl: String,// "https://i0.hdslb.com/bfs/vc/6f879abee6780918e1f6983e522ab894262c87d3.png",
			@SerializedName("join_start_time")
			var joinStartTime: Long,// 1567594800 2019/9/4 19:00:00
			@SerializedName("join_end_time")
			var joinEndTime: Long,// 1567596600 2019/9/4 19:30:00
			@SerializedName("round_num")
			var roundNum: Int,// 1,
			@SerializedName("list")
			var list: List<Gift>,
			/**
			 * -1未开始，0未参与，1已参与，2中奖了，3未参与，4未中奖
			 */
			@SerializedName("status")
			var status: Int// 4
		) : Serializable {
			data class Gift(
				@SerializedName("jp_id")
				var jpId: Long,// "1847",
				@SerializedName("jp_name")
				var jpName: String,// "荣耀畅玩8A",
				@SerializedName("jp_pic")
				var jpPic: String,// "https://i0.hdslb.com/bfs/vc/6f879abee6780918e1f6983e522ab894262c87d3.png",
				@SerializedName("jp_num")
				var jpNum: Int,// "1",
				@SerializedName("jp_type")
				var jpType: Int,// "2",
				@SerializedName("ex_text")
				var exText: String// ""
			) : Serializable
		}
	}
}