package com.status102.bilibili

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.management.ManagementFactory
import java.net.NetworkInterface

class C {
	// 私有函数，所以其返回类型是匿名对象类型
	private fun foo() = object : Any() {
		val x: String = "x"
	}

	// 公有函数，所以其返回类型是 Any
	fun publicFoo() = object {
		val x: String = "x"
		fun get() {}
	}

	fun bar() {
		val x = foo()
		val x1 = x.x        // 没问题
		val x2 = publicFoo()  // 错误：未能解析的引用“x”
		val xx = x2
	}
}

fun main() {
	runBlocking {
		println(ManagementFactory.getRuntimeMXBean().pid)
		launch {
			repeat(10) {
				delay(2000)
				println("%d---launch 1".format(ManagementFactory.getRuntimeMXBean().pid))
			}
		}
		delay(500)
		val j = launch {
			repeat(10) {
				delay(2000)
				println("%d---launch 2".format(ManagementFactory.getRuntimeMXBean().pid))
			}
		}
	}
	println(ManagementFactory.getRuntimeMXBean().pid)
	//ManagementFactory.getRuntimeMXBean().pid
	//NetworkInterface.getNetworkInterfaces().toList().forEach{println(it.displayName + "---" + it.isUp)}
}
