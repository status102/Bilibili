package com.status102.bilibili.broadcast.httpservice

import com.status102.bilibili.PCController
import com.status102.utils.TimeUtils
import io.netty.buffer.ByteBufUtil
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.*
import io.netty.handler.codec.http.HttpMethod.GET
import io.netty.handler.codec.http.HttpResponseStatus.OK


class WebSocketFullHttpRequestHandler : SimpleChannelInboundHandler<FullHttpRequest>() {
	override fun channelRead0(ctx: ChannelHandlerContext, request: FullHttpRequest) {
		if(request.method()?.equals(GET) == true){
			val str = StringBuilder()
			str.append("<html>")
				.append("<head><title>测试页面</title></head>")
				.append("<body>")
				.apply {
					append("<p>页面生成于${TimeUtils.unixStrTime}</p>")
					PCController.userList.forEach {user->
						append("<p>用户名：${user.account.userName}</p>")
						append("<p>状态：${user.account.accountStatusStr}</p>")
						append("<p>每日签到：${user.account.setting.isDailySign}</p>")
						append("<p>风纪投票：${user.account.setting.isJudgement}</p>")
						append("<p>银瓜子宝箱：${user.account.setting.isFreeSilver}</p>")
						append("<p>本页面仅供测试</p>")
					}
					request.headers().forEach {
						append("<p>${it.key}=${it.value}</p>")
					}
				}
				.append("</body></html>")
			val content = Unpooled.copiedBuffer(str.toString(), Charsets.UTF_8)
			sendHttpResponse(ctx, request, DefaultFullHttpResponse(request.protocolVersion, OK, content).also {
				it.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8")
				HttpUtil.setContentLength(it, content.readableBytes().toLong())
			})
		}
	}
	private fun sendHttpResponse(ctx: ChannelHandlerContext, req: FullHttpRequest, res: FullHttpResponse) {
		// Generate an error page if response getStatus code is not OK (200).
		val responseStatus = res.status()
		if (responseStatus.code() != 200) {
			ByteBufUtil.writeUtf8(res.content(), responseStatus.toString())
			HttpUtil.setContentLength(res, res.content().readableBytes().toLong())
		}
		// Send the response and close the connection if necessary.
		val keepAlive = HttpUtil.isKeepAlive(req) && responseStatus.code() == 200
		HttpUtil.setKeepAlive(res, keepAlive)
		val future = ctx.writeAndFlush(res)
		if (!keepAlive) {
			future.addListener(ChannelFutureListener.CLOSE)
		}
	}
}

