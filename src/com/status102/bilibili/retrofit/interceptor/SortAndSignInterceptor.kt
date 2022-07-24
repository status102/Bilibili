package com.status102.bilibili.retrofit.interceptor

import com.status102.bilibili.BilibiliProperty
import com.status102.bilibili.retrofit.containsEncodedName
import com.status102.bilibili.retrofit.sortedRaw
import com.status102.bilibili.tool.Tool
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Response

class SortAndSignInterceptor : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		var request = chain.request()
		val url = request.url
		val body = request.body

		request = when {
			//判断 appKey 是否在 Query 里
			url.queryParameter(BilibiliProperty.Param.APP_KEY) != null -> {
				val sortedEncodedQuery = url.encodedQuery!!.split('&').sorted().joinToString(separator = "&")
				request.newBuilder()
					.url(
						url.newBuilder()
							.encodedQuery(
								"$sortedEncodedQuery&${BilibiliProperty.Param.SIGN}=${Tool.getStringMD5(
									sortedEncodedQuery + BilibiliProperty.appSecret
								)}"
							)
							.build()
					).build()
			}

			//在 FormBody 里
			body is FormBody && body.containsEncodedName(BilibiliProperty.Param.APP_KEY) -> {
				val sortedRaw = body.sortedRaw()
				val formBody = FormBody.Builder().apply {
					sortedRaw.split('&').forEach {
						val (name, value) = it.split('=')
						addEncoded(name, value)
					}
					addEncoded(BilibiliProperty.Param.SIGN, Tool.getStringMD5(sortedRaw + BilibiliProperty.appSecret))
				}.build()
				request.newBuilder()
					.method(request.method, formBody)
					.build()
			}

			//不存在 accessKey
			else -> {
				request
			}
		}

		return chain.proceed(request)
	}
}