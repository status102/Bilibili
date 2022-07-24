package com.status102.bilibili.retrofit.interceptor

import com.status102.bilibili.BilibiliProperty
import com.status102.bilibili.retrofit.addAllEncoded
import com.status102.bilibili.retrofit.forEachNonNull
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Response

class AddParamInterceptor(private vararg val additionParams: Pair<String, () -> String>) : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		val request = chain.request()
		var headers = request.headers
		var httpUrl = request.url
		var body = request.body

		//是否强制加到 Query(暂不存在强制加到 FormBody 的情况)
		var forceQuery = false
		val forceParam = headers[BilibiliProperty.Header.FORCE_PARAM]
		if (forceParam != null) {
			if (forceParam == BilibiliProperty.Header.FORCE_PARAM_QUERY) forceQuery = true
			headers = headers.newBuilder().removeAll(BilibiliProperty.Header.FORCE_PARAM).build()
		}

		when {
			//如果是 GET 则添加到 Query
			request.method == BilibiliProperty.Method.GET || forceQuery -> {
				httpUrl = request.url.newBuilder().apply {
					additionParams.forEach { (name, value) ->
						addQueryParameter(name, value.invoke())
					}
				}.build()
			}

			//如果 Body 不存在或者为空则创建一个 FormBody
			body == null || body.contentLength() == 0L -> {
				body = FormBody.Builder().apply {
					additionParams.forEachNonNull { name, value ->
						add(name, value)
					}
				}.build()
			}

			//如果 Body 为 FormBody 则里面可能已经存在内容
			body is FormBody -> {
				body = FormBody.Builder().addAllEncoded(body).apply {
					additionParams.forEachNonNull { name, value ->
						add(name, value)
					}
				}.build()
			}

			//如果方式不为 GET 且 Body 不为空或者为 FormBody 则无法添加公共参数
			else -> {

			}
		}

		return chain.proceed(
			request.newBuilder()
				.headers(headers)
				.url(httpUrl)
				.method(request.method, body)
				.build()
		)
	}
}