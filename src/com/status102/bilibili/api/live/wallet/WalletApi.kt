package com.status102.bilibili.api.live.wallet

import com.status102.bilibili.PCController
import com.status102.bilibili.account.User
import com.status102.bilibili.retrofit.RetrofitExtension
import com.status102.bilibili.retrofit.exception.ApiFailureException
import com.status102.bilibili.retrofit.exception.ResponseConvertException
import com.status102.bilibili.tool.Tool
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

object WalletApi {
	suspend fun silver2Coin(user: User) = withContext(PCController.workerDispatcher) {
		val str = StringBuilder("每日银瓜子换硬币")
		try {
			RetrofitExtension.buildRetrofit(
				WalletService.BASE_URL,
				WalletService::class.java,
				user.addHeaderInterceptor,
				user.addParamInterceptor
			).silver2coin().data.run {
				str.append("成功：")
					.append("兑换硬币${coin}个，剩余${silver}银瓜子，${gold}金瓜子")
				user.addLog(str.toString())
			}
		} catch (e: ApiFailureException) {
			e.jsonObject.run {
				user.addLog("${str}失败${toString()}")
				user.println("${str}失败${toString()}")
			}
		} catch (e: ResponseConvertException) {
			user.addLog("${str}失败${e.response.message}")
			user.println("${str}失败${e.response.message}")
		} catch (e: HttpException) {
			user.addLog("${str}失败${Tool.getStackTraceInfo(e)}")
			user.println("${str}失败${Tool.getStackTraceInfo(e)}")
		} catch (e: IOException) {
			user.addLog("${str}失败${Tool.getStackTraceInfo(e)}")
			user.println("${str}失败${Tool.getStackTraceInfo(e)}")
		}
	}
}