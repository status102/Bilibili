package com.status102.bilibili.api.live.area

import com.status102.bilibili.BilibiliProperty
import com.status102.bilibili.PCController
import com.status102.bilibili.retrofit.RetrofitExtension
import com.status102.bilibili.retrofit.exception.ApiFailureException
import com.status102.bilibili.retrofit.exception.ResponseConvertException
import com.status102.bilibili.retrofit.interceptor.AddHeaderInterceptor
import com.status102.bilibili.retrofit.interceptor.AddParamInterceptor
import com.status102.bilibili.tool.Tool
import kotlinx.coroutines.withContext
import java.io.IOException
import java.time.Instant

object LiveAreaApi {
	suspend fun getAreaIDMap(): MutableMap<Int, String>? {
		val areaIDMap = mutableMapOf<Int, String>()
		try {
			RetrofitExtension.buildRetrofit(
				LiveAreaService.BASE_URL, LiveAreaService::class.java, AddHeaderInterceptor(
					BilibiliProperty.Header.DISPLAY_ID to { BilibiliProperty.buildVersionId + "-" + Instant.now().epochSecond.toString() },
					BilibiliProperty.Header.BUILD_VERSION_ID to { BilibiliProperty.buildVersionId },
					BilibiliProperty.Header.DEVICE_ID to { BilibiliProperty.hardwareId },
					BilibiliProperty.Header.USER_AGENT to { BilibiliProperty.defaultUserAgent }),
				AddParamInterceptor(
					BilibiliProperty.Param.APP_KEY to { BilibiliProperty.appKey },
					BilibiliProperty.Param.BUILD to { BilibiliProperty.build },
					BilibiliProperty.Param.MOBILE_APP to { BilibiliProperty.platform },
					BilibiliProperty.Param.PLATFORM to { BilibiliProperty.platform },
					BilibiliProperty.Param.TIMESTAMP to { Instant.now().epochSecond.toString() }
				))
				.getAreaList(1, 0, 2).data?.forEach {
					areaIDMap[it.id] = it.name
				}
		} catch (e: ResponseConvertException) {
			PCController.printlnMsg(e.response.message)
		} catch (e: ApiFailureException) {
			PCController.printlnMsg(Tool.getStackTraceInfo(e))
		} catch (e: IOException) {
			e.printStackTrace()
		}
		return areaIDMap
	}

	suspend fun getRoomListOfArea(areaId: Int, page: Int) = withContext(PCController.workerDispatcher){
			RetrofitExtension.buildRetrofit(
				LiveAreaService.BASE_URL, LiveAreaService::class.java, AddHeaderInterceptor(
					BilibiliProperty.Header.DISPLAY_ID to { BilibiliProperty.buildVersionId + "-" + Instant.now().epochSecond.toString() },
					BilibiliProperty.Header.BUILD_VERSION_ID to { BilibiliProperty.buildVersionId },
					BilibiliProperty.Header.DEVICE_ID to { BilibiliProperty.hardwareId },
					BilibiliProperty.Header.USER_AGENT to { BilibiliProperty.defaultUserAgent }),
				AddParamInterceptor(
					BilibiliProperty.Param.APP_KEY to { BilibiliProperty.appKey },
					BilibiliProperty.Param.BUILD to { BilibiliProperty.build },
					BilibiliProperty.Param.MOBILE_APP to { BilibiliProperty.platform },
					BilibiliProperty.Param.PLATFORM to { BilibiliProperty.platform },
					BilibiliProperty.Param.TIMESTAMP to { Instant.now().epochSecond.toString() }
				))
				.getRoomList(areaId, page)
	}
}