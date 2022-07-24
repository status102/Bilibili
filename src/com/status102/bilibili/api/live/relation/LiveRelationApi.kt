package com.status102.bilibili.api.live.relation

import com.status102.bilibili.PCController
import com.status102.bilibili.account.User
import com.status102.bilibili.retrofit.RetrofitExtension
import kotlinx.coroutines.withContext

object LiveRelationApi {
	suspend fun getSubscribeLiveRoomList(user: User)= withContext(PCController.workerDispatcher){
		RetrofitExtension.buildRetrofit(
			LiveRelationService.BASE_URL,
			LiveRelationService::class.java,
			user.addHeaderInterceptor,
			user.addParamInterceptor
		).getSubscribeLiveRoomList()
	}
}