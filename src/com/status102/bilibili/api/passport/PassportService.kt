package com.status102.bilibili.api.passport

import com.status102.bilibili.api.passport.entity.GetKeyResponse
import com.status102.bilibili.api.passport.entity.LoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PassportService {
	companion object {
		const val BASE_URL = "https://passport.bilibili.com"
	}

	@POST("/api/oauth2/getKey")
	suspend fun getKey(): GetKeyResponse

	/**
	 * 多次错误的登陆尝试后, 服务器将返回 {"ts":1550569982,"code":-105,"data":{"url":"https://passport.bilibili.com/register/verification.html?success=1&gt=b6e5b7fad7ecd37f465838689732e788&challenge=9a67afa4d42ede71a93aeaaa54a4b6fe&ct=1&hash=105af2e7cc6ea829c4a95205f2371dc5"},"message":"验证码错误!"}
	 */
	//@Suppress("SpellCheckingInspection")
	@POST("/api/v3/oauth2/login")
	@FormUrlEncoded
	suspend fun login(
		@Field("username") username: String, @Field("password") password: String,
		//以下为极验所需字段
		@Field("challenge") challenge: String? = null,
		@Field("seccode") secCode: String? = null,
		@Field("validate") validate: String? = null
	): LoginResponse
}