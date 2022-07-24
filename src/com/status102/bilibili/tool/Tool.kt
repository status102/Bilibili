package com.status102.bilibili.tool

import org.apache.commons.codec.digest.DigestUtils
import java.io.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


object Tool {

	/**
	 * 获取file的MD5值
	 * @param file 获取MD5的file
	 * @throws java.io.FileNotFoundException FileInputStream初始化时抛出
	 * @throws java.io.IOException DigestUtils.md5Hex抛出
	 */
	fun getFileMD5HexString(file: File): String? = DigestUtils.md5Hex(FileInputStream(file))

	/**
	 * 获取字符串的MD5
	 * @throws NoSuchAlgorithmException 找不到加密算法时报错
	 */
	@Throws(NoSuchAlgorithmException::class)
	fun getStringMD5(input: String): String =
		MessageDigest.getInstance("MD5").digest(input.toByteArray()).joinToString("") { "%02x".format(it) }

	fun getStackTraceInfo(e: Throwable): String {
		var stringWriter: StringWriter? = null
		var printWriter: PrintWriter? = null
		return try {
			stringWriter = StringWriter()
			printWriter = PrintWriter(stringWriter)
			e.printStackTrace(printWriter) // 将出错的栈信息输出到printWriter中
			printWriter.flush()
			stringWriter.flush()
			stringWriter.toString()
		} catch (ex: Exception) {
			"发生错误"
		} finally {
			if (stringWriter != null) {
				try {
					stringWriter.close()
				} catch (e1: IOException) {
					e1.printStackTrace()
				}
			}
			printWriter?.close()
		}
	}
}