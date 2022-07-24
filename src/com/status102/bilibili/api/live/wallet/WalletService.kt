package com.status102.bilibili.api.live.wallet

import com.status102.bilibili.api.live.wallet.entity.Silver2Coin
import retrofit2.http.GET
import retrofit2.http.Query

interface WalletService {
	companion object {
		const val BASE_URL = "https://api.live.bilibili.com"
	}

	@GET("/pay/v1/Exchange/silver2coin")
	suspend fun silver2coin(@Query("num") num: Int = 1): Silver2Coin
}