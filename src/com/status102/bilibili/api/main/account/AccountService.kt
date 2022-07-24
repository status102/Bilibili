package com.status102.bilibili.api.main.account

import com.status102.bilibili.api.main.account.entity.FingerPrint
import retrofit2.http.POST

interface AccountService {
	companion object {
		const val BASE_URL = "https://app.bilibili.com"
	}

	@POST("/x/resource/fingerprint")
	suspend fun getBiliLocalID(): FingerPrint
}