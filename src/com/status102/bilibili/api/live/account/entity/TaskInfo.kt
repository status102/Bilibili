package com.status102.bilibili.api.live.account.entity

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import java.io.Serializable

//{"code":0,"msg":"ok","message":"ok","data":{"box_info":{"freeSilverFinish":false,"freeSilverTimes":1,"max_times":5,"minute":3,"silver":30,"status":0,"times":1,"times_mobile":1,"times_web":1,"type":1},"sign_info":{"text":"","specialText":"","status":0,"allDays":30,"curMonth":4,"curYear":2020,"curDay":27,"curDate":"2020-4-27","hadSignDays":0,"newTask":0,"signDaysList":[],"signBonusDaysList":[]},"watch_info":{"task_id":"single_watch_task","status":0,"progress":{"now":0,"max":1},"awards":[{"name":"银瓜子","type":"silver","num":500}]},"double_watch_info":{"task_id":"double_watch_task","status":0,"web_watch":0,"mobile_watch":0,"progress":{"now":0,"max":2},"awards":[{"name":"银瓜子","type":"silver","num":700},{"name":"友爱金","type":"union_money","num":1000},{"name":"亲密度","type":"intimacy","num":20}]},"login_info":{"mobile_login":0,"web_login":0},"live_time_info":{"status":false,"minute":0}}}
//{"code":0,"msg":"ok","message":"ok","data":{"box_info":{"freeSilverFinish":false,"freeSilverTimes":1,"max_times":5,"minute":6,"silver":80,"status":0,"times":2,"times_mobile":1,"times_web":1,"type":2},"sign_info":{"text":"","specialText":"","status":0,"allDays":30,"curMonth":4,"curYear":2020,"curDay":27,"curDate":"2020-4-27","hadSignDays":0,"newTask":0,"signDaysList":[],"signBonusDaysList":[]},"watch_info":{"task_id":"single_watch_task","status":0,"progress":{"now":0,"max":1},"awards":[{"name":"银瓜子","type":"silver","num":500}]},"double_watch_info":{"task_id":"double_watch_task","status":0,"web_watch":0,"mobile_watch":1,"progress":{"now":1,"max":2},"awards":[{"name":"银瓜子","type":"silver","num":700},{"name":"友爱金","type":"union_money","num":1000},{"name":"亲密度","type":"intimacy","num":20}]},"login_info":{"mobile_login":0,"web_login":0},"live_time_info":{"status":false,"minute":0}}}
/*
{"code":0,"msg":"ok","message":"ok",
"data":{
	"box_info":{
		"freeSilverFinish":false,
		"freeSilverTimes":1,
		"max_times":5,
		"minute":3,
		"silver":30,
		"status":0,
		"times":1,
		"times_mobile":1,
		"times_web":1,
		"type":1},
	"sign_info":{
		"text":"",
		"specialText":"",
		"status":0,
		"allDays":30,
		"curMonth":4,
		"curYear":2020,
		"curDay":27,
		"curDate":"2020-4-27",
		"hadSignDays":0,
		"newTask":0,
		"signDaysList":[],
		"signBonusDaysList":[]},
	"watch_info":{
		"task_id":"single_watch_task",
		"status":0,
		"progress":{"now":0,"max":1},
		"awards":[{"name":"银瓜子","type":"silver","num":500}]
		},
	"double_watch_info":{
		"task_id":"double_watch_task",
		"status":0,
		"web_watch":0,
		"mobile_watch":0,
		"progress":{"now":0,"max":2},
		"awards":[
			{"name":"银瓜子","type":"silver","num":700},
			{"name":"友爱金","type":"union_money","num":1000},
			{"name":"亲密度","type":"intimacy","num":20}]
	},
	"login_info":{"mobile_login":0,"web_login":0},
	"live_time_info":{"status":false,"minute":0}
}}
 */
class TaskInfo(
	@SerializedName("code")
	var coded: Int, // 0
	@SerializedName("msg")
	var msg: String,// "ok"
	@SerializedName("message")
	var message: String,// "ok",
	@SerializedName("data")
	var data: Data
) : Serializable {
	data class Data(
		/**
		 * 银瓜子宝箱数据
		 */
		@SerializedName("box_info")
		var boxInfo: BoxInfo?,
		/**
		 * 签到（废弃）
		 */
		@Deprecated("已经不再显示是否签到，请从签到信息接口获取是否签到")
		@SerializedName("sign_info")
		var signInfo: SignInfo?,
		@SerializedName("watch_info")
		var watchInfo: WatchInfo?,
		@SerializedName("double_watch_info")
		var doubleWatchInfo: DoubleWatchInfo?,
		@SerializedName("login_info")
		var loginInfo: LoginInfo?,
		@SerializedName("live_time_info")
		var liveTimeInfo: LiveTimeInfo?
	) : Serializable {
		data class BoxInfo(
			@SerializedName("freeSilverFinish")
			var freeSilverFinish: Boolean, // false,
			@SerializedName("freeSilverTimes")
			var freeSilverTimes: Int,// 1,
			@SerializedName("max_times")
			var maxTimes: Int,// 5,
			@SerializedName("minute")
			var minute: Int,// 3,
			@SerializedName("silver")
			var silver: Int,// 30,
			@SerializedName("status")
			var status: Int,// 0,
			@SerializedName("times")
			var times: Int,// 1,
			@SerializedName("times_mobile")
			var timesMobile: Int,// 1,
			@SerializedName("times_web")
			var timesWeb: Int,// 1,
			@SerializedName("type")
			var type: Int// 1
		) : Serializable

		data class SignInfo(
			@SerializedName("text")
			var text: String,// ""
			@SerializedName("specialText")
			var specialText: String,// ""
			@SerializedName("status")
			var status: Int,// 0
			@SerializedName("allDays")
			var allDays: Int,// 0
			@SerializedName("curMonth")
			var curMonth: Int,// 0
			@SerializedName("curYear")
			var curYear: Int,// 0
			@SerializedName("curDay")
			var curDay: Int,// 0
			@SerializedName("curDate")
			var curDate: String,// "2020-4-27"
			@SerializedName("hadSignDays")
			var hadSignDays: Int,// 0
			@SerializedName("newTask")
			var newTask: Int,// 0
			@SerializedName("signDaysList")
			var signDaysList: List<JsonObject>,// []
			@SerializedName("signBonusDaysList")
			var signBonusDaysList: List<JsonObject>// []
		) : Serializable

		data class WatchInfo(
			@SerializedName("task_id")
			var taskId: String,// "single_watch_task",
			@SerializedName("status")
			var status: Int,// 0,
			@SerializedName("progress")
			var progress: Progress,// {"now":0,"max":1},
			@SerializedName("awards")
			var awards:List<Award>// [ {"name":"银瓜子","type":"silver","num":500 }]
		):Serializable{
			data class Progress(
				@SerializedName("now")
				var now:Int,
				@SerializedName("max")
				var max:Int
			):Serializable
			data class Award(
				@SerializedName("name")
				var name:String,
				@SerializedName("type")
				var type:String,
				@SerializedName("num")
				var num:Int
			):Serializable
		}
		data class DoubleWatchInfo(
			@SerializedName("task_id")
			var taskId: String,// "double_watch_task",
			@SerializedName("status")
			var status: Int,// 0,
			@SerializedName("web_watch")
			var webWatch:Int,// 0,
			@SerializedName("mobile_watch")
			var mobileWatch:Int,// 0,
			@SerializedName("progress")
			var progress: Progress,// {"now":0,"max":1},
			@SerializedName("awards")
			var awards:List<Award>
		) : Serializable{
			data class Progress(
				@SerializedName("now")
				var now:Int,
				@SerializedName("max")
				var max:Int
			):Serializable
			data class Award(
				@SerializedName("name")
				var name:String,
				@SerializedName("type")
				var type:String,
				@SerializedName("num")
				var num:Int
			):Serializable
		}
		data class LoginInfo(
		@SerializedName("mobile_login")
			var mobileLogin:Int,// 0,
			@SerializedName("web_login")
			var web_login:Int// 0},
		) : Serializable
		data class LiveTimeInfo(
		@SerializedName("status")
			var status:Boolean,// false,
			@SerializedName("minute")
			var minute:Int// 0
		) : Serializable
	}
}