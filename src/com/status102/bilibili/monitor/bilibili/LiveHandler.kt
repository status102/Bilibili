package com.status102.bilibili.monitor.bilibili

import io.netty.buffer.Unpooled
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.FullHttpResponse
import io.netty.handler.codec.http.websocketx.*
import io.netty.handler.timeout.IdleState
import io.netty.handler.timeout.IdleStateEvent
import io.netty.util.CharsetUtil
import java.nio.charset.StandardCharsets
import java.util.zip.Inflater

abstract class LiveHandler(
	private val handShaker: WebSocketClientHandshaker,
	private val userId: Long,
	private val areaId: Int,
	private val areaName: String,
	private val roomId: Long,
	private val key: String,
	private val analyzer: LiveAnalyze
) :
	SimpleChannelInboundHandler<Any>() {
	/**
	 * 判断wss握手是否成功
	 */
	var handshakeFuture: ChannelPromise? = null

	/**
	 * 判断有没有成功连接上弹幕服务
	 */
	private var isConnect = false
	abstract fun sendMsg(msg: String)

	companion object {
		private val HEART_BYTES = byteArrayOf(
			0, 0, 0, 0x1f, 0, 0x10, 0, 1, 0, 0, 0, 2, 0, 0, 0, 1,
			0x5b, 0x6f, 0x62, 0x6a, 0x65, 0x63, 0x74, 0x20, 0x4f, 0x62, 0x6a, 0x65, 0x63, 0x74, 0x5d
		) //[object Object]
	}

	/**
	 * wss握手之后不应该出现FullHttpResponse，收到FullHttpResponse抛出IllegalStateException
	 */
	override fun channelRead0(ctx: ChannelHandlerContext?, msg: Any?) {
		if (ctx == null || msg == null) {
			return
		}
		val channel: Channel = ctx.channel()
		if (!handShaker.isHandshakeComplete) {
			try {
				handShaker.finishHandshake(channel, msg as FullHttpResponse)
				handshakeFuture!!.setSuccess()
				val enterRoomString =
					"{\"uid\":${userId}," +
							"\"roomid\":${roomId}," + // 进房包中的roomId决定接收哪个房间的弹幕
							"\"protover\":2," +
							"\"platform\":\"web\"," +
							"\"clientver\":\"1.10.1\"," +
							"\"type\":2," +
							"\"key\":\"${key}\"}"
				val byteData = enterRoomString.toByteArray(StandardCharsets.UTF_8)
				val enterRoom = Unpooled.buffer(byteData.size + 16)
				enterRoom.writeInt(byteData.size + 16).writeShort(16).writeShort(1).writeInt(7).writeInt(1)
					.writeBytes(byteData)
				ctx.writeAndFlush(BinaryWebSocketFrame(enterRoom))
			} catch (e: WebSocketHandshakeException) {
				handshakeFuture!!.setFailure(e)
			}
			return
		}
		if (msg is FullHttpResponse) {
			throw IllegalStateException(
				"Unexpected FullHttpResponse (getStatus=" + msg.status() + ", content=" + msg.content()
					.toString(CharsetUtil.UTF_8) + ')'
			)
		}

		val frame = msg as WebSocketFrame
		if (frame is BinaryWebSocketFrame) {
			val buf = frame.content().apply {
				readInt()
				readShort()
				readShort()
				readInt()
				readInt()
			} ?: return
			val dataArray = ByteArray(buf.readableBytes())
			buf.readBytes(dataArray, 0, buf.readableBytes())
			if (!isConnect) {
				if (dataArray.toString(StandardCharsets.UTF_8).contentEquals("{\"code\":0}")) {
					isConnect = true
					sendMsg("连接成功")
					ctx.writeAndFlush(BinaryWebSocketFrame(Unpooled.copiedBuffer(HEART_BYTES)))
				}
			} else {
				if (dataArray.size > 4) {
					//byte[] data = new byte[buf.readableBytes()];
					val cmd = dataArray.toString(StandardCharsets.UTF_8)
					when {
						cmd.contains("cmd") -> {
							//如果是已经进了handler的数据包如果想要继续传递要先
							// ReferenceCountUtil.retain(msg)修改计数器
							// ctx.fireChannelRead(dataArray)
							analyzer.analyze(areaId, areaName, roomId, dataArray)
						}
						else -> {// 貌似只有弹幕包会压缩
							val packageHead = ByteArray(16)
							val inflater = Inflater()
							inflater.setInput(dataArray)
							//offset 偏移量是目标数组的，先解压16长度的包头，获取数据包总长度，然后创建数组packageBody
							inflater.inflate(packageHead, 0, 16)
							val stringBuilder = StringBuilder()
							for (i in 0..3) {
								stringBuilder.append(packageHead[i].toString(16))
							}
							val packageBody = ByteArray(stringBuilder.toString().toInt(16) - 16)
							inflater.inflate(packageBody)
							inflater.end()
							analyzer.analyze(areaId, areaName, roomId, packageBody)
						}
					}
				}
			}
		} else if (frame is PongWebSocketFrame) {
			println("WebSocket Client received pong")
		} else if (frame is CloseWebSocketFrame) {
			println("WebSocket Client received closing")
			channel.close()
		}
	}

	override fun handlerAdded(ctx: ChannelHandlerContext) {
		handshakeFuture = ctx.newPromise()
	}

	override fun channelActive(ctx: ChannelHandlerContext) {
		handShaker.handshake(ctx.channel())
	}


	override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
		cause.printStackTrace()
		if (!handshakeFuture!!.isDone) {
			handshakeFuture!!.setFailure(cause)
		}
		ctx.close()
	}

	@Throws(Exception::class)
	override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any?) {
		if (evt is IdleStateEvent) {
			if (evt.state() == IdleState.READER_IDLE) {
				println("输入超时连接断开")
				ctx.channel().close()
			} else if (evt.state() == IdleState.WRITER_IDLE) {
				if (isConnect) {
					ctx.writeAndFlush(BinaryWebSocketFrame(Unpooled.copiedBuffer(HEART_BYTES)))
				}
			}
		} else {
			super.userEventTriggered(ctx, evt)
		}
	}
}