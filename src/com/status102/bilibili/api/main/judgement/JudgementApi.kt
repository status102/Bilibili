package com.status102.bilibili.api.main.judgement

import com.status102.bilibili.account.User
import com.status102.bilibili.retrofit.RetrofitExtension
import com.status102.bilibili.retrofit.exception.ApiFailureException
import com.status102.utils.ToolK
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

//TODO("添加推送")
object JudgementApi {
	fun dailyVote(user: User) {
		GlobalScope.launch {
			val api = RetrofitExtension.buildRetrofit(
				JudgementService.BASE_URL,
				JudgementService::class.java,
				user.addHeaderInterceptor,
				addSignAndSortInterceptor = false
			)
			val csrf = user.account.csrf
			if (csrf.isBlank()) {
				user.addLog("风纪投票-获取csrf失败")
				user.println("风纪投票-获取csrf失败")
				return@launch
			}
			try {
				api.getMyInfo().run {
					val str = "连接风纪成功，您已审理${data.caseTotal}次，剩余${data.restDays}天权限(${data.rightRadio})"
					user.addLog(str)
					user.println(str)
				}
			} catch (e: ApiFailureException) {
				when (e.jsonObject["code"].asInt) {
					25005 -> {// 请成为风纪委员后再试
						user.account.setting.isJudgement = false
						val str = "获取风纪委员信息失败-${e.jsonObject["message"].asString}"
						user.println(str)
						user.addLog(str)
						return@launch
					}
					-101 -> {// {"code":-101,"message":"账号未登录","ttl":1}
						val str = "获取风纪委员信息失败-${e.jsonObject["message"].asString}"
						user.println(str)
						user.addLog(str)
						user.login((5..60).random() * 1000L)
						return@launch
					}
					else -> {
						user.account.setting.isJudgement = false
						val str = "获取风纪委员信息失败(1-未知code)-${e.jsonObject}"
						user.println(str)
						user.addLog(str)
						return@launch
					}
				}
			} catch (e: HttpException) {
				user.addLog("风纪投票错误1-" + e.message())
				user.println("风纪投票错误1-" + e.message())
				return@launch
			} catch (e: IOException) {
				user.addLog("风纪投票错误1-" + ToolK.getStackTraceInfo(e))
				user.println("风纪投票错误1-" + ToolK.getStackTraceInfo(e))
				return@launch
			}

			var counts = 0
			var cid: Long = -1L
			var voteBan = 0
			var voteDelete = 0
			var voteNegative = 0
			var voteType = 2

			while (true) {
				delay((2000L..8000L).random())
				try {
					api.getNewCase(csrf).run {
						cid = data.id
						user.addLog("风纪投票获取新案件成功，案件id: $cid")
					}
				} catch (e: ApiFailureException) {
					when (e.jsonObject["code"].asInt) {
						25014 -> {// 今日案件审理完毕
							val str = "今天的案件审理完成，本次处理${counts}件风纪投票"
							user.println(str)
							user.addLog(str)
							/*if (counts != 0 && user.getAccount().getSetting().isSendAfterJudgement()) {
								user.sendToServer("风纪投票推送", str, false)
							}*/
							return@launch
						}
						25008 -> {// 暂无案件可处理，处理数未达当日上限
							val str = "${e.jsonObject["message"].asString}(暂无案件可处理，处理数未达当日上限)，本次处理${counts}件风纪投票"
							user.println(str)
							user.addLog(str)
							/*if (user.getAccount().getSetting().isSendAfterJudgement()) {
								user.sendToServer("风纪投票推送", str, false)
							}*/
							return@launch
						}
						25006 -> {// 风纪权限过期
							val str = e.jsonObject.get("message").getAsString()
							user.println(str)
							user.addLog(str)
							/*if (user.getAccount().getSetting().isSendAfterJudgement()) {
								user.sendToServer("风纪投票推送", str, false)
							}*/
							return@launch
						}
						-111 -> {// csrf校验失败
							val str = "${e.jsonObject["message"].asString}，本次处理${counts}件风纪投票"
							user.println(str)
							user.addLog(str)
							/*if (user.getAccount().getSetting().isSendAfterJudgement()) {
								user.sendToServer("风纪投票推送", str, false)
							}*/
							//user.refreshToken() //todo 刷新token以获取cookie
							return@launch
						}
						else -> {
							user.account.setting.isJudgement = false
							val str = "获取风纪委员信息失败(2-未知code)-${e.jsonObject.get("message").getAsString()}"
							user.println(str)
							user.addLog(str)
							return@launch
						}
					}
				} catch (e: HttpException) {
					user.addLog("风纪投票错误2-" + e.message())
					user.println("风纪投票错误2-" + e.message())
					return@launch
				} catch (e: IOException) {
					user.addLog("风纪投票错误2-" + ToolK.getStackTraceInfo(e))
					user.println("风纪投票错误2-" + ToolK.getStackTraceInfo(e))
					return@launch
				}

				delay((10L..800L).random())
				try {
					api.getCaseInfo(cid, csrf).data.run {
						val str = "风纪委员获取到新案件信息: ${uname}涉嫌${punishTitle}：${originContent}"
						user.println(str)
						user.addLog(str)
						voteBan = this.voteBreak
						voteDelete = this.voteDelete
						voteNegative = this.voteRule

						if (voteBan == 0 && voteDelete == 0 && voteNegative == 0) {
							var page = 1
							var c = true
							while (c) {
								try {
									api.getOpinion(cid, page).data.run {
										if (opinion == null || opinion?.isEmpty() != false) {
											c = false
										} else {
											opinion?.forEach {
												when (it.vote) {
													1 -> voteBan += 1
													2 -> voteNegative += 1
													4 -> voteDelete += 1
													else -> voteNegative += 1
												}
											}
										}
									}
									delay((2..8).random() * 1000L)
								} catch (e: ApiFailureException) {
									user.addLog("风纪投票错误-31-" + e.jsonObject)
									user.println("风纪投票错误-31-" + e.jsonObject)
									break
								} catch (e: HttpException) {
									user.addLog("风纪投票错误-31-" + e.message())
									user.println("风纪投票错误-31-" + e.message())
									break
								} catch (e: IOException) {
									user.addLog("风纪投票错误-31-" + ToolK.getStackTraceInfo(e))
									user.println("风纪投票错误-31-" + ToolK.getStackTraceInfo(e))
									break
								}
								page += 1
							}
						}
					}
				} catch (e: ApiFailureException) {
					val str = "风纪委员获取案件详细信息失败(3-未知code)-${e.jsonObject}"
					user.addLog(str)
					user.println(str)
					return@launch
				} catch (e: HttpException) {
					user.addLog("风纪投票错误3-" + e.message())
					user.println("风纪投票错误3-" + e.message())
					return@launch
				} catch (e: IOException) {
					user.addLog("风纪投票错误3-" + ToolK.getStackTraceInfo(e))
					user.println("风纪投票错误3-" + ToolK.getStackTraceInfo(e))
					return@launch
				}
				delay((3..18).random() * 1000L)
				if(voteBan >= voteDelete && voteBan >= voteNegative){
					voteType = 1
				}else if(voteNegative >= voteBan && voteNegative >= voteDelete){
					voteType = 2
				}else if(voteDelete >= voteBan && voteDelete >= voteNegative){
					voteType = 4
				}

				try {
					api.vote(csrf, cid, voteType)
					val str = "风纪投票云端判定系统选择 ${if (voteType == 1) "封禁" else if (voteType == 2) "不违规" else "删除"}，投票成功"
					user.println(str)
					user.addLog(str)
				}catch (e:ApiFailureException){
					val str = "进行风纪投票失败-4-${e.jsonObject}"
					user.println(str)
					user.addLog(str)
					return@launch
				}catch (e: HttpException) {
					user.addLog("风纪投票错误-4-" + e.message())
					user.println("风纪投票错误-4-" + e.message())
					return@launch
				} catch (e: IOException) {
					user.addLog("风纪投票错误-4-" + ToolK.getStackTraceInfo(e))
					user.println("风纪投票错误-4-" + ToolK.getStackTraceInfo(e))
					return@launch
				}
				counts++
			}
		}
	}
}