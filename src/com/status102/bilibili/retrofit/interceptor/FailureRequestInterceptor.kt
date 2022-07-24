package com.status102.bilibili.retrofit.interceptor

import com.google.gson.JsonParser
import com.google.gson.stream.MalformedJsonException
import com.status102.bilibili.retrofit.exception.ApiFailureException
import com.status102.bilibili.retrofit.exception.ResponseConvertException
import okhttp3.Interceptor
import okhttp3.Response
import java.net.SocketTimeoutException
import javax.net.ssl.SSLException

/**
 * 检查返回数据格式
 * @throws ResponseConvertException 无法解析为json抛出
 * @throws ApiFailureException code不为0抛出
 */
class FailureRequestInterceptor : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		val request = chain.request()
		val response = try {
			chain.proceed(request)
		} catch (e: SocketTimeoutException) {
			Thread.sleep(1000)
			chain.proceed(request)
		}
		val body = response.body
		if (!response.isSuccessful || body == null || body.contentLength() == 0L) return response
		//获取字符集
		val contentType = body.contentType()
		val charset = if (contentType == null) {
			Charsets.UTF_8
		} else {
			contentType.charset(Charsets.UTF_8)!!
		}

		/*if (request.url.toString().contains("smalltv")) {
			print("")
		}*/

		//拷贝流
		val inputStreamReader = body.source().also {
			it.request(Long.MAX_VALUE)
		}.buffer.clone().inputStream().reader(charset)

		//读取其内容
		val jsonObject = try {
			JsonParser.parseString(inputStreamReader.readText()).asJsonObject
		} catch (exception: MalformedJsonException) {
			//该注释来自czp的项目
			//如果返回内容解析失败, 说明它不是一个合法的 json
			//如果Entity和返回数据字段的类型不一致 会导致 Retrofit 的异步请求一直卡着直到超时
			throw ResponseConvertException(response)
		} finally {
			inputStreamReader.close()
		}
		if (jsonObject["code"].asInt != 0) {
			throw ApiFailureException(jsonObject)
		}
		return response
	}
}