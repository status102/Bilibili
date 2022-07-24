package com.status102.bilibili.api.main.account

import com.status102.bilibili.OkHttpObject
import com.status102.bilibili.account.User
import com.status102.bilibili.retrofit.RetrofitExtension
import com.status102.bilibili.retrofit.exception.ApiFailureException
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AccountApi {

	/**
	 * 获取账号当前设备ID对应的device_id
	 */
	suspend fun refreshBiliLocalId(user: User) {
		try {
			RetrofitExtension.buildRetrofit(AccountService.BASE_URL,
				AccountService::class.java,
				user.addHeaderInterceptor,
				user.addParamInterceptor
			).getBiliLocalID().data.run {
				user.account.device.localId = biliDeviceId
			}
		} catch (e: HttpException) {
		} catch (e: ApiFailureException) {
		} catch (e: IllegalStateException) {
		}
		// TODO 完善Exception的处理
	}
}