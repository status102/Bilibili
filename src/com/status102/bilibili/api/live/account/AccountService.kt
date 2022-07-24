package com.status102.bilibili.api.live.account

import com.google.gson.JsonObject
import com.status102.bilibili.api.live.account.entity.*
import retrofit2.http.GET
import retrofit2.http.Query

interface AccountService {
	companion object {
		const val BASE_URL = "https://api.live.bilibili.com"
	}

	@GET("/xlive/web-ucenter/user/get_user_info")
	suspend fun getMyInfo(): MyInfo

	@GET("/rc/v2/Sign/getSignInfo")
	suspend fun getSignInfo(): SignInfo

	@GET("/rc/v1/Sign/doSign")
	suspend fun doSign(): DoSign

	@GET("/i/api/taskInfo")
	suspend fun getTaskInfo(): TaskInfo

	@GET("/activity/v1/task/receive_award")
	suspend fun receiveAward(@Query("task_id") taskId: String = "double_watch_task"): ReceiveAward
}