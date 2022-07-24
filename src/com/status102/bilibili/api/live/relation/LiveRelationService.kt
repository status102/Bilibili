package com.status102.bilibili.api.live.relation

import com.status102.bilibili.api.live.relation.entity.SubscribeLiveRoom
import retrofit2.http.GET

interface LiveRelationService {
	companion object {
		const val BASE_URL = "https://api.live.bilibili.com"
	}

	@GET("/relation/v1/BplusApp/dynamicRoomListV2")
	suspend fun getSubscribeLiveRoomList(): SubscribeLiveRoom
}