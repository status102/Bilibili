package com.status102.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

object TimeUtils {

	val unixTime: Long
		inline get() = Instant.now().epochSecond

	val unixStrTime: String?
		get() = unixToStrTime(unixTime)

	val currentStrTime: String
		get() = currentToStrTime(System.currentTimeMillis())

	// 将时间戳转为字符串
	fun unixToStrTime(cc_time: Long): String? {
		var reStrTime: String?
		val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
		// 例如：cc_time=1291778220
		reStrTime = sdf.format(Date(cc_time * 1000L))
		return reStrTime
	}

	// 将时间戳转为字符串
	fun currentToStrTime(cc_time: Long): String {
		val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
		return dateFormat.format(cc_time)
	}

	fun strToUnixTime(strTime: String?): Long {
		val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
		var date: Date
		try {
			date = sdf.parse(strTime)
			val l = date.time
			val str = l.toString()
			return str.substring(0, 10).toLong()
		} catch (e: ParseException) {
			e.printStackTrace()
		}
		return -1
	}

	fun strToCurrentTime(strTime: String?): String {
		val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
		val d: Date
		try {
			d = sdf.parse(strTime)
			val l = d.time
			val str = l.toString()
			return str.substring(0, 10)
		} catch (e: ParseException) {
			e.printStackTrace()
		}
		return ""
	}

	fun calculateLength(length: Long): String {
		var mlength = length
		val second = (mlength % 60).toInt()
		mlength /= 60
		val minute = (mlength % 60).toInt()
		mlength /= 60
		val hour = (mlength % 24).toInt()
		mlength /= 24
		val day = mlength.toInt()
		return String.format("%d:%02d:%02d:%02d", day, hour, minute, second)
	}
}