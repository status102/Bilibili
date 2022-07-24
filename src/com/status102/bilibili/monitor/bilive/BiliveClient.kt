package com.status102.bilibili.monitor.bilive

import com.status102.bilibili.PCController
import com.status102.bilibili.tool.Tool
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.http.DefaultHttpHeaders
import io.netty.handler.codec.http.HttpClientCodec
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory
import io.netty.handler.codec.http.websocketx.WebSocketVersion
import io.netty.handler.ssl.SslHandler
import io.netty.handler.timeout.IdleStateHandler
import java.net.ConnectException
import java.net.URI
import java.net.URISyntaxException
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext

class BiliveClient(private var workerGroup: EventLoopGroup) {
	companion object {
		const val URL =
			"wss://bilive.halaal.win/server/"
		//"ws://47.101.153.223:20080/"
		const val PROTOCOL =
			"164c48292a6c773c85716093589cd60e"
		// "ff5f0db2548baecbcd21c7a50ece57a3"
	}

	private var channelFuture: ChannelFuture? = null

	fun connect(): Boolean {
		val boot = Bootstrap()
		val httpHeaders = DefaultHttpHeaders()
		//httpHeaders.add("User-Agent", "Bilive_Client 2.2.6.2260V")
		//httpHeaders.add("servername","")
		try {
			/*var b = Request.Builder().head()
			b.url("https://bilive.halaal.win/server/")
				.build().run { OkHttpObject.defaultClient.newCall(this) }
				.execute().run {
					println(this)
				}*/

			val uri = URI(URL)
			val handShaker = WebSocketClientHandshakerFactory.newHandshaker(
				uri,
				// URI(URL.replace("wss://","https://")),
				WebSocketVersion.V13,
				PROTOCOL,
				true,
				httpHeaders
			)
			val biliveHandler: BiliveHandler = object : BiliveHandler(handShaker) {
				override fun channelInactive() {
					PCController.printlnMsg("与${BiliveAnalyzer.HEAD}断开，10s后重连")
					PCController.bossPool.schedule({
						PCController.workerPool.execute {
							while (!connect()) {
								Thread.sleep((120..600).random() * 1000L.also { PCController.printlnMsg("连接${BiliveAnalyzer.HEAD}失败，等待${it}s后重试") })
							}
						}
					}, 10, TimeUnit.SECONDS)
				}
			}
			boot.group(workerGroup)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.option(ChannelOption.TCP_NODELAY, true)
				.channel(NioSocketChannel::class.java)
				.handler(object : ChannelInitializer<SocketChannel>() {
					@Throws(Exception::class)
					override fun initChannel(socketChannel: SocketChannel) {
						val pipeline = socketChannel.pipeline()
						if (URL.contains("wss://")) {
							val sslEngine = SSLContext.getDefault().createSSLEngine(uri.host,443)
							sslEngine.useClientMode = true
							pipeline.addFirst("ssl", SslHandler(sslEngine))
						}
						pipeline.addLast(IdleStateHandler(120, 0, 0))
							.addLast(HttpClientCodec()) //.addLast(new HttpContentDecompressor())
							.addLast(HttpObjectAggregator(4 * 1024)) //.addLast(new LoggingHandler(LogLevel.INFO))
							.addLast(biliveHandler)
					}
				})
			channelFuture = boot.connect(uri.host, 443) ?: return false
			channelFuture?.addListener { future ->
				if (!future.isSuccess && future.cause() is ConnectException) {
					PCController.printlnMsg("${BiliveAnalyzer.HEAD}[$URL---$PROTOCOL]连接失败：${Tool.getStackTraceInfo(future.cause())}")
				}
			}
			channelFuture?.await()
			biliveHandler.handshakeFuture?.sync()
			return channelFuture?.isSuccess ?: false
		} catch (e: URISyntaxException) {
			PCController.printlnMsg("${BiliveAnalyzer.HEAD}[$URL---$PROTOCOL]连接失败：${Tool.getStackTraceInfo(e)}")
		} catch (e: InterruptedException) {
			PCController.printlnMsg("${BiliveAnalyzer.HEAD}[$URL---$PROTOCOL]连接失败：${Tool.getStackTraceInfo(e)}")
		}
		return false
	}

	fun disConnect() {
		if (channelFuture != null) {
			channelFuture?.channel()?.close()
			channelFuture = null
		}
	}
}