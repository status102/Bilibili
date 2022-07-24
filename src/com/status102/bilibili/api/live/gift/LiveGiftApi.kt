package com.status102.bilibili.api.live.gift

import com.status102.bilibili.account.User
import com.status102.bilibili.retrofit.RetrofitExtension
import com.status102.bilibili.retrofit.exception.ApiFailureException
import com.status102.utils.ToolK
import retrofit2.HttpException
import java.io.IOException

object LiveGiftApi {
	suspend fun getDailyBagStatus(user: User) {
		try {
			RetrofitExtension.buildRetrofit(
				LiveGiftService.BASE_URL,
				LiveGiftService::class.java,
				user.addHeaderInterceptor,
				user.addParamInterceptor
			).getDailyBagStatus().run {
				when (data.result) {
					1 -> getDailyBag(user)
					2 -> user.addLog("领取每日礼包失败-已领取")
					else -> {
						user.addLog("获取每日礼包状态失败-未知状态:${data.result}")
						user.println("获取每日礼包状态失败-未知状态:${data.result}")
					}
				}
			}
		} catch (e: ApiFailureException) {
			user.addLog("获取每日礼包状态失败-:${e.jsonObject}")
			user.println("获取每日礼包状态失败-:${e.jsonObject}")
		} catch (e: HttpException) {
			user.addLog("获取每日礼包状态失败-:${e.response()?.message()}\n${ToolK.getStackTraceInfo(e)}")
			user.println("获取每日礼包状态失败-:${e.response()?.message()}\n${ToolK.getStackTraceInfo(e)}")
		} catch (e: IOException) {
			user.addLog("获取每日礼包状态失败-:${ToolK.getStackTraceInfo(e)}")
			user.println("获取每日礼包状态失败-:${ToolK.getStackTraceInfo(e)}")
		}
	}

	private suspend fun getDailyBag(user: User) {
		try {
			RetrofitExtension.buildRetrofit(
				LiveGiftService.BASE_URL,
				LiveGiftService::class.java,
				user.addHeaderInterceptor,
				user.addParamInterceptor
			).getDailyBag().run {
				val str = StringBuilder("领取每日礼包成功-")
				if (data.isEmpty()) {
					str.append("礼包为空")
				} else {
					data.forEach {
						str.append(it.giftTypeName)
						it.giftList.forEach {
							str.append(" ${if (it.giftId == "1") "辣条" else "giftId=${it.giftId}(${it.expireat})x${it.giftNum}个"}")
						}
					}
				}
				str.toString().replace("<br/>", "").run {
					user.addLog(this)
					user.println(this)
				}
			}
		} catch (e: ApiFailureException) {
			user.addLog("领取每日礼包失败-:${e.jsonObject}")
			user.println("领取每日礼包失败-:${e.jsonObject}")
		} catch (e: HttpException) {
			user.addLog("领取每日礼包失败-:${e.response()?.message()}\n${ToolK.getStackTraceInfo(e)}")
			user.println("领取每日礼包失败-:${e.response()?.message()}\n${ToolK.getStackTraceInfo(e)}")
		} catch (e: IOException) {
			user.addLog("领取每日礼包失败-:${ToolK.getStackTraceInfo(e)}")
			user.println("领取每日礼包失败-:${ToolK.getStackTraceInfo(e)}")
		}
	}
}