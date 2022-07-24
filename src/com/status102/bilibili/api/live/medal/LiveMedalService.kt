package com.status102.bilibili.api.live.medal

import com.google.gson.JsonObject
import com.status102.bilibili.api.live.medal.entity.MedalList
import retrofit2.http.GET
import retrofit2.http.Query

interface LiveMedalService {
	companion object {
		const val BASE_URL = "https://api.live.bilibili.com"
	}


	@GET("/fans_medal/v2/HighQps/received_medals")
	suspend fun getMedalList(@Query("uid") uid: Long): MedalList

	@GET("/i/api/medal")
	suspend fun getMedalListOld(@Query("page") page: Int = 1, @Query("pageSize") pageSize: Int = 25): JsonObject

}