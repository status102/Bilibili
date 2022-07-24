package com.status102.bilibili.account

import com.google.gson.annotations.SerializedName
import com.status102.bilibili.BilibiliProperty
import com.status102.bilibili.Hidden
import com.status102.bilibili.PCController
import com.status102.utils.TimeUtils
import java.io.Serializable
import java.util.*

data class AccountK(
	@SerializedName(value = "nickname", alternate = ["tag"])
	var nickname: String? = null,

	@SerializedName(value = "expires_time", alternate = ["endTime"])
	private var expiresTime: String? = null,

	@SerializedName(value = "server_key", alternate = ["serverKey"])
	var serverKey: String? = null,

	@SerializedName(value = "account_status", alternate = ["accountType"])
	private var accountStatusInJson: Int = 1,

	@SerializedName(value = "check_banned_times", alternate = ["blackHouseCheckTimes"])
	var checkBannedTimes: Int = 10, // 连续访问被拒绝次数达到该值判定进入小黑屋

	@SerializedName(value = "banned_time", alternate = ["enterBlackHouseTime"])
	var bannedTime: Long? = null, // 进小黑屋时间

	@SerializedName(value = "last_check_banned_time", alternate = ["lastCheckBlackHouseTime"])
	var lastCheckBannedTime: Long? = null, // 上次检查时间 出黑屋后转为出黑屋时间

	@SerializedName(value = "check_banned_interval", alternate = ["checkBlackHouseInterval"])
	var checkBannedInterval: Int = 8 * 60 * 60, // 检测小黑屋间隔
	var uid: Long = -1,

	@SerializedName(value = "user_name", alternate = ["username"])
	var userName: String? = null,

	@SerializedName(value = "login_name", alternate = ["accountID"])
	var loginName: String = "", // 登录名

	@SerializedName(value = "password", alternate = ["passWord"])
	var password: String = "",
	@SerializedName(value = "access_token", alternate = ["accessToken"])
	private var accessToken_json: String? = null,

	@SerializedName(value = "refresh_token", alternate = ["refreshToken"])
	private var refreshToken_json: String? = null,

	@SerializedName(value = "cookie_list")
	var cookieList: MutableList<String> = ArrayList(),

	@SerializedName(value = "device")
	private var device_json: Device = Device(),
	@SerializedName(value = "setting")
	var setting: Setting = Setting(),
	@SerializedName(value = "limit")
	var limit: Limit = Limit()

) : Serializable {
	var accountStatus: Int
		set(value) {
			if(accountStatus != value) {
				accountStatusInJson = value
				PCController.saveAccount = true
			}
		}
		get() = accountStatusInJson
	val accountStatusStr: String
		get() = when (accountStatusInJson) {
			-1 -> "延期"
			0 -> "正常"
			1 -> "未登录"
			2 -> "账号或密码错误"
			30 -> "需要极验验证码"
			31 -> "需要短信/邮件验证码"
			4 -> "小黑屋"
			8 -> "出黑屋后等待"
			else -> "未知状态[$accountStatusInJson]"
		}

	var expiresUnix: Long
		set(value) {
			expiresTime = TimeUtils.unixToStrTime(value)
			PCController.saveAccount = true
		}
		get() = TimeUtils.strToUnixTime(expiresTime)
	var accessToken: String?
		set(value) {
			accessToken_json = if (value?.isNotBlank() == true) {
				value
			} else {
				null
			}
			PCController.saveAccount = true
		}
		get() = accessToken_json
	var refreshToken: String?
		set(value) {
			refreshToken_json = if (value?.isNotBlank() == true) {
				value
			} else {
				null
			}
			PCController.saveAccount = true
		}
		get() = refreshToken_json
	var device: Device
		set(value) {
			device_json = value
			PCController.saveAccount = true
		}
		get() = device_json

	val isLoggedIn: Boolean
		get() = when (accountStatusInJson) {
			-1, 0, 4, 8 -> true
			else -> false
		}

	val userNameStr: String
		get() = StringBuilder().run {
			if (!nickname.isNullOrBlank()) {
				append("${nickname}---")
			}
			if (userName.isNullOrBlank()) {
				append("(${loginName})")
			} else {
				append(userName)
			}
			return toString()
		}
	val csrf: String
		get() {
			return cookieList.first { it.startsWith("bili_jct=") }.replace("bili_jct=", "")
		}

	@Hidden
	lateinit var user: User

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val account = other as AccountK
		return loginName == account.loginName
	}

	data class Device(
		@SerializedName(value = "hardware_id", alternate = ["hardwareId"])
		private var hardwareId_json: String = randomHardwareID(),// OwMxUjMDZgRgUGdTL1Mv 在header里key为Device-ID

		@SerializedName(value = "local_id", alternate = ["localId"])
		private var localId_json: String? = null, //f60fe87ee2b118254f46fdf841410d17 2020 03 10 144158 db5f8f105cdaee81e9

		@SerializedName(value = "android_version", alternate = ["androidVersion"])
		private var androidVersion_json: Int = 9,

		@SerializedName(value = "manufacturer")
		private var manufacturer_json: String? = null,// Xiaomi

		@SerializedName(value = "model")
		private var model_json: String? = null,// Redmi Note 7 Pro

		@SerializedName(value = "user_agent", alternate = ["userAgent"])
		private var userAgent_json: String = BilibiliProperty.defaultUserAgent// Mozilla/5.0 BiliDroid/5.54.0 (bbcallen@gmail.com) os/android model/Redmi Note 7 Pro mobi_app/android build/5540500 channel/xiaomi innerVer/5540500 osVer/9 network/2
	) : Serializable {
		var hardwareId: String
			set(value) {
				hardwareId_json = value
				PCController.saveAccount = true
			}
			get() = hardwareId_json
		var localId: String?
			set(value) {
				localId_json = value
				PCController.saveAccount = true
			}
			get() = localId_json
		var androidVersion: Int
			set(value) {
				androidVersion_json = value
				PCController.saveAccount = true
			}
			get() = androidVersion_json
		var manufacturer: String?
			set(value) {
				manufacturer_json = value
				PCController.saveAccount = true
			}
			get() = manufacturer_json
		var model: String?
			set(value) {
				model_json = value
				PCController.saveAccount = true
			}
			get() = model_json
		var userAgent: String
			set(value) {
				userAgent_json = value
				PCController.saveAccount = true
			}
			get() = userAgent_json

		companion object {
			fun randomHardwareID(length: Int = (20..32).random()): String {
				val stringBuilder = StringBuilder()
				var char: Char
				for (i in 0 until length) {
					char = 'a' + (0..25).random()
					stringBuilder.append(
						if ((-1..10).random() >= 0) {
							if ((0..1).random() == 0) {
								char.toUpperCase()
							} else {
								char
							}
						} else {
							(1..9).random()
						}
					)
				}
				return stringBuilder.toString()
			}
		}
	}

	data class Setting(
		@SerializedName(value = "daily_sign", alternate = ["dailySign"])
		var isDailySign: Boolean = false,

		@SerializedName(value = "exchange_silver_to_coin")
		var isExchangeSilverToCoin: Boolean = false,

		@SerializedName(value = "group_sign", alternate = ["groupSign"])
		var isGroupSign: Boolean = false,

		@SerializedName(value = "main_task", alternate = ["mainTask"])
		var isMainTask: Boolean = false,

		@SerializedName(value = "twice_watch", alternate = ["twiceWatch"])
		var isTwiceWatch: Boolean = false,

		@SerializedName(value = "judgement")
		var isJudgement: Boolean = false,

		@SerializedName(value = "free_silver", alternate = ["freeSilver"])
		var isFreeSilver: Boolean = false,

		@SerializedName(value = "raffle_lottery", alternate = ["activityLottery", "activity_lottery"])
		var isRaffleLottery: Boolean = false,

		@SerializedName(value = "raffle_ignore", alternate = ["lotteryIgnore", "lottery_ignore"])
		var raffleIgnore: Int = 0,

		@SerializedName(value = "pk_lottery")
		var isPkLottery: Boolean = false,

		@SerializedName(value = "pk_ignore")
		var pkIgnore: Int = 0,

		@SerializedName(value = "box_lottery", alternate = ["boxLottery"])
		var isBoxLottery: Boolean = false,

		@SerializedName(value = "guard_lottery", alternate = ["shipLottery", "ship_lottery"])
		var isGuardLottery: Boolean = false,

		@SerializedName(value = "guard_ignore", alternate = ["shipIgnore", "ship_ignore"])
		var guardIgnore: Int = 0,

		@SerializedName(value = "storm_lottery", alternate = ["stormLottery"])
		var isStormLottery: Boolean = false,

		@SerializedName(value = "storm_ignore", alternate = ["stormIgnore"])
		var stormIgnore: Int = 0,

		@SerializedName(value = "storm_interval", alternate = ["stormInterval"])
		var stormInterval: Int = 80,

		@SerializedName(value = "storm_random_interval", alternate = ["stormRandomInterval"])
		var stormRandomInterval: Int = 60,

		@SerializedName(value = "storm_try_times", alternate = ["stormTryTimes"])
		var stormTryTimes: Int = 15,

		@SerializedName(value = "storm_limit", alternate = ["stormLimit"])
		var stormLimit: Int = -1,

		@SerializedName(value = "feed_medal", alternate = ["feedMedal"])
		var isFeedMedal: Boolean = false,

		@SerializedName(value = "send_expires_gift", alternate = ["outdate"])
		var isSendExpiresGift: Boolean = false,

		@SerializedName(value = "send_expires_gift_room", alternate = ["outdateRoom"])
		var sendExpiresGiftRoom: Int = 0,

		@SerializedName(value = "dynamic_lottery", alternate = ["dynamicLottery"])
		var isDynamicLottery: Boolean = false,

		@SerializedName(value = "dynamic_lottery_at_UID", alternate = ["dynamicLotteryAtUID"])
		var dynamicLotteryAtUID: MutableList<Int> = ArrayList(),

		@SerializedName(value = "dynamic_lottery_ignore_UID", alternate = ["dynamicLotteryIgnoreUID"])
		var dynamicLotteryIgnoreUID: MutableList<Int> = ArrayList(),

		@SerializedName(value = "dynamic_lottery_keyword_used", alternate = ["dynamicLotteryKeywordUsed"])
		var isDynamicLotteryKeywordUsed: Boolean = false,

		@SerializedName(value = "dynamic_lottery_keyword", alternate = ["dynamicLotteryKeyword"])
		var dynamicLotteryKeyword: MutableList<String> = ArrayList(),

		@SerializedName(value = "dynamic_lottery_ignore_keyword_used", alternate = ["dynamicLotteryIgnoreKeywordUsed"])
		var isDynamicLotteryIgnoreKeywordUsed: Boolean = false,

		@SerializedName(value = "dynamic_lottery_ignore_keyword", alternate = ["dynamicLotteryIgnoreKeyword"])
		var dynamicLotteryIgnoreKeyword: MutableList<String> = ArrayList(),

		@SerializedName(value = "dynamic_lottery_thank_word", alternate = ["dynamicLotteryThankWord"])
		var dynamicLotteryThankWord: MutableList<String> = ArrayList(),

		@SerializedName(value = "un_subscribe", alternate = ["unSubscribe"])
		var isUnSubscribe: Boolean = false,

		@SerializedName(value = "delete_dynamic", alternate = ["deleteSpace"])
		var isDeleteDynamic: Boolean = false,

		@SerializedName(value = "live_assistant_room", alternate = ["liveAssistant"])
		var liveAssistantRoom: MutableList<Int> = ArrayList(),

		@SerializedName(value = "send_after_sign", alternate = ["sendAfterSign"])
		var isSendAfterSign: Boolean = false,

		@SerializedName(value = "send_after_judgement", alternate = ["sendAfterJudgement"])
		var isSendAfterJudgement: Boolean = false

	) : Serializable

	data class Limit(
		@SerializedName(value = "op")
		var isOp: Boolean = false,

		@SerializedName(value = "daily_sign", alternate = ["dailySign"])
		var isDailySign: Boolean = true,

		@SerializedName(value = "group_sign", alternate = ["groupSign"])
		var isGroupSign: Boolean = true,

		@SerializedName(value = "main_task", alternate = ["mainTask"])
		var isMainTask: Boolean = true,

		@SerializedName(value = "watch_exp", alternate = ["watchExp"])
		var isWatchExp: Boolean = true,

		@SerializedName(value = "twice_watch", alternate = ["twiceWatch"])
		var isTwiceWatch: Boolean = true,

		@SerializedName(value = "judgement")
		var isJudgement: Boolean = true,

		@SerializedName(value = "free_silver", alternate = ["freeSilver"])
		var isFreeSilver: Boolean = true,

		@SerializedName(value = "raffle_lottery", alternate = ["activityLottery", "activity_lottery"])
		var isRaffleLottery: Boolean = true,

		@SerializedName(value = "pk_lottery")
		var isPkLottery: Boolean = true,

		@SerializedName(value = "box_lottery", alternate = ["boxLottery"])
		var isBoxLottery: Boolean = true,

		@SerializedName(value = "guard_lottery", alternate = ["shipLottery", "ship_lottery"])
		var isGuardLottery: Boolean = true,

		@SerializedName(value = "storm_lottery", alternate = ["stormLottery"])
		var isStormLottery: Boolean = true,

		@SerializedName(value = "feed_medal", alternate = ["feedMedal"])
		var isFeedMedal: Boolean = true,

		@SerializedName(value = "send_expires_gift", alternate = ["outdate"])
		var isSendExpiresGift: Boolean = true,

		@SerializedName(value = "dynamic_lottery", alternate = ["dynamicLottery"])
		var isDynamicLottery: Boolean = true

	) : Serializable
}