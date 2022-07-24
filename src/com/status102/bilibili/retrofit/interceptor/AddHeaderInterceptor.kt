package com.status102.bilibili.retrofit.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class AddHeaderInterceptor (private vararg val additionHeaders: Pair<String, ()->String>):Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		val request = chain.request().newBuilder().apply {
			additionHeaders.forEach {
				addHeader(it.first, it.second.invoke())
			}
		}.build()
		return chain.proceed(request)
	}
}