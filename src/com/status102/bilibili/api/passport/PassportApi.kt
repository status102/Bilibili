package com.status102.bilibili.api.passport

import com.status102.bilibili.BilibiliProperty
import com.status102.bilibili.OkHttpObject
import com.status102.bilibili.account.AccountK
import com.status102.bilibili.account.User
import com.status102.bilibili.api.main.account.AccountApi
import com.status102.bilibili.api.passport.entity.LoginResponse
import com.status102.bilibili.retrofit.RetrofitExtension
import com.status102.bilibili.retrofit.exception.ApiFailureException
import com.status102.bilibili.retrofit.exception.ResponseConvertException
import com.status102.bilibili.retrofit.interceptor.AddHeaderInterceptor
import com.status102.bilibili.retrofit.interceptor.AddParamInterceptor
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import java.time.Instant
import java.util.*
import javax.crypto.Cipher

object PassportApi {
	/**
	 * 登陆
	 * v3 登陆接口会同时返回 cookies 和 token
	 * 如果要求验证码, 访问 data 中提供的 url 将打开一个弹窗, 里面会加载 js 并显示极验
	 * 极验会调用 https://api.geetest.com/ajax.php 上传滑动轨迹, 然后获得 validate 的值
	 * secCode 的值为 "$validate|jordan"
	 *
	 * @throws HttpException HTTP返回错误时抛出(非200 OK)，可以拿到code、message，但content会被自动拼接上code和message(非法访问403 forbidden)
	 * @throws ResponseConvertException 返回数据转换出错
	 * @throws ApiFailureException 用户名与密码不匹配(-629)或者需要验证码(极验)(-105)
	 * @throws IllegalStateException 账号或密码为空
	 */
	suspend fun login(
		user: User, loginName: String, password: String,
		//如果登陆请求返回了 "验证码错误!"(-105) 的结果, 那么下一次发送登陆请求就需要带上验证码
		challenge: String? = null, secCode: String? = null, validate: String? = null
	): LoginResponse {
		if (loginName.isEmpty() || password.isEmpty()) {
			throw IllegalStateException(
				if (loginName.isEmpty()) {
					"账号"
				} else {
					"密码"
				} + "为空"
			)
		}
		if (user.account.device.hardwareId.isEmpty()) {
			user.account.device.hardwareId = AccountK.Device.randomHardwareID()
		}
		if (user.account.device.localId.isNullOrEmpty()) {
			AccountApi.refreshBiliLocalId(user)
		}
		val passportApi = RetrofitExtension.buildRetrofit(
			PassportService.BASE_URL,
			PassportService::class.java,
			user.addHeaderInterceptor,
			user.addParamInterceptor
		)

		//取得 hash 和 RSA 公钥
		val (hash, key) = passportApi.getKey().data.let { data ->
			data.hash to data.key.split('\n').filterNot { it.startsWith('-') }.joinToString(separator = "")
		}
		//解析 RSA 公钥
		val publicKey = X509EncodedKeySpec(Base64.getDecoder().decode(key)).let {
			KeyFactory.getInstance("RSA").generatePublic(it)
		}
		//加密密码
		//兼容 Android
		val cipheredPassword = Cipher.getInstance("RSA/ECB/PKCS1Padding").apply {
			init(Cipher.ENCRYPT_MODE, publicKey)
		}.doFinal((hash + password).toByteArray()).let {
			Base64.getEncoder().encode(it)
		}.let {
			String(it)
		}

		return Retrofit.Builder()
			.baseUrl(PassportService.BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.client(OkHttpClient.Builder().apply {
				addInterceptor(
					AddHeaderInterceptor(
						BilibiliProperty.Header.DISPLAY_ID to { BilibiliProperty.buildVersionId + "-" + Instant.now().epochSecond.toString() },
						BilibiliProperty.Header.BUILD_VERSION_ID to { BilibiliProperty.buildVersionId },
						BilibiliProperty.Header.DEVICE_ID to { user.account.device.hardwareId },
						BilibiliProperty.Header.USER_AGENT to { user.account.device.userAgent })
				)
				addInterceptor(
					AddParamInterceptor(
						BilibiliProperty.Param.APP_KEY to { BilibiliProperty.appKey },
						//BilibiliProperty.Param.BILI_LOCAL_ID to { user.account.device.localId },
						BilibiliProperty.Param.BUILD to { BilibiliProperty.build },
						BilibiliProperty.Param.BUILD_VERSION_ID to { BilibiliProperty.buildVersionId },
						BilibiliProperty.Param.DEVICE to { "phone" },
						//BilibiliProperty.Param.DEVICE_ID to { user.account.device.localId },
						//BilibiliProperty.Param.DEVICE_NAME to { "${user.account.device.manufacturer}${user.account.device.model}" },
						//BilibiliProperty.Param.DEVICE_PLATFORM to { "Android${user.account.device.androidVersion}${user.account.device.manufacturer}${user.account.device.model}" },
						BilibiliProperty.Param.LOCAL_ID to { BilibiliProperty.buildVersionId },
						BilibiliProperty.Param.MOBILE_APP to { BilibiliProperty.platform },
						BilibiliProperty.Param.PLATFORM to { BilibiliProperty.platform },
						BilibiliProperty.Param.TIMESTAMP to { Instant.now().epochSecond.toString() }
						// TODO 完善登录包手机信息
					))
				addInterceptor(OkHttpObject.sortAndSignInterceptor)
				addInterceptor(OkHttpObject.failureRequestInterceptor)
				dispatcher(OkHttpObject.dispatcher)
				connectionPool(OkHttpObject.connectionPool)
			}.build())
			.build()
			.create(PassportService::class.java).login(loginName, cipheredPassword, challenge, secCode, validate)
	}
}