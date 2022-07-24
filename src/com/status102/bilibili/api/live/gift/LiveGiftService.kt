package com.status102.bilibili.api.live.gift

import com.status102.bilibili.api.live.gift.entity.DailyBag
import com.status102.bilibili.api.live.gift.entity.DailyBagStatus
import retrofit2.http.GET

interface LiveGiftService {
	companion object {
		const val BASE_URL = "https://api.live.bilibili.com"
	}

	@GET("/gift/v2/live/m_daily_bag_status")
	suspend fun getDailyBagStatus(): DailyBagStatus

	@GET("/gift/v2/live/m_receive_daily_bag")
	suspend fun getDailyBag(): DailyBag
}