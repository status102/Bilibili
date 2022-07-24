package com.status102.bilibili.api.live.lottery

import com.google.gson.JsonObject
import com.status102.bilibili.api.live.lottery.entity.*
import com.status102.bilibili.api.live.lottery.entity.silverbox.SilverBoxAward
import com.status102.bilibili.api.live.lottery.entity.silverbox.SilverBoxTask
import retrofit2.http.*

interface LiveLotteryService {
	companion object {
		const val BASE_URL = "https://api.live.bilibili.com"
	}

	@GET("/xlive/lottery-interface/v1/lottery/getLotteryInfo")
	suspend fun getLotteryInfo(@Query("roomid") roomId: Long): LotteryInfo

	interface SilverBox {
		/**
		 * 领取银瓜子宝箱，两个时间都是unix
		 */
		@GET("/lottery/v1/SilverBox/getAward")
		suspend fun getAward(@Query("time_start") timeStart: Long, @Query("time_end") timeEnd: Long): SilverBoxAward

		@GET("/lottery/v1/SilverBox/getCurrentTask")
		suspend fun getCurrentTask(): SilverBoxTask
	}

	interface Raffle {
		@POST("/xlive/lottery-interface/v4/smalltv/Getaward")
		@FormUrlEncoded
		suspend fun getAward(@Field("roomid") roomId: Long, @Field("raffleId") raffleId: Long, @Field("type") type: String): RaffleAward
	}

	interface Guard {
		@POST("/lottery/v2/Lottery/join")
		@FormUrlEncoded
		suspend fun getAward(@Field("roomid") roomId: Long, @Field("id") raffleId: Long, @Field("type") type: String): GuardAward
	}

	interface ActivityBox {
		@GET("/lottery/v2/Box/getStatus")
		suspend fun getStatus(@Query("aid") aid: Long): BoxStatus

		@GET("/lottery/v1/Box/draw")
		suspend fun draw(@Query("aid") aid: Long, @Query("number") round: Int): DrawActivityBox

		@GET("/lottery/v2/Box/getWinnerGroupInfo")
		suspend fun getWinnerGroup(@Query("aid") aid: Long, @Query("number") round: Int): WinnerGroup
	}

	interface PK {
		@POST("/xlive/lottery-interface/v1/pk/join")
		@FormUrlEncoded
		suspend fun join(@Field("roomid") roomId: Long, @Field("id") pkID: Long): PkAward
	}
}