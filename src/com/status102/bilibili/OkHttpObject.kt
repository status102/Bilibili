package com.status102.bilibili

import com.status102.bilibili.retrofit.interceptor.FailureRequestInterceptor
import com.status102.bilibili.retrofit.interceptor.SortAndSignInterceptor
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient

object OkHttpObject {
	val dispatcher by lazy {
		Dispatcher().apply {
			maxRequests = Int.MAX_VALUE
			maxRequestsPerHost = Int.MAX_VALUE
		}
	}
	val connectionPool by lazy { ConnectionPool() }
	val defaultClient: OkHttpClient by lazy {
		OkHttpClient.Builder().apply {
			//interceptors.forEach { addInterceptor(it) }
			//addInterceptor(sortAndSignInterceptor)
			//addInterceptor(FailureResponseInterceptor)
			//addNetworkInterceptor(FailureRequestInterceptor())
			dispatcher(dispatcher)
			connectionPool(connectionPool)
		}.build()
	}
	val failureRequestInterceptor by lazy { FailureRequestInterceptor() }
	val sortAndSignInterceptor by lazy { SortAndSignInterceptor() }
}