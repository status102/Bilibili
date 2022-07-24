package com.status102.bilibili

import com.status102.bilibili.account.AccountLoader
import com.status102.bilibili.account.User
import com.status102.bilibili.api.live.account.AccountApi
import com.status102.bilibili.api.live.gift.LiveGiftApi
import com.status102.bilibili.api.live.relation.LiveRelationService
import com.status102.bilibili.api.live.wallet.WalletApi
import com.status102.bilibili.api.main.judgement.JudgementApi
import com.status102.bilibili.monitor.Monitor
import com.status102.bilibili.retrofit.RetrofitExtension
import com.status102.utils.DateUtils
import com.status102.utils.TimeUtils
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis


//private val runPath = System.getProperty("user.dir")

class PCController {
	companion object {
		val test: Boolean = true

		private const val LOG_PATH = "/log"
		private const val MAIN_LOG_DIR = "/[main]"
		private const val ERROR_LOG_PATH = "/error"
		lateinit var runPath: String
		val bossPool: ScheduledExecutorService by lazy { Executors.newScheduledThreadPool(1) }
		val workerPool: ExecutorService by lazy { Executors.newCachedThreadPool() }
		val workerDispatcher
			get() = workerPool.asCoroutineDispatcher()
		val monitor: Monitor by lazy { Monitor() }

		val userList = mutableListOf<User>()
		var saveAccount: Boolean = false
		private lateinit var logFilePath: String
		private lateinit var logFile: File
		private var logFileWriter: OutputStreamWriter? = null
		private var logBuilder = StringBuilder()

		//private val coroutineCallAdapterFactory = CoroutineCallAdapterFactory()

		private suspend fun init(): Unit = coroutineScope {
			val initErrIO = launch { initErrorDir(runPath) }//TODO 重定向err流已被注释
			val initLogDir = launch { initLogDir(runPath) }
			initErrIO.join()
			initLogDir.join()
			//launch { monitor.initBiliLiveConnect(0) }
			bossPool.scheduleAtFixedRate(
				{ workerPool.execute { GlobalScope.launch(workerPool.asCoroutineDispatcher()) { minuteCheck() } } },
				0,
				60,
				TimeUnit.SECONDS
			)
			return@coroutineScope
		}

		private fun initErrorDir(storagePath: String) {
			val path = storagePath + ERROR_LOG_PATH
			val errorDir = File(path)
			if (!errorDir.exists()) {
				if (!errorDir.mkdirs()) {
					println("\"$path\"不存在，创建失败")
					return
				}
			}
			if (!errorDir.canWrite()) {
				if (!errorDir.setExecutable(true)) {
					println("\"$path\"不可写，设为可写失败")
					return
				}
			}
			val array = errorDir.listFiles()
			if (array == null) {
				println("\"${path}\".listFiles不可用")
			} else {
				for (file in array) {
					if (file.length() == 0.toLong()) {
						if (!file.delete()) {
							println("\"${file.path}\"为空，删除失败")
						}
					}
				}
			}
			val errorFileName = "/%s.log".format(TimeUtils.unixStrTime).replace(':', '_')
			val errorFile = File(path + errorFileName)
			if (!errorFile.exists()) {
				if (!errorFile.createNewFile()) {
					println("目录“%s”不存在，创建失败")
					return
				}
			}
			if (!errorFile.canWrite()) {
				if (!errorFile.setWritable(true)) {
					println("目录“%s”不可写，设置写入权限失败")
					return
				}
			}
			System.setErr(PrintStream(errorFile))
		}

		private fun initLogDir(storagePath: String) {
			logFilePath = runPath + LOG_PATH + MAIN_LOG_DIR + "/%04d-%02d-%02d.log".format(
				DateUtils.year,
				DateUtils.month,
				DateUtils.day
			)
			logFile = File(logFilePath)
			val path = storagePath + LOG_PATH
			val logDir = logFile.parentFile
			if (!logDir.exists()) {
				if (!logDir.mkdirs()) {
					println("\"$path\"不存在，创建失败")
					return
				}
			}
			if (!logDir.canWrite()) {
				if (!logDir.setExecutable(true)) {
					println("\"$path\"不可写，设为可写失败")
					return
				}
			}
		}

		suspend fun loadAccount() {
			AccountLoader.loadAccountFile(runPath, userList)
		}

		fun saveAccount() {
			AccountLoader.saveAccountFile(runPath, userList)
		}

		suspend fun minuteCheck() {
			writeLog(runPath + LOG_PATH)
			userList.forEach {
				it.writeLog(runPath + LOG_PATH)
			}
			if (DateUtils.minute % 20 == 0 || saveAccount) {
				saveAccount = false
				saveAccount()
			}
			when (val time = DateUtils.hour to DateUtils.minute) {
				0 to 0 -> {
					userList.forEach {
						GlobalScope.launch { it.dailyCheck() }
					}
				}
				4 to 0 -> {
					workerPool.execute {
						userList.forEach {
							it.watchLive.enterWatchRoom()
							Thread.sleep((5..30).random() * 1000L)
						}
						monitor.resetBiliveClient()

					}
				}
				12 to 0 -> GlobalScope.launch {
					userList.forEach {
						bossPool.schedule(
							{
								GlobalScope.launch {
									AccountApi.run {
										checkDailySign(it)
										checkDailyTask(it)
									}
									LiveGiftApi.getDailyBagStatus(it)
									if (it.account.setting.isExchangeSilverToCoin) {
										WalletApi.silver2Coin(it)
									}
									if (it.account.setting.isJudgement) {
										JudgementApi.dailyVote(it)
									}
									it.watchLive.enterWatchRoom()
								}
							}, (0..7200).random() * 1000L, TimeUnit.MILLISECONDS
						)
					}
				}
				23 to 0 -> GlobalScope.launch {
					userList.forEach {
						bossPool.schedule(
							{
								GlobalScope.launch {
									AccountApi.run {
										checkDailySign(it)
										checkDailyTask(it)
									}
									LiveGiftApi.getDailyBagStatus(it)
									if (it.account.setting.isExchangeSilverToCoin) {
										WalletApi.silver2Coin(it)
									}
									if (it.account.setting.isJudgement) {
										JudgementApi.dailyVote(it)
									}
								}
							}, (0..1800).random() * 1000L, TimeUnit.MILLISECONDS
						)
					}
				}
				else -> {
					//println(time)
				}
			}
		}

		fun printMsg(string: String, addTime: Boolean = true) =
			StringBuilder().apply {
				if (addTime) append(TimeUtils.unixStrTime + " ")
				append(string)
			}.run {
				print(this)
				logBuilder.append("${toString()}\n")
			}

		fun printlnMsg(string: String, addTime: Boolean = true) =
			StringBuilder().apply {
				if (addTime) append(TimeUtils.unixStrTime + " ")
				append(string)
			}.run {
				println(this)
				logBuilder.append("${toString()}\n")
			}

		private fun writeLog(storagePath: String) {
			val logFilePath = "${storagePath}${MAIN_LOG_DIR}/%04d-%02d-%02d.log".format(
				DateUtils.year,
				DateUtils.month,
				DateUtils.day
			)
			if (!logFilePath.contentEquals(this.logFilePath)) {
				logFileWriter?.close()
				logFileWriter = null
				logFile = File(logFilePath)
			}
			if (!logFile.parentFile.exists()) {
				logFile.parentFile.mkdirs()
			}
			if (!logFile.exists()) {
				logFile.createNewFile()
			}
			if (logFileWriter == null) {
				logFileWriter = FileOutputStream(logFile, true).writer(StandardCharsets.UTF_8)
			}

			logFileWriter?.write(String(logBuilder).run {
				logBuilder.clear()
				if (System.getProperty("os.name").toLowerCase().contains("windows"))
					replace("\n", "\r\n")
				else
					this
			})
			logFileWriter?.flush()
		}
	}

	suspend fun start(): Unit = coroutineScope {
		launch {
			val initTime: Long? = withTimeoutOrNull(60 * 1000) { measureTimeMillis { init() } }
			if (initTime == null) {
				printlnMsg("初始化失败：超时")
			} else {
				printlnMsg("初始化完成：耗时${initTime / 1000.0}s")
			}
		}
		launch {
			val initAccountTime = withTimeoutOrNull(60 * 1000) { measureTimeMillis { loadAccount() } }
			if (initAccountTime == null) {
				printlnMsg("账号加载失败：超时")
			} else {
				printlnMsg("账号加载完成：耗时${initAccountTime / 1000.0}s")
			}
		}
		workerPool.execute {
			GlobalScope.launch(workerDispatcher) { monitor.initBiliLiveConnect(-1) }
		}
		return@coroutineScope
	}

}

val COMMANDLIST = listOf(
	"未知指令，可用指令：",
	/*1*/"exit", "\t\t退出",
	/*3*/"loadAccount", "\t\t从account文件夹加载账号",
	/*5*/"saveAccount", "\t\t保存账号至account文件夹",
	/*7*/"ls", "\t\t查看服务器挂机账号",
	/*9*/"check", "\t\t手动检查",
	/*11*/"", ""
).map { it.toLowerCase() }

/**
 * 用于测试或者后台管理的命令集，同样不要求大小写
 */
val TESTCOMMANDLIST = listOf(
	""
).map { it.toLowerCase() }

fun abx(): Unit {

}

fun main(args: Array<String>) = runBlocking {
	//test()
	//withTimeoutOrNull(60 * 1000) { measureTimeMillis { test() } }
	/*println(
		ToolK.formatJson(
			JsonParser.parseString(
				GsonBuilder()
					.setExclusionStrategies(object : ExclusionStrategy {
						override fun shouldSkipClass(p0: Class<*>?): Boolean {
							return p0?.getAnnotation(Hidden::class.java) != null
						}

						override fun shouldSkipField(p0: FieldAttributes?): Boolean {
							return p0?.getAnnotation(Hidden::class.java) != null
						}

					})
					.serializeNulls().create().toJson(AccountK().also {
					}, AccountK::class.java)
			)
		)
	)
	*/
	/*
	GlobalScope.launch {
		delay(1000)
		//{
			try {
				abx()
			} catch (e: NullPointerException) {
				e.printStackTrace()
			}
		println(321)
		//}
	}
	GlobalScope.launch {
		println(13)
		delay(3000)
		println(123)
	}
	Thread.sleep(8000)*/
	/*if (PCController.test) {
		return@runBlocking
	}*/
	launch {
		PCController.runPath = System.getProperty("user.dir")!!
		PCController().start()
	}.join()
	var command: String? = readLine()
	while (command != null) {
		doCommand(command)
		command = readLine()
	}
//test()
}

suspend fun doCommand(command: String) = if (command == "test") {
	println("test")
	/*PCController.userList.forEach {
		RetrofitExtension.buildRetrofit(
			LiveRelationService.BASE_URL,
			LiveRelationService::class.java,
			it.addHeaderInterceptor,
			it.addParamInterceptor
		).getSubscribeLiveRoomList().run { println(this) }
	}*/
	GlobalScope.launch {
		PCController.userList.forEach {
			PCController.bossPool.schedule(
				{
					GlobalScope.launch {
						AccountApi.run {
							checkDailySign(it)
							checkDailyTask(it)
						}
						LiveGiftApi.getDailyBagStatus(it)
						if (it.account.setting.isExchangeSilverToCoin) {
							WalletApi.silver2Coin(it)
						}
						if (it.account.setting.isJudgement) {
							JudgementApi.dailyVote(it)
						}
						it.watchLive.enterWatchRoom()
					}
				}, (0..5).random() * 1000L, TimeUnit.MILLISECONDS
			)
		}
	}
} else when (COMMANDLIST.indexOf(command.trim().toLowerCase())) {
	1 -> exitProcess(0)
	3 -> PCController.loadAccount()
	5 -> PCController.saveAccount()
	7 -> PCController.userList.run {
		//ToDo 添加连接的房间显示
		PCController.printlnMsg("共计${size}个账号")
		forEach {
			PCController.printlnMsg(
				"[${it.account.accountStatusStr}] [%s] (%s---%s)\t".format(
					it.account.nickname ?: "",
					it.account.userName,
					it.account.loginName
				), false
			)
		}
	}
	9 -> PCController.minuteCheck()
	else -> {

		PCController.printMsg("\"$command\"", false)
		for (index: Int in COMMANDLIST.indices) {
			PCController.printMsg(COMMANDLIST[index], false)
			if (index % 2 == 0)
				PCController.printlnMsg("", false)
		}
	}
}

private val set = (1..10).toMutableSet()
var a: Int = 1
	set(value) {
		println(value)
	}

fun test() {

	//LiveClient(NioEventLoopGroup()).connect(2715784)
	/*val user = User()
	user.run {
		account = AccountK(
			loginName = "1021263881@qq.com",
			password = "zrq@20010319"
		)
		login()
	}*/
	//println(Gson().fromJson("{\"code\":0,\"message\":\"0\",\"ttl\":1,\"data\":{\"storm\":null,\"guard\":[],\"slive_box\":{\"minute\":0,\"silver\":0,\"time_end\":0,\"time_start\":0,\"times\":0,\"max_times\":0,\"status\":1},\"activity_box\":null,\"bls_box\":null,\"gift_list\":[],\"pk\":[],\"danmu\":null,\"danmu_gift\":null,\"anchor\":null,\"icon_tap_time\":13}}",
	//LotteryInfo::class.java))
//	LiveLotteryApi.getLotteryInfo(user, 2715784)
}

@Target(AnnotationTarget.FIELD)
annotation class Hidden