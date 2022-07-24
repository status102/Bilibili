package com.status102.bilibili.api.main.judgement.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable
//{"code":0,"message":"0","ttl":1,"data":{"id":1017251,"mid":329225355,"status":1,"statusTitle":"封禁7天","originType":1,"reasonType":9,"originContent":"10个啥b拿着手机打王者荣耀不是电竞，十头肥猪围着电脑打游戏就是电竞了？真是有够好笑的:)","punishResult":2,"punishTitle":"在评论中发布引战言论","judgeType":0,"originUrl":"https://www.bilibili.com/video/BV1s7411f7G1/#reply2552089488","blockedDays":7,"putTotal":221,"voteRule":32,"voteBreak":200,"voteDelete":190,"startTime":1587782345,"endTime":1587868745,"ctime":1587745161,"mtime":1587782946,"originTitle":"【什么是电竞】不只是游戏而已。英雄联盟向","relationId":"2552089488-1-96584935","face":"http://i1.hdslb.com/bfs/face/d3f3dd40c3a31012e9a03cd3560586f39d674abb.jpg","uname":"愛溜冰的猪","vote":0,"expiredMillis":1799494,"case_type":0}}
data class CaseInfo(
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
		 * 案件cid
		 */
		@SerializedName("id")
		var id: Long,
		/**
		 * 被举报人的uid
		 */
		@SerializedName("mid")
		var mid: Long,
		@SerializedName("status")
		var status: Int,// 1
		@SerializedName("statusTitle")
		var statusTitle: String,// 封禁7天
		@SerializedName("originType")
		var originType: Int,// 1
		@SerializedName("reasonType")
		var reasonType: Int,// 9
		@SerializedName("originContent")
		var originContent: String,// "10个啥b拿着手机打王者荣耀不是电竞，十头肥猪围着电脑打游戏就是电竞了？真是有够好笑的:)"
		@SerializedName("punishResult")
		var punishResult: Int,// 2
		@SerializedName("punishTitle")
		var punishTitle: String,// "在评论中发布引战言论"
		@SerializedName("judgeType")
		var judgeType: Int,// 0
		@SerializedName("originUrl")
		var originUrl: String,// "https://www.bilibili.com/video/BV1s7411f7G1/#reply2552089488"
		/**
		 * 对应statusTitle中封禁天数
		 */
		@SerializedName("blockedDays")
		var blockedDays: Int,// 7
		@SerializedName("putTotal")
		var putTotal: Int,// 0
		@SerializedName("voteRule")
		var voteRule: Int,// 0
		@SerializedName("voteBreak")
		var voteBreak: Int,// 0
		@SerializedName("voteDelete")
		var voteDelete: Int,// 0
		@SerializedName("startTime")
		var startTime: Long,// 1587782345 2020/4/25 10:39:05
		@SerializedName("endTime")
		var endTime: Long,// 1587868745 2020/4/26 10:39:05
		@SerializedName("ctime")
		var ctime: Long,// 1587745161 2020/4/25 00:19:21
		@SerializedName("mtime")
		var mtime: Long,// 1587782946 2020/4/25 10:49:06

		@SerializedName("originTitle")
		var originTitle: String,// "【什么是电竞】不只是游戏而已。英雄联盟向"
		@SerializedName("relationId")
		var relationId: String,// "2552089488-1-96584935"
		@SerializedName("face")
		var face: String,// "【什么是电竞】不只是游戏而已。英雄联盟向"
		@SerializedName("uname")
		var uname: String,// "愛溜冰的猪"
		/**
		 * 自己的投票情况，0未投，1是，2否，3弃权
		 */
		@SerializedName("vote")
		var vote: Int,// 0
		/**
		 * 投票剩余时限（毫秒）
		 */
		@SerializedName("expiredMillis")
		var expiredMillis: Long?,// 1799494
		@SerializedName("case_type")
		var caseType: String// 0
	) : Serializable
}