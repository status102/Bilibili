package com.status102.bilibili.monitor.bilive

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.FullHttpResponse
import io.netty.handler.codec.http.websocketx.*
import io.netty.handler.timeout.IdleState
import io.netty.handler.timeout.IdleStateEvent
import io.netty.util.CharsetUtil

abstract class BiliveHandler(private val handShaker: WebSocketClientHandshaker) : SimpleChannelInboundHandler<Any>() {
	 var handshakeFuture: ChannelPromise? = null
	abstract fun channelInactive()

	override fun handlerAdded(ctx: ChannelHandlerContext) {
		handshakeFuture = ctx.newPromise()
	}

	override fun channelActive(ctx: ChannelHandlerContext) {
		handShaker.handshake(ctx.channel())
	}

	override fun channelRead0(ctx: ChannelHandlerContext, msg: Any) {
		if (!handShaker.isHandshakeComplete) {
			val channel = ctx.channel()
			try {
				handShaker.finishHandshake(channel, msg as FullHttpResponse)
				//System.out.println("WebSocket Client connected!");
				handshakeFuture!!.setSuccess()
			} catch (e: WebSocketHandshakeException) {
				println("WebSocket Client failed to connect")
				handshakeFuture!!.setFailure(e)
			}
			return
		}
		if (msg is FullHttpResponse) {
			val response = msg
			throw IllegalStateException(
				"Unexpected FullHttpResponse (getStatus=" + response.status() + ", content=" + response.content()
					.toString(CharsetUtil.UTF_8) + ')'
			)
		} else if (msg is PingWebSocketFrame) { // 每隔60s受到一个来自服务器的ping包
			//System.out.println("ping");
			ctx.writeAndFlush(PongWebSocketFrame())
		} else if (msg is WebSocketFrame) {
			val frame = msg
			if (frame is TextWebSocketFrame) {
				BiliveAnalyzer.analyze(frame.text())
			} else if (frame is BinaryWebSocketFrame) {
				//System.out.println(frame.content().toString());
			}
		} else {
			println("read：" + msg.javaClass)
		}
	}

	override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any?) {
		if (evt is IdleStateEvent) {
			if (evt.state() == IdleState.READER_IDLE) {
				println("心跳超时") // 每隔60s会受到来自服务器的一个ping包
				ctx.channel().close()
			} else if (evt.state() == IdleState.WRITER_IDLE) {
				ctx.writeAndFlush( PingWebSocketFrame());
				//System.out.println("发送心跳");
			}
		}
		//super.userEventTriggered(ctx, evt);
	}

	@Throws(Exception::class)
	override fun channelInactive(ctx: ChannelHandlerContext?) {
		channelInactive()
		super.channelInactive(ctx)
	}

	@Throws(Exception::class)
	override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
		//cause?.printStackTrace()
		super.exceptionCaught(ctx, cause)
	}
}