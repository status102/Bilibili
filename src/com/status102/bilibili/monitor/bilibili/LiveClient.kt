package com.status102.bilibili.monitor.bilibili

import com.status102.bilibili.PCController
import com.status102.bilibili.account.User
import com.status102.bilibili.api.live.room.LiveRoomApi
import com.status102.bilibili.retrofit.exception.ApiFailureException
import com.status102.bilibili.retrofit.exception.ResponseConvertException
import com.status102.bilibili.tool.Tool
import com.status102.utils.TimeUtils
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.http.*
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory
import io.netty.handler.codec.http.websocketx.WebSocketVersion
import io.netty.handler.ssl.SslHandler
import io.netty.handler.timeout.IdleStateHandler
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.URI
import java.net.URISyntaxException
import java.time.Instant
import javax.net.ssl.SSLContext
import kotlin.random.Random

class LiveClient(
	private val workerGroup: EventLoopGroup,
	private val analyzer: LiveAnalyze = LiveAnalyze.analyzer,
	private val user: User? = null
) {
	private var channelFuture: ChannelFuture? = null
	var areaId: Int = -1
	var areaName: String = ""
	var roomId: Long = -1

	suspend fun connect(roomId: Long, userId: Long = 0): Boolean =
		withContext(PCController.workerPool.asCoroutineDispatcher()) {
			val key: String
			val host: String
			val port: Int

			try {
				LiveRoomApi.getDanmuInfo(roomId).also {
					it.data.apply {
						key = token
						if (hostList.isNotEmpty()) {
							hostList.random(Random(Instant.now().epochSecond)).apply {
								host = this.host
								port = wssPort
							}
						} else {
							PCController.printlnMsg("连接${roomId}失败：初始key获取失败，\n$it")
							return@withContext false
						}
					}
				}
				LiveRoomApi.getInfoByRoom(roomId).data.roomInfo?.apply {
					this@LiveClient.areaId = parentAreaId ?: this@LiveClient.areaId
					this@LiveClient.areaName = parentAreaName ?: this@LiveClient.areaName
					this@LiveClient.roomId = this.roomId ?: roomId
				}
			} catch (e: HttpException) {
				PCController.printlnMsg("连接${roomId}失败：初始key获取失败，\n${Tool.getStackTraceInfo(e)}")
				return@withContext false
			} catch (e: ApiFailureException) {
				PCController.printlnMsg("连接${roomId}失败：初始key获取失败，\n${e.jsonObject}\n${Tool.getStackTraceInfo(e)}")
				return@withContext false
			} catch (e: ResponseConvertException) {
				PCController.printlnMsg("连接${roomId}失败：初始key获取失败，\n${Tool.getStackTraceInfo(e)}")
				return@withContext false
			} catch (e: IOException) {
				PCController.printlnMsg("连接${roomId}失败：初始key获取失败，\n${Tool.getStackTraceInfo(e)}")
				return@withContext false
			}

			val boot = Bootstrap()

			try {
				val uri = URI("wss://$host:$port/sub")
				val httpHeaders = DefaultHttpHeaders()
				httpHeaders[HttpHeaderNames.ACCEPT_ENCODING] = ""
				httpHeaders[HttpHeaderNames.ACCEPT_LANGUAGE] = "zh-CN,zh;q=0.9"
				httpHeaders[HttpHeaderNames.CACHE_CONTROL] = HttpHeaderValues.NO_CACHE
				httpHeaders["Sec-WebSocket-Extensions"] = "permessage-deflate; client_max_window_bits"
				val handShaker = WebSocketClientHandshakerFactory.newHandshaker(
					uri,
					WebSocketVersion.V13,
					null,
					true,
					httpHeaders
				)
				val handler = object : LiveHandler(handShaker, userId, areaId, areaName, roomId, key, analyzer) {
					override fun sendMsg(msg: String) {
						PCController.printlnMsg("${TimeUtils.unixStrTime} [$areaId-${areaName}]${roomId}房：$msg")
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
							val sslEngine = SSLContext.getDefault().createSSLEngine()
							sslEngine.useClientMode = true
							pipeline.addLast("ssl", SslHandler(sslEngine))
							pipeline.addLast(IdleStateHandler(45, 30, 0)) //.addLast(new HttpContentDecompressor())
								.addLast(HttpClientCodec())
								.addLast(HttpContentDecompressor())
								.addLast(HttpObjectAggregator(4 * 1024))
								//.addLast(LoggingHandler(LogLevel.INFO))
								.addLast(handler)
						}
					})

				val channelFuture = boot.connect(host, port) ?: return@withContext false
				channelFuture.addListener { future ->
					if (!future.isSuccess && future.cause() is ConnectException) {
						PCController.printlnMsg("getException:" + future.cause().message)
						future.cause().printStackTrace()
					}
				}
				channelFuture.await()
				handler.handshakeFuture?.sync()
				this@LiveClient.channelFuture = channelFuture
				return@withContext channelFuture.isSuccess
			} catch (e: URISyntaxException) {
				e.printStackTrace()
			} catch (e: InterruptedException) {
				e.printStackTrace()
			} catch (e: IOException) {
				e.printStackTrace()
			}
			return@withContext false
		}

	fun disConnect() {
		if (channelFuture != null) {
			channelFuture!!.channel().close()
		}
	}

	override fun equals(other: Any?): Boolean {
		if (other == null || other.javaClass != this.javaClass) return false
		if (other is LiveClient && other.roomId == this.roomId) return true
		return false
	}
}