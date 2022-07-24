package com.status102.bilibili.api.live.account

import com.status102.bilibili.PCController
import com.status102.bilibili.account.User
import com.status102.bilibili.retrofit.RetrofitExtension
import com.status102.bilibili.retrofit.exception.ApiFailureException
import com.status102.bilibili.retrofit.exception.ResponseConvertException
import com.status102.utils.ToolK
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

object AccountApi {
	suspend fun getMyInfo(user: User) = withContext(PCController.workerDispatcher) {
		val str = "获取个人信息"
		try {
			RetrofitExtension.buildRetrofit(
				AccountService.BASE_URL,
				AccountService::class.java,
				user.addHeaderInterceptor,
				user.addParamInterceptor
			).getMyInfo().run {
				user.account.uid = data.uid
				user.account.userName = data.userName
				user.addLog("${str}成功，Lv.${data.userLevel}(${data.userIntimacy}/${data.userNextIntimacy})排名: ${data.userLevelRank}，银瓜子: ${data.silver}，金瓜子: ${data.gold}，硬币: ${data.billCoin}")
			}
		} catch (e: ApiFailureException) {
			user.addLog("${str}失败，${e.jsonObject}")
		} catch (e: HttpException) {
			user.addLog("${str}失败，${e.response()?.message()} ${ToolK.getStackTraceInfo(e)}")
		} catch (e: IOException) {
			user.addLog("${str}失败，${ToolK.getStackTraceInfo(e)}")
		}
	}

	suspend fun checkDailyTask(user: User) = withContext(PCController.workerDispatcher) {
		val str = StringBuilder("检查每日任务")
		try {
			RetrofitExtension.buildRetrofit(
				AccountService.BASE_URL,
				AccountService::class.java,
				user.addHeaderInterceptor,
				addSignAndSortInterceptor = false
			).getTaskInfo().data.run {
				str.append("成功：")
				boxInfo?.run {
					str.append("每日银瓜子宝箱")
					if (freeSilverFinish) {
						str.append("已领取完毕")
					} else {
						str.append("未领取完毕，")
							.append("当前为第${times}/${maxTimes}轮，下一个宝箱在${minute}后领取${silver}瓜子")
					}
					str.append("\n")
				}
				doubleWatchInfo?.run {
					str.append("双端观看任务：")
						.append("手机观看%s完成，".format(if (mobileWatch == 1) "已" else "未"))
						.append("网页观看%s完成，".format(if (webWatch == 1) "已" else "未"))
						.append("%s领取双端观看奖励\n".format(if (status == 2) "已" else "未"))

				}
				user.addLog(str.toString())
				doubleWatchInfo?.run {
					if (status != 2 && progress.now == progress.max) {
						getDoubleWatchAward(user)
					}
				}
			}
		} catch (e: ApiFailureException) {
			str.append("失败-${e.jsonObject}")
		} catch (e: ResponseConvertException) {
			str.append("失败-${e.response.message}")
		}
	}

	private suspend fun getDoubleWatchAward(user: User) = withContext(PCController.workerDispatcher) {
		val str = StringBuilder("领取双端观看奖励")
		try {
			RetrofitExtension.buildRetrofit(
				AccountService.BASE_URL,
				AccountService::class.java,
				user.addHeaderInterceptor,
				addSignAndSortInterceptor = false
			).receiveAward().run {
				str.append("成功").toString().run { user.addLog(this) }
			}
		} catch (e: ApiFailureException) {
			str.append("失败-${e.jsonObject}")
		} catch (e: ResponseConvertException) {
			str.append("失败-${e.response.message}")
		}
	}

	suspend fun checkDailySign(user: User) = withContext(PCController.workerDispatcher) {
		val str :StringBuilder= StringBuilder("检查签到信息")
		try {
			RetrofitExtension.buildRetrofit(
				AccountService.BASE_URL,
				AccountService::class.java,
				user.addHeaderInterceptor,
				user.addParamInterceptor
			).getSignInfo().data.run {
				str.append("成功：").also { stringBuilder ->
					stringBuilder.append("今日%s签到，".format(if (isSigned) "已" else "未"))
						.append("今日奖励:")
					awards.forEach {
						stringBuilder.append(" %s x %d".format(it.text, it.count))
					}
				}.run { user.addLog(this.toString()) }
				if (!isSigned) doSign(user)
			}
		} catch (e: ApiFailureException) {
			str.append("失败-${e.jsonObject}")
		} catch (e: ResponseConvertException) {
			str.append("失败-${e.response.message}")
		}
	}

	private suspend fun doSign(user: User) = withContext(PCController.workerDispatcher) {
		val str = StringBuilder("签到")
		try {
			RetrofitExtension.buildRetrofit(
				AccountService.BASE_URL,
				AccountService::class.java,
				user.addHeaderInterceptor,
				user.addParamInterceptor
			).doSign().data.run {
				str.append("成功：").also {
					it.append("获得").append(text)
						.append("本月签到%d/%d天".format(hadSignDays, allDays))
				}.run { user.addLog(this.toString()) }
			}
		} catch (e: ApiFailureException) {
			str.append("失败-${e.jsonObject}")
		} catch (e: ResponseConvertException) {
			str.append("失败-${e.response.message}")
		}
	}
}