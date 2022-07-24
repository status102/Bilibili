package com.status102.bilibili.retrofit

import com.status102.bilibili.OkHttpObject
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * copy自czp3009的bilibili-api项目
 */

inline fun FormBody.forEach(block: (String, String) -> Unit) {
	repeat(size) {
		block(encodedName(it), encodedValue(it))
	}
}

fun FormBody.raw() =
	StringBuilder().apply {
		repeat(size) {
			if (it != 0) append('&')
			append(encodedName(it))
			append('=')
			append(encodedValue(it))
		}
	}.toString()

fun FormBody.sortedRaw(): String {
	val nameAndValue = ArrayList<String>()
	repeat(size) {
		nameAndValue.add("${encodedName(it)}=${encodedValue(it)}")
	}
	return nameAndValue.sorted().joinToString(separator = "&")
}

fun FormBody.containsEncodedName(name: String): Boolean {
	repeat(size) {
		if (encodedName(it) == name) return true
	}
	return false
}

fun FormBody.Builder.addAllEncoded(formBody: FormBody): FormBody.Builder {
	with(formBody) {
		repeat(size) {
			addEncoded(encodedName(it), encodedValue(it))
		}
	}
	return this
}

internal typealias ParamExpression = Pair<String, () -> String?>

internal inline fun Array<out ParamExpression>.forEachNonNull(action: (String, String) -> Unit) {
	forEach { (name, valueExpression) ->
		val value = valueExpression()
		if (value != null) {
			action(name, value)
		}
	}
}

object RetrofitExtension {
	fun <T> buildRetrofit(
		baseUrl: String,
		javaClass: Class<T>,
		vararg interceptor: Interceptor,
		addSignAndSortInterceptor: Boolean = true,
		addFailureRequestInterceptor: Boolean = true
	): T = Retrofit.Builder()
		.baseUrl(baseUrl)
		.addConverterFactory(GsonConverterFactory.create())
		.client(OkHttpClient.Builder().apply {
			interceptor.forEach { addInterceptor(it) }
			if (addSignAndSortInterceptor) addInterceptor(OkHttpObject.sortAndSignInterceptor)
			if (addFailureRequestInterceptor) addInterceptor(OkHttpObject.failureRequestInterceptor)
			dispatcher(OkHttpObject.dispatcher)
			connectionPool(OkHttpObject.connectionPool)
		}.build())
		.build()
		.create(javaClass)
}