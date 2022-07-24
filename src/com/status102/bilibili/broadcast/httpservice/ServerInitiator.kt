package com.status102.bilibili.broadcast.httpservice

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpServerCodec
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler
import io.netty.handler.ssl.SslContext
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.SelfSignedCertificate
import io.netty.handler.timeout.IdleStateHandler
import java.security.cert.CertificateException
import javax.net.ssl.SSLException

class ServerInitiator {
	private val WEBSOCKET_PATH = "/bili"
	private val DEFAULT_PORT = 23333
	private var channelFuture: ChannelFuture? = null
	private val bossGroup: EventLoopGroup = NioEventLoopGroup() // (1)

	private val workerGroup: EventLoopGroup = NioEventLoopGroup()

	fun start(port: Int, sslMode: Boolean = false) {
		try {
			val b = ServerBootstrap() // (2)
			b.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel::class.java) // (3)
				//.handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(object : ChannelInitializer<SocketChannel>() {
					// (4)
					override fun initChannel(channel: SocketChannel) {
						val pipeline = channel.pipeline()
						if (sslMode) {
							var ssc: SelfSignedCertificate? = null
							var sslCtx: SslContext? = null
							try {
								ssc = SelfSignedCertificate()
								sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build()
							} catch (e: CertificateException) {
								e.printStackTrace()
							} catch (e: SSLException) {
								e.printStackTrace()
							}
							if (sslCtx != null) {
								pipeline.addLast("ssl", sslCtx.newHandler(channel.alloc()))
							}
						}
						pipeline.addLast(IdleStateHandler(120, 0, 0))
							.addLast(HttpServerCodec())
							.addLast(HttpObjectAggregator(65536))
							.addLast(WebSocketServerCompressionHandler())
							.addLast(WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true))
							.addLast(WebSocketFullHttpRequestHandler())
							//.addLast(ServerTextFrameHandler(this@Server))
							.addLast(object : SimpleChannelInboundHandler<PingWebSocketFrame?>() {
								override fun channelRead0(
									channelHandlerContext: ChannelHandlerContext,
									pingWebSocketFrame: PingWebSocketFrame?
								) {
									channelHandlerContext.writeAndFlush(PongWebSocketFrame())
								}
							})
						// 用于大数据流的分区传输
						//channel.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
					}
				})
			//.option(ChannelOption.SO_BACKLOG, 128)          // (5)
			//.childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

			// Bind and start to accept incoming connections.
			channelFuture = b.bind(if (port != -1) port else DEFAULT_PORT).sync() // (7)

			// Wait until the server socket is closed.
			// In this example, this does not happen, but you can do that to gracefully
			// shut down your server.
			//f.channel().closeFuture().sync();
		} catch (e: InterruptedException) {
			e.printStackTrace()
		} finally {
		}
	}

	fun close() {
		//disConnectAllClient()
		if (channelFuture != null) {
			try {
				channelFuture!!.channel().closeFuture().sync()
			} catch (e: InterruptedException) {
				e.printStackTrace()
			}
			channelFuture = null
		}
		workerGroup.shutdownGracefully()
		bossGroup.shutdownGracefully()
	}
}