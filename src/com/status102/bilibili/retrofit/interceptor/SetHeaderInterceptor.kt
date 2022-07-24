package com.status102.bilibili.retrofit.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class SetHeaderInterceptor(private vararg val additionHeaders: Pair<String, String>) : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		val request = chain.request().newBuilder().apply {
			additionHeaders.forEach {
				header(it.first, it.second)
			}
		}.build()
		return chain.proceed(request)
	}
}