package com.status102.bilibili.account

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import com.status102.bilibili.Hidden
import com.status102.bilibili.PCController
import com.status102.bilibili.tool.Tool
import kotlinx.coroutines.*
import java.io.File
import java.security.NoSuchAlgorithmException

object AccountLoader {
	private const val ACCOUNT_PATH = "/account"

	suspend fun loadAccountFile(storagePath: String, userList: MutableList<User>) {
		val accountDirFile = File(storagePath + ACCOUNT_PATH)
		if (!accountDirFile.exists()) {
			if (!accountDirFile.mkdirs()) {
				println("\"$storagePath\"不存在，创建失败")
				return//@coroutineScope
			}
			return//@coroutineScope
		}
		PCController.printlnMsg("开始加载${accountDirFile.path}路径账号")
		val fileList = accountDirFile.listFiles()
		if (fileList == null) {
			println("fileList为空")
			return
		}
		val gson: Gson = GsonBuilder().setExclusionStrategies(object : ExclusionStrategy {
			override fun shouldSkipClass(p0: Class<*>?): Boolean {
				return p0?.getAnnotation(Hidden::class.java) != null
			}

			override fun shouldSkipField(p0: FieldAttributes?): Boolean {
				return p0?.getAnnotation(Hidden::class.java) != null
			}

		}).disableHtmlEscaping().serializeNulls().setPrettyPrinting().create()
		var user: User
		fileList.forEach {
			var data: String = withContext(Dispatchers.IO) {
				val reader = it.inputStream().reader(Charsets.UTF_8)
				val str = reader.readText()
				reader.close()
				str
			}

			user = User()
			user.account = gson.fromJson(data, AccountK::class.java) ?: return@forEach
			user.accountMD5 = try {
				Tool.getStringMD5(gson.toJson(user.account, AccountK::class.java))
			} catch (e: NoSuchAlgorithmException) {
				PCController.printlnMsg("${it.name}计算MD5出错${Tool.getStackTraceInfo(e)}")
				""
			}
			if (user in userList) {// 已登录该账号
				val md5: String = userList[userList.indexOf(user)].accountMD5
				if (!md5.contentEquals(user.accountMD5)) {// 如果account的配置被修改过，读取配置
					val index = userList.indexOf(user)
					userList[index].account = user.account
					userList[index].accountMD5 = user.accountMD5
					PCController.printlnMsg(
						"[%s](%s---%s)账号设置更正，账号状态：%s".format(
							user.account.nickname,
							user.account.userName,
							user.account.loginName,
							user.account.accountStatusStr
						)
					)
				}
				val fileNameFormat = "${user.account.nickname}---${user.account.loginName}.json"
				if (!it.name!!.contentEquals(fileNameFormat)) {
					it.renameTo(File("${accountDirFile.path}/${fileNameFormat}"))
				}
			} else {
				userList.add(user)
				PCController.printlnMsg(
					"[%s](%s---%s)载入成功，账号状态：%s".format(
						user.account.nickname,
						user.account.userName,
						user.account.loginName,
						user.account.accountStatusStr
					)
				)
				if (user.account.isLoggedIn) {
					user.loadAccount()
				} else {
					GlobalScope.launch {
						user.loadAccount()
					}
				}
				val fileNameFormat = "${user.account.nickname}---${user.account.loginName}.json"
				if (!it.name!!.contentEquals(fileNameFormat)) {
					it.renameTo(File("${accountDirFile.path}/${fileNameFormat}"))
				}
			}
		}
		PCController.printlnMsg("成功从${accountDirFile.path}读取账号")
	}

	fun saveAccountFile(storagePath: String, userList: MutableList<User>) {
		val accountDirFile = File(storagePath + ACCOUNT_PATH)
		if (!accountDirFile.exists()) {
			if (!accountDirFile.mkdirs()) {
				println("\"$storagePath\"不存在，创建失败")
				return
			}
			return
		}
		userList.forEach {
			val user = it
			val index = accountDirFile.list().indexOfFirst { it.contains("${user.account.loginName}.json") }
			val accountFile: File
			val fileNameFormat = "${user.account.nickname}---${user.account.loginName}.json"
			if (index == -1) {
				accountFile = File("${accountDirFile.path}/${fileNameFormat}")
			} else {
				accountFile = accountDirFile.listFiles()[index]
				if (!accountFile.name!!.contentEquals(fileNameFormat)) {
					val file = File("${accountDirFile.path}/${fileNameFormat}")
					accountFile.renameTo(file)
				}
			}
			val gson = GsonBuilder().setExclusionStrategies(object : ExclusionStrategy {
				override fun shouldSkipClass(p0: Class<*>?): Boolean {
					return p0?.getAnnotation(Hidden::class.java) != null
				}

				override fun shouldSkipField(p0: FieldAttributes?): Boolean {
					return p0?.getAnnotation(Hidden::class.java) != null
				}

			}).disableHtmlEscaping()/*.serializeNulls()*/.setPrettyPrinting().create()
			accountFile.outputStream().writer(Charsets.UTF_8)
				//FileWriter(accountFile, Charsets.UTF_8, false)
				.run {
					write(
						gson.toJson(user.account, AccountK::class.java)
							.run {
								if (System.getProperty("os.name").toLowerCase().contains("windows"))
									replace("\n", "\r\n")
								else
									this
							})
					flush()
					close()
				}
		}
		PCController.printlnMsg("成功保存账号至${accountDirFile.path}")
	}
}