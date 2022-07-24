package com.status102.utils

import java.util.*

object DateUtils {
	// object.setTimeZone(TimeZone.getTimeZone("GMT+:08:00"));
	val calendar: Calendar
		get() =// object.setTimeZone(TimeZone.getTimeZone("GMT+:08:00"));
			Calendar.getInstance(TimeZone.getTimeZone("GMT+8"))

	val year: Int
		get() = calendar[Calendar.YEAR]

	val month: Int
		get() = calendar[Calendar.MONTH] + 1

	val day: Int
		get() = calendar[Calendar.DATE]

	val hour: Int
		get() = calendar[Calendar.HOUR_OF_DAY]

	val minute: Int
		get() = calendar[Calendar.MINUTE]

	val second: Int
		get() = calendar[Calendar.SECOND]

	val weekDay: Int
		get() = (calendar[Calendar.DAY_OF_WEEK] + 6) % 7
}