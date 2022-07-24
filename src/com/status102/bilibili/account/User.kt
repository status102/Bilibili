package com.status102.bilibili.account

import com.status102.bilibili.BilibiliProperty
import com.status102.bilibili.PCController
import com.status102.bilibili.api.live.account.AccountApi
import com.status102.bilibili.api.live.lottery.LiveLotteryApi
import com.status102.bilibili.api.passport.PassportApi
import com.status102.bilibili.plugin.WatchLive
import com.status102.bilibili.retrofit.exception.ApiFailureException
import com.status102.bilibili.retrofit.exception.ResponseConvertException
import com.status102.bilibili.retrofit.interceptor.AddHeaderInterceptor
import com.status102.bilibili.retrofit.interceptor.AddParamInterceptor
import com.status102.bilibili.tool.Tool
import com.status102.utils.DateUtils
import com.status102.utils.TimeUtils
import io.netty.handler.codec.http.HttpHeaderNames
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.File
import java.io.FileOutputStream
import java.time.Instant
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class User {
	companion object {
		const val reLoginInternal = 4 * 60 * 60// 4小时重登间隔
	}

	var account: AccountK by Delegates.observable(AccountK().also {
		it.user = this
	}) { kProperty: KProperty<*>, accountK: AccountK, accountK1: AccountK ->
		accountK1.user = this
	}
	lateinit var accountMD5: String
	val isInBlack: Boolean
		get() = account.accountStatus in listOf(4, 8)
	private var loginTime: Long = -1
	private var log = StringBuilder()
	private var awardMap: MutableMap<String, Int> = mutableMapOf()

	val watchLive by lazy { WatchLive(this) }

	var tryLogin = false
	val canLottery: Boolean
		get() = when (account.accountStatus) {
			0, 4 -> true
			else -> false
		}

	val addHeaderInterceptor: AddHeaderInterceptor by lazy {
		AddHeaderInterceptor(
			HttpHeaderNames.COOKIE.toString() to {
				if (account.cookieList.isNotEmpty()) account.cookieList.reduceIndexed { index, acc, s ->
					if (index == 0) {
						"${acc}$s"
					} else {
						"${acc}; $s"
					}
				} else ""
			},
			BilibiliProperty.Header.DISPLAY_ID to { BilibiliProperty.buildVersionId + "-" + Instant.now().epochSecond.toString() },
			BilibiliProperty.Header.DEVICE_ID to { account.device.hardwareId },
			BilibiliProperty.Header.BUILD_VERSION_ID to { BilibiliProperty.buildVersionId },
			BilibiliProperty.Header.USER_AGENT to { account.device.userAgent })
	}
	val addParamInterceptor: AddParamInterceptor by lazy {
		AddParamInterceptor(
			BilibiliProperty.Param.ACCESS_KEY to { account.accessToken ?: "" },
			BilibiliProperty.Param.APP_KEY to { BilibiliProperty.appKey },
			BilibiliProperty.Param.BUILD to { BilibiliProperty.build },
			BilibiliProperty.Param.DEVICE to { BilibiliProperty.platform },
			BilibiliProperty.Param.MOBILE_APP to { BilibiliProperty.platform },
			BilibiliProperty.Param.PLATFORM to { BilibiliProperty.platform },
			BilibiliProperty.Param.TIMESTAMP to { Instant.now().epochSecond.toString() }
		)
	}

	val liveLotteryIdList: LiveLotteryApi.LiveLotteryIdList by lazy { LiveLotteryApi.LiveLotteryIdList() }

	/**
	 * 加载各类插件，检查并返回账号是否登录
	 */
	suspend fun loadAccount(): Boolean {
		if (account.isLoggedIn) {
			loadPlugin()
		} else {
			login((5..120).random() * 1000L)
		}
		return account.isLoggedIn
	}

	suspend fun login(delayTime: Long, strictLogin: Boolean = false, reLoginFrequently: Boolean = false) {
		if (account.loginName.isBlank() || account.password.isBlank()) {
			addLog("账号或密码为空")
			println("账号或密码为空")
			return
		}
		if (strictLogin || account.accountStatus in 1..2 || account.accessToken.isNullOrBlank() || account.refreshToken.isNullOrEmpty()) {
			if (!reLoginFrequently) {
				if (tryLogin) {
					return
				}
				tryLogin = true
				addLog("等待${delayTime / 1000}s后登录")
				println("等待${delayTime / 1000}s后登录")
				delay(delayTime)
				if (!tryLogin) {
					return
				}
			}
			val loginTime = Instant.now().epochSecond
			if (!reLoginFrequently && this.loginTime != -1L && loginTime - this.loginTime >= reLoginInternal) {
				addLog("登录太频繁了，上次登录${TimeUtils.unixToStrTime(loginTime)}")
				return
			}
			this.loginTime = loginTime
			try {
				account.run { PassportApi.login(this@User, loginName, password) }
					.apply {
						when (data.status) {
							0 -> {
								addLog("登录成功")
								println("登录成功")
								account.accountStatus = 0

								data.tokenInfo.apply {
									account.uid = mid
									account.accessToken = accessToken
									account.refreshToken = refreshToken
								}
								account.cookieList.clear()
								data.cookieInfo.cookies.forEach {
									account.cookieList.add("${it.name}=${it.value}")
								}

								AccountApi.run {
									getMyInfo(this@User)
									checkDailyTask(this@User)
								}
							}
							1 -> {
								addLog("登录失败：需要短信/邮件验证码")
								println("登录失败：需要短信/邮件验证码")
								account.accountStatus = 31
							}
						}
					}
			} catch (e: HttpException) {
				addLog("登录失败：${e.message()}\n${Tool.getStackTraceInfo(e)}")
				println("登录失败：${e.message()}\n${Tool.getStackTraceInfo(e)}")
			} catch (e: ApiFailureException) {
				e.jsonObject.apply {
					when (this["code"].asInt) {
						//{"ts":1587626432,
						// "code":-105,
						// "data":{"url":"https://passport.bilibili.com/register/verification.html?success=1&gt=b6e5b7fad7ecd37f465838689732e788&challenge=3e9786f32883235a4142e490213d81fd&ct=1&hash=698c6ceee971c7204c9010e272a0803b"},
						// "message":"验证码错误"}
						-105 -> {
							account.accountStatus = 30
							addLog("登录失败：极验验证码错误}")
							println("登录失败：极验验证码错误}")
						}
						-629 -> {//账号或者密码错误
							account.accountStatus = 2
							account.password = ""
							addLog("登录失败：${this["message"]}")
							println("登录失败：${this["message"]}")
						}
						else -> {
							addLog("登录失败${toString()}")
							println("登录失败${toString()}")
						}
					}
				}
			} catch (e: ResponseConvertException) {
				addLog("登录失败${Tool.getStackTraceInfo(e)}")
				println("登录失败${Tool.getStackTraceInfo(e)}")
			} catch (e: IllegalStateException) {
				addLog("登录失败${Tool.getStackTraceInfo(e)}")
				println("登录失败${Tool.getStackTraceInfo(e)}")
			} finally {
				PCController.saveAccount = true
				tryLogin = false
			}
		}
	}

	private fun loadPlugin() {
		if(!watchLive.isWorking)watchLive.enterWatchRoom()
	}

	fun addAward(giftName: String, giftNum: Int, msg: String? = null) {
		if (awardMap.containsKey(giftName)) {
			awardMap[giftName] = awardMap[giftName]?.plus(giftNum) ?: giftNum
		} else {
			awardMap[giftName] = giftNum
		}
		//todo 添加礼物推送
	}

	fun addLog(msg: String, addTime: Boolean = true) {
		if (addTime) {
			log.append("${TimeUtils.unixStrTime} $msg\n")
		} else {
			log.append("$msg\n")
		}

	}

	fun writeLog(storagePath: String) {
		val logFile = File(
			"${storagePath}/${account.userNameStr}/%04d-%02d-%02d.log".format(
				DateUtils.year,
				DateUtils.month,
				DateUtils.day
			)
		)
		if (!logFile.parentFile.exists()) {
			logFile.parentFile.mkdirs()
		}
		if (!logFile.exists()) {
			logFile.createNewFile()
		}

		FileOutputStream(logFile, true).writer(Charsets.UTF_8).run {
			write(String(log).run {
				log.clear()
				replace("\n", "\r\n")
			})
			flush()
			close()
		}
	}

	fun println(msg: String, addName: Boolean = true) =
		if (addName)
			PCController.printlnMsg("[${account.userNameStr}]${msg}")
		else
			PCController.printlnMsg(msg)

	fun dailyCheck() {
		val awardMap = awardMap.toMap()
		this.awardMap.clear()
		//todo 添加每日推送
	}

	override fun equals(other: Any?): Boolean =
		if (other == null || this.javaClass != other.javaClass) false
		else {
			val user: User = other as User
			account == user.account
		}

	override fun hashCode(): Int = account.hashCode()
}