package com.status102.bilibili.monitor.bilibili

import com.status102.bilibili.PCController
import com.status102.utils.TimeUtils

class LiveAnalyze {
	companion object {
		val analyzer: LiveAnalyze = LiveAnalyze()
	}

	fun analyze(areaId: Int, areaName: String, roomId: Long, data: ByteArray) {
		PCController.printlnMsg(TimeUtils.unixStrTime + String(data))
	}
}