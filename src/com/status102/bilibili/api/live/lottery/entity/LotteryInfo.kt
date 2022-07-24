package com.status102.bilibili.api.live.lottery.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.time.Instant

data class LotteryInfo(
	@SerializedName("code")
	var code: Int, // 0
	@SerializedName("message")
	var message: String?,
	@SerializedName("ttl")
	var ttl: Int, // 1
	@SerializedName("data")
	var data: Data
) : Serializable {
	data class Data(
		@SerializedName("slive_box")
		var silverBox: SilverBox?,
		//@SerializedName("bls_box")
		//var blsBox: Any?,// apk反编的entity里面没有该项
		//@SerializedName("danmu")
		//var danmu: Any?,// apk反编的entity里面没有该项
		@SerializedName("danmu_gift")
		var danmuGift: LiveDanmakuLottery?,
		@SerializedName("gift_list")
		var giftList: List<Raffle>?,
		@SerializedName("guard")
		var guard: List<Guard>?,
		@SerializedName("pk")
		var pk: List<PK>?,
		@SerializedName("storm")
		var storm: Storm?,
		@SerializedName("anchor")
		var anchor: AnchorLottery?,// TODO 完善anchor
		@SerializedName("activity_box")
		var activityBox: ActivityBox?,
		@SerializedName("icon_tap_time")
		var iconTapTime: Int = 10
	) : Serializable {
		data class AnchorLottery(
			@SerializedName("asset_icon")
			var assetIcon: String = "",
			@SerializedName("award_image")
			var awardImage: String = "",
			@SerializedName("award_name")
			var awardName: String = "",
			@SerializedName("award_num")
			var awardNum: Int,
			@SerializedName("award_users")
			var awardUsers: MutableList<AnchorLotteryAwardUser> = mutableListOf(),
			@SerializedName("cur_gift_num")
			var curGiftNum: Int?,
			@SerializedName("current_time")
			var currentTime: Int?,
			@SerializedName("danmu")
			var danmu: String = "",
			@SerializedName("gift_id")
			var giftId: Long?,
			@SerializedName("gift_name")
			var giftName: String = "",
			@SerializedName("gift_num")
			var giftNum: Int?,
			@SerializedName("goaway_time")
			var goawayTime: Int?,
			@SerializedName("id")
			var id: Long?,
			@SerializedName("join_type")
			var joinType: Int?,
			var localStartTime: Long = Instant.now().epochSecond,
			@SerializedName("lot_status")
			var lotStatus: Int?,
			@SerializedName("max_time")
			var maxTime: Int?,
			@SerializedName("require_text")
			var requireText: String = "",
			@SerializedName("require_type")
			var requireType: Int?,
			@SerializedName("require_value")
			var requireValue: Int?,
			@SerializedName("room_id")
			var roomId: Long?,
			@SerializedName("show_panel")
			var showPanel: Int,
			@SerializedName("status")
			var status: Int,
			@SerializedName("time")
			var time: Int,
			@SerializedName("url")
			var url: String = ""
		) : Serializable {
			companion object {
				const val STATUS_AWARDED = 2
				const val STATUS_ENDED = 1
				const val STATUS_ONGOING = 0
			}

			data class AnchorLotteryAwardUser(
				@SerializedName("face")
				var face: String = "",
				@SerializedName("level")
				@JvmField
				var level: Int,
				@SerializedName("uid")
				var uid: Long,
				@SerializedName("uname")
				var uname: String = ""
			) : Serializable
		}

		data class ActivityBox(
			@SerializedName("title")
			var title: String,// 休想打败我的生活”直播抽奖 第二日
			@SerializedName("rule")
			var rule: String,// 抽奖规则： 2月12-14日，每天上午10点至晚上12点，每个整点抽送小米10手机1台。其中，2月13日下午2点-4点，#小米10发布会#直播期间，特别放送每半小时抽奖小米10手机12台。2月15日，上午10点抽送小米10手机1台。
			@SerializedName("current_round")
			var currentRound: Int,// 15
			@SerializedName("typeB")
			var typeB: List<BoxRoundInfo>,
			@SerializedName("activity_pic")
			var activityPic: String,// https://i0.hdslb.com/bfs/live/c3ed87683f6e87d256d1f5fdddbfb220fc4c2cdf.png
			@SerializedName("activity_id")
			var activityId: Long,// 488
			@SerializedName("weight")
			var weight: Int,// 20
			@SerializedName("background")
			var background: String,// https://i0.hdslb.com/bfs/live/84cd59bcb1e977359df618dbeb0f7828751f457c.png
			@SerializedName("title_color")
			var titleColor: String,// #FFFFFF
			@SerializedName("closeable")
			var closeable: Int,// 0
			@SerializedName("jump_url")
			var jumpUrl: String// https://live.bilibili.com/p/html/live-app-treasurebox/index.html?is_live_half_webview=1&hybrid_biz=live-app-treasurebox&hybrid_rotate_d=1&hybrid_half_ui=1,3,100p,70p,0,0,30,100:                            ,2,2,375,100p,0,0,30,100:                            ,3,3,100p,70p,0,0,30,100:                            ,4,2,375,100p,0,0,30,100:                            ,5,3,100p,70p,0,0,30,100:                            ,6,3,100p,70p,0,0,30,100:                            ,7,3,100p,70p,0,0,30,100&aid=488
		) : Serializable {
			data class BoxRoundInfo(
				/**
				 * 开奖时间
				 */
				@SerializedName("startTime")
				var startTime: String,// 2020-02-13 10:59:00

				@SerializedName("imgUrl")
				var imgUrl: String,// https://i0.hdslb.com/bfs/live/300c7264ef15d3711f04aa8cc6314f9bff5c3708.jpg

				@SerializedName("join_start_time")
				var joinStartTime: Long,// 1581559200

				@SerializedName("join_end_time")
				var joinEndTime: Long,// 1581562740

				@SerializedName("status")
				var status: Int,// -2

				@SerializedName("list")
				var list: List<Gift>,

				@SerializedName("round_num")
				var roundNum: Int// 1
			) : Serializable {
				data class Gift(
					@SerializedName("jp_name")
					var jpName: String,// 小米10手机

					@SerializedName("jp_num")
					var jpNum: String,// 1

					@SerializedName("jp_id")
					var jpId: Int,// 2345

					@SerializedName("jp_type")
					var jpType: Int,// 2

					@SerializedName("ex_text")
					var exText: String?,

					@SerializedName("jp_pic")
					var jpPic: String// https://i0.hdslb.com/bfs/live/300c7264ef15d3711f04aa8cc6314f9bff5c3708.jpg
				) : Serializable
			}
		}

		data class Guard(
			@SerializedName("id")
			var id: Long,// 1362917
			/**
			 * 貌似一般只有uid信息
			 */
			@SerializedName("sender")
			var sender: Sender,

			@SerializedName("keyword")
			var keyword: String,// guard

			@SerializedName("privilege_type")
			var privilegeType: Int,// 3

			/**
			 * 抽奖剩余有效时间，秒为单位
			 */
			@SerializedName("time")
			var time: Int,// 859

			@SerializedName("status")
			var status: Int,// 1

			@SerializedName("payflow_id")
			var payFlowId: String // "gds_ec97ce6bd35922e301_201908"

		) : Serializable {
			companion object {
				const val LOTTERY_CAPTAIN = 3
				const val LOTTERY_COMMANDER = 2
				const val LOTTERY_GOVERNOR = 1
			}

			data class Sender(
				@SerializedName("uid")
				var uid: Long,
				@SerializedName("uname")
				var uname: String,
				@SerializedName("face")
				var face: String
			) : Serializable
		}

		data class LiveDanmakuLottery(
			@SerializedName("award_num")
			var awardAmount: Int?,

			@SerializedName("award_image")
			var awardIcon: String = "",

			@SerializedName("award_name")
			var awardName: String = "",

			@SerializedName("current_time")
			var currenTime: Long?,

			@SerializedName("danmu")
			var danmu: String = "",

			@SerializedName("asset_icon")
			var entranceIcon: String = "",

			@SerializedName("gift_id")
			var giftId: Long?,

			@SerializedName("gift_name")
			var giftName: String = "",

			@SerializedName("id")
			var id: Long?,

			@SerializedName("time")
			var leftTime: Long?,

			@SerializedName("join_type")
			var lotteryType: Int?,

			@SerializedName("price")
			var price: Long?,

			@SerializedName("require_text")
			var requireText: String = "",

			@SerializedName("require_type")
			var requireType: Int?,

			@SerializedName("require_value")
			var requireValue: Long?,

			@SerializedName("room_id")
			var roomId: Long?,

			@SerializedName("cur_gift_num")
			var sendedGiftAmount: Int?,

			@SerializedName("show_panel")
			var showPanel: Int?,

			@SerializedName("status")
			var status: Int?,

			@SerializedName("max_time")
			var totalTime: Long?
		) : Serializable {
			companion object {
				const val REQUIRE_TYPE_FANS_MEDAL = 2
				const val REQUIRE_TYPE_FOLLOW = 1
				const val REQUIRE_TYPE_GUARD = 3
				const val REQUIRE_TYPE_NONE = 0
				const val STATUS_JOINED = 2
			}

			val showPanelWhenStart: Boolean
				get() = showPanel == 1

			val hasJoined: Boolean
				get() = status == 2

			val isDanmuLottery: Boolean
				get() = lotteryType == 0

			val isGiftLottery: Boolean
				get() = lotteryType == 1

			val requireTypeNone: Boolean
				get() = requireType == 0

			val requireTypeFollow: Boolean
				get() = requireType == 1

			val requireTypeFansMedal: Boolean
				get() = requireType == 2

			val requireTypeGuard: Boolean
				get() = requireType == 3
		}

		data class PK(
			@SerializedName("id")
			var id: Long,
			@SerializedName("pk_id")
			var pkId: Long,// 486002
			@SerializedName("room_id")
			var roomId: Long,// 2974177
			@SerializedName("time")
			var time: Int,// 29
			@SerializedName("status")
			var status: Int,// 1
			@SerializedName("asset_icon")
			var assetIcon: String,// "https://i0.hdslb.com/bfs/vc/44c367b09a8271afa22853785849e65797e085a1.png",
			@SerializedName("asset_animation_pic")
			var assetAnimationPic: String,// "https://i0.hdslb.com/bfs/vc/03be4c2912a4bd9f29eca3dac059c0e3e3fc69ce.gif",
			@SerializedName("title")
			var title: String,// "恭喜主播大乱斗胜利"
			@SerializedName("max_time")
			var maxTime: Int// 120

		) : Serializable

		data class Raffle(

			@SerializedName("raffleId")
			var raffleId: Long,// 376898

			@SerializedName("title")
			var title: String,// "应援喵抽奖"

			@SerializedName("type")
			var type: String,// "GIFT_30277"

			@SerializedName("payflow_id")
			var payFlowId: String,// 1565535420111900001

			@SerializedName("from_user")
			var fromUser: FromUser,

			@SerializedName("time_wait")
			var timeWait: Int,// 51

			@SerializedName("time")
			var time: Int,// 111

			@SerializedName("max_time")
			var maxTime: Int,// 120

			@SerializedName("status")
			var status: Int,// 1

			@SerializedName("asset_animation_pic")
			var assetAnimationPic: String,// "http://i0.hdslb.com/bfs/vc/99829aae8070858899da42c81579dc069083155c.gif",

			@SerializedName("asset_tips_pic")
			var assetTipsPic: String,// "http://s1.hdslb.com/bfs/live/2090d88c508d47e0670bec57d9126e576d998c34.png",

			@SerializedName("sender_type")
			var senderType: Int// 0
		) : Serializable {
			data class FromUser(
				@SerializedName("uname")
				var uname: String?,// "米立の米一"
				@SerializedName("face")
				var face: String?// "http://i0.hdslb.com/bfs/face/7def6077fad6fed916603a377fb906b1429f6804.jpg"
			) : Serializable
		}

		data class Storm(
			@SerializedName("id")
			var id: Long,// 1362954757223  据说前面为ID，后6位为随机数
			@SerializedName("num")
			var num: Int,
			@SerializedName("time")
			var time: Int,
			@SerializedName("content")
			var content: String,
			@SerializedName("hadJoin")
			var hadJoin: Int,
			@SerializedName("storm_gif")
			var stormGif: String?
		) : Serializable

		data class SilverBox(
			@SerializedName("minute")
			var minute: Int,// 9
			@SerializedName("silver")
			var silver: Int,// 190
			@SerializedName("time_end")
			var timeEnd: Long,// 1565535581
			@SerializedName("time_start")
			var timeStart: Long,// 1565535041
			/**
			 * 当前为第几轮
			 */
			@SerializedName("times")
			var times: Int,// 5
			/**
			 * 最多多少轮
			 */
			@SerializedName("max_times")
			var maxTimes: Int, // 5
			/**
			 * 宝箱状态，正常 0，当天领完 1 同时times,time_end,time_start为0
			 */
			@SerializedName("status")
			var status: Int
		) : Serializable
	}
}