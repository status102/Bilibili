package com.status102.bilibili.api.live.room

import com.status102.bilibili.BilibiliProperty
import com.status102.bilibili.PCController
import com.status102.bilibili.account.User
import com.status102.bilibili.api.live.room.entity.app.DanmuInfo
import com.status102.bilibili.api.live.room.entity.app.IndexInfo
import com.status102.bilibili.retrofit.RetrofitExtension
import com.status102.bilibili.retrofit.interceptor.AddHeaderInterceptor
import com.status102.bilibili.retrofit.interceptor.AddParamInterceptor
import com.status102.utils.TimeUtils
import kotlinx.coroutines.withContext
import java.time.Instant

object LiveRoomApi {
	suspend fun getInfoByRoom(roomId: Long) = getInfoByRoom(null, roomId)
	suspend fun getInfoByRoom(user: User?, roomId: Long): IndexInfo = if (user == null) {
		RetrofitExtension.buildRetrofit(
			LiveRoomService.BASE_URL, LiveRoomService.APP::class.java,
			AddHeaderInterceptor(
				BilibiliProperty.Header.DISPLAY_ID to { BilibiliProperty.buildVersionId + "-" + Instant.now().epochSecond.toString() },
				BilibiliProperty.Header.BUILD_VERSION_ID to { BilibiliProperty.buildVersionId },
				BilibiliProperty.Header.DEVICE_ID to { BilibiliProperty.hardwareId },
				BilibiliProperty.Header.USER_AGENT to { BilibiliProperty.defaultUserAgent }),
			AddParamInterceptor(
				BilibiliProperty.Param.APP_KEY to { BilibiliProperty.appKey },
				BilibiliProperty.Param.BUILD to { BilibiliProperty.build },
				BilibiliProperty.Param.DEVICE to { BilibiliProperty.platform },
				BilibiliProperty.Param.MOBILE_APP to { BilibiliProperty.platform },
				BilibiliProperty.Param.PLATFORM to { BilibiliProperty.platform },
				BilibiliProperty.Param.TIMESTAMP to { Instant.now().epochSecond.toString() }
			)
		)
	} else {
		RetrofitExtension.buildRetrofit(
			LiveRoomService.BASE_URL, LiveRoomService.APP::class.java,
			user.addHeaderInterceptor, user.addParamInterceptor
		)
	}.run { this.getInfoByRoom(roomId) }

	suspend fun getDanmuInfo(roomId: Long) = getDanmuInfo(null, roomId)
	suspend fun getDanmuInfo(user: User?, roomId: Long): DanmuInfo = if (user == null) {
		RetrofitExtension.buildRetrofit(
			LiveRoomService.BASE_URL, LiveRoomService.APP::class.java,
			AddHeaderInterceptor(
				BilibiliProperty.Header.DISPLAY_ID to { BilibiliProperty.buildVersionId + "-" + Instant.now().epochSecond.toString() },
				BilibiliProperty.Header.BUILD_VERSION_ID to { BilibiliProperty.buildVersionId },
				BilibiliProperty.Header.DEVICE_ID to { BilibiliProperty.hardwareId },
				BilibiliProperty.Header.USER_AGENT to { BilibiliProperty.defaultUserAgent }),
			AddParamInterceptor(
				BilibiliProperty.Param.APP_KEY to { BilibiliProperty.appKey },
				BilibiliProperty.Param.BUILD to { BilibiliProperty.build },
				BilibiliProperty.Param.DEVICE to { BilibiliProperty.platform },
				BilibiliProperty.Param.MOBILE_APP to { BilibiliProperty.platform },
				BilibiliProperty.Param.PLATFORM to { BilibiliProperty.platform },
				BilibiliProperty.Param.TIMESTAMP to { Instant.now().epochSecond.toString() }
			)
		)
	} else {
		RetrofitExtension.buildRetrofit(
			LiveRoomService.BASE_URL,
			LiveRoomService.APP::class.java,
			user.addHeaderInterceptor,
			user.addParamInterceptor
		)
	}.run { this.getDanmuInfo(roomId) }

	object APP {
		/**
		 * APP端进房
		 */
		suspend fun enterRoom(user: User, roomId: Long) = withContext(PCController.workerDispatcher) {
			RetrofitExtension.buildRetrofit(
				LiveRoomService.BASE_URL,
				LiveRoomService.APP::class.java,
				user.addHeaderInterceptor,
				user.addParamInterceptor
			).enterRoom(roomId)
		}

		suspend fun heartBeat(user: User, roomId: Long) = withContext(PCController.workerDispatcher) {
			val param = mutableMapOf<String, String>()
				.apply {
					this["access_key"] = user.account.accessToken ?: ""
					this["room_id"] = roomId.toString()
					this["client_ts"] = TimeUtils.unixTime.toString()
					this[BilibiliProperty.Param.APP_KEY] = BilibiliProperty.appKey
					this[BilibiliProperty.Param.BUILD] = BilibiliProperty.build
					this[BilibiliProperty.Param.MOBILE_APP] = BilibiliProperty.platform
					this[BilibiliProperty.Param.PLATFORM] = BilibiliProperty.platform
					this[BilibiliProperty.Param.TIMESTAMP] = Instant.now().epochSecond.toString()
				}.toSortedMap()
			RetrofitExtension.buildRetrofit(
				LiveRoomService.BASE_URL,
				LiveRoomService.APP::class.java,
				user.addHeaderInterceptor,
				addSignAndSortInterceptor = false
			).heartBeat(param, roomId)
		}
	}

	object Web {

	}
}