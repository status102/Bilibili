package com.status102.bilibili.api.live.medal

import com.status102.bilibili.PCController
import com.status102.bilibili.account.User
import com.status102.bilibili.api.live.medal.entity.MedalList
import com.status102.bilibili.retrofit.RetrofitExtension
import com.status102.bilibili.retrofit.exception.ApiFailureException
import com.status102.bilibili.retrofit.exception.ResponseConvertException
import com.status102.bilibili.tool.Tool
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

object LiveMedalApi {
	suspend fun getMedalList(user: User) = withContext(PCController.workerDispatcher) {
		RetrofitExtension.buildRetrofit(
			LiveMedalService.BASE_URL,
			LiveMedalService::class.java,
			user.addHeaderInterceptor,
			user.addParamInterceptor
		).getMedalList(user.account.uid)
	}

	suspend fun getWearingMedal(user: User): MedalList.Data.Medal? = getMedalList(user).data.list.firstOrNull { it.status == 1 }

	/**
	 * 默认的每天喂满佩戴的牌子
	 */
	suspend fun feedWearingMedal(user: User) {
		var str: StringBuilder = StringBuilder("投喂佩戴勋章-获取佩戴中的勋章")
		var medal: MedalList.Data.Medal? = null
		try {
			medal = getWearingMedal(user)?.also {
				str.append("成功：正在佩戴勋章-${it.medalName}[${it.targetName}---UID:${it.targetId}---RoomId:${it.roomId}]")
					.append(" Lv.${it.medalLevel}(${it.intimacy}/${it.nextIntimacy})，今日亲密度${it.todayIntimacy}/${it.dayLimit}，")
					.append("还需投喂${it.dayLimit - it.todayIntimacy}")
					.run { user.addLog(this.toString()) }
			}
			if (medal == null) {
				user.addLog("${str}成功：未佩戴勋章")
				return
			}
		} catch (e: ApiFailureException) {
			e.jsonObject.run {
				user.addLog("${str}失败${toString()}")
				user.println("${str}失败${toString()}")
			}
		} catch (e: ResponseConvertException) {
			user.addLog("${str}失败${e.response.message}")
			user.println("${str}失败${e.response.message}")
		} catch (e: HttpException) {
			user.addLog("${str}失败${Tool.getStackTraceInfo(e)}")
			user.println("${str}失败${Tool.getStackTraceInfo(e)}")
		} catch (e: IOException) {
			user.addLog("${str}失败${Tool.getStackTraceInfo(e)}")
			user.println("${str}失败${Tool.getStackTraceInfo(e)}")
		}
	}
}