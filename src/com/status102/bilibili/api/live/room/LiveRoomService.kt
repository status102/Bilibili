package com.status102.bilibili.api.live.room

import com.google.gson.JsonObject
import com.status102.bilibili.api.live.room.entity.app.DanmuInfo
import com.status102.bilibili.api.live.room.entity.app.IndexInfo
import com.status102.utils.TimeUtils
import retrofit2.http.*

interface LiveRoomService {
	companion object {
		const val BASE_URL = "https://api.live.bilibili.com"
	}

	interface APP {
		/**
		 * 获取直播间初始化信息
		 */
		@GET("/xlive/app-room/v1/index/getInfoByRoom")
		suspend fun getInfoByRoom(@Query("room_id") roomId: Long): IndexInfo

		/**
		 * 获取直播间弹幕服务器表
		 */
		@GET("/xlive/app-room/v1/index/getDanmuInfo")
		suspend fun getDanmuInfo(@Query("room_id") roomId: Long): DanmuInfo

		/**
		 * 作用未知，但是在带纪录的进房包之前，不要带BaseURL
		 * @return {"code":0,"message":"0","ttl":1,"data":{"timestamp":1588306268,"heartbeat_interval":300,"secret_key":"axoaadsffcazxksectbbb","secret_rule":[3,7,2,6,8],"patch_status":2}}
		 */
		@POST("https://live-trace.bilibili.com/xlive/data-interface/v1/heartbeat/mobileEntry")
		@FormUrlEncoded
		suspend fun mobileEntry(
			@Field("room_id") roomId: Long,
			@Field("client_ts") clientTs: Long = TimeUtils.unixTime
		): JsonObject

		/**
		 * 作用未知，但是在带纪录的进房包之前，不要带BaseURL
		 * @return {"code":0,"message":"0","ttl":1,"data":{"timestamp":1588306268,"heartbeat_interval":300,"secret_key":"axoaadsffcazxksectbbb","secret_rule":[3,7,2,6,8],"patch_status":2}}
		 */
		@POST("https://live-trace.bilibili.com/xlive/data-interface/v1/heartbeat/mobileHeartBeat")
		@FormUrlEncoded
		suspend fun mobileHeartBeat(
			@Field("room_id") roomId: Long,
			@Field("client_ts") clientTs: Long = TimeUtils.unixTime
		): JsonObject

		/**
		 * APP进房包
		 *
		 * @return {"code":0,"message":"0","ttl":1,"data":null}
		 */
		@POST("/room/v1/Room/room_entry_action")
		@FormUrlEncoded
		suspend fun enterRoom(
			@Field("room_id") roomId: Long,
			@Field("jumpFrom") jumpFrom: Int = 30000,
			@Field("noHistory") noHistory: Int = 0
		): JsonObject

		/**
		 * APP心跳包（300s间隔），POST方法url带accessToken、ts、sign等参数，不要使用默认addParam拦截器
		 *
		 * @return {"code":0,"msg":"OK","message":"OK","data":{"giftlist":[]}}
		 * {"code":3,"msg":"user no login","message":"user no login","data":{"giftlist":[]}}
		 */
		@POST("/heartbeat/v1/OnLine/mobileOnline")
		@FormUrlEncoded
		suspend fun heartBeat(
			@QueryMap queryMap: Map<String, String>,
			@Field("room_id") roomId: Long,
			@Field("scale") scale: String = "xxhdpi"
		): JsonObject
	}

	interface Web {
		/**
		 * WEB进房包，不带sign和addParam
		 *
		 * @return {"code":0,"msg":"ok","message":"ok","data":[]}
		 */
		@POST("/room/v1/Room/room_entry_action")
		@FormUrlEncoded
		suspend fun enterRoom(
			@Field("room_id") roomId: Long,
			@Field("csrf") csrf: String,
			@Field("csrf_token") csrfToken: String,
			@Field("platform") platform: String = "pc",
			@Field("visit_id") visitId: String = "",
			@Header("Accept") accept: String = "application/json, text/plain, */*",
			@Header("Content-Type") contentType: String = "application/x-www-form-urlencoded",
			@Header("Origin") origin: String = "https://live.bilibili.com",
			@Header("Referer") referer: String = "https://live.bilibili.com/$roomId"
		): JsonObject

		/**
		 * WEB心跳包，不带sign和addParam
		 *
		 * @return {"code":0,"msg":"OK","message":"OK","data":{"giftlist":[]}}
		 * {"code":3,"msg":"user no login","message":"user no login","data":{"giftlist":[]}}
		 */
		@POST("/User/userOnlineHeart")
		@FormUrlEncoded
		suspend fun heartBeat(
			@Field("csrf") csrf: String,
			@Field("csrf_token") csrfToken: String,
			@Field("visit_id") visitId: String = "",
			@Header("Accept") accept: String = "application/json, text/plain, */*",
			@Header("Content-Type") contentType: String = "application/x-www-form-urlencoded",
			@Header("Origin") origin: String = "https://live.bilibili.com"
		): JsonObject
	}
}