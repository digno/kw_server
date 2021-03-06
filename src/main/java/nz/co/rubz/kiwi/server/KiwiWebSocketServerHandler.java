/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package nz.co.rubz.kiwi.server;

import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.setContentLength;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaders.Names.HOST;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 此类是处理连接和消息路由的核心类，在这里需要处理如下消息
 * 1：处理握手消息
 * 2：处理业务消息
 * 3：管理客户端连接
 * 		3.1：一个客户端只有一条链路
 * 		3.2：收发异步消息的链路必须有所属者
 * 		
 * Handles handshakes and messages
 */

@Scope("prototype")
@Component
public class KiwiWebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
	private static final Logger logger = Logger
			.getLogger(KiwiWebSocketServerHandler.class);
	

	private static final String WEBSOCKET_PATH = "/websocket";

	private WebSocketServerHandshaker handshaker;


	
	@Autowired
	private KiwiChannelManager rcManager;

	@Override
	public void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		
		if (msg instanceof FullHttpRequest) {
			handleHttpRequest(ctx, (FullHttpRequest) msg);
		} else if (msg instanceof WebSocketFrame) {
			handleWebSocketFrame(ctx, (WebSocketFrame) msg);
		}
	}
	
	
	private void handleHttpRequest(ChannelHandlerContext ctx,
			FullHttpRequest req) throws Exception {
		// Handle a bad request.
		if (!req.getDecoderResult().isSuccess()) {
			sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1,
					BAD_REQUEST));
			return;
		}

		// Allow only GET methods.
		if (req.getMethod() != GET) {
			sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1,
					FORBIDDEN));
			return;
		}

		// Send the demo page and favicon.ico
		if ("/".equals(req.getUri())) {
			ByteBuf content = KiwiWebSocketServerIndexPage
					.getContent(getWebSocketLocation(req));
			FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, OK,
					content);

			res.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
			setContentLength(res, content.readableBytes());
			logger.info("response content " + res.content().toString());
			sendHttpResponse(ctx, req, res);
			return;
		}
		if ("/favicon.ico".equals(req.getUri())) {
			FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1,
					NOT_FOUND);
			sendHttpResponse(ctx, req, res);
			return;
		}

		// Handshake
		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
				getWebSocketLocation(req), null, false);
		handshaker = wsFactory.newHandshaker(req);
		if (handshaker == null) {
			WebSocketServerHandshakerFactory
					.sendUnsupportedWebSocketVersionResponse(ctx.channel());
		} else {
			logger.debug("received client handshake frame .");
			handshaker.handshake(ctx.channel(), req);
			rcManager.registerClassuChannel(ctx.channel());
		}
	}

	
	
	
	private void handleWebSocketFrame(ChannelHandlerContext ctx,
			WebSocketFrame frame) {
		logger.debug("received WebSocketFrame from "  + ctx.channel().hashCode());
		logger.debug("frame instance is " + frame.toString());
		// Check for closing frame
		if (frame instanceof CloseWebSocketFrame) {
			frame.retain();
			handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame);
			logger.info("received CloseWebSocketFrame");
			rcManager.closeClassuChannel(ctx.channel().hashCode());
			return;
		}
		if (frame instanceof PingWebSocketFrame) {
			frame.content().retain();
			logger.info("received PingWebSocketFrame from "  + ctx.channel().hashCode());
			rcManager.updateLastAliveTime(ctx.channel().hashCode());
			ctx.channel().write(new PongWebSocketFrame(frame.content()));
			return;
		}
		if (!(frame instanceof TextWebSocketFrame)) {
			throw new UnsupportedOperationException(String.format(
					"%s frame types not supported", frame.getClass().getName()));
		}
		
		// Send the uppercase string back.
		String request = ((TextWebSocketFrame) frame).text();
		if (logger.isInfoEnabled()) {
			logger.info(String.format("Channel %s received %s", ctx.channel()
					.hashCode(), request));
		}
		// ctx.channel().write(new TextWebSocketFrame(request.toUpperCase()));
		ctx.fireChannelRead(request);
	}

	private static void sendHttpResponse(ChannelHandlerContext ctx,
			FullHttpRequest req, FullHttpResponse res) {
		// Generate an error page if response getStatus code is not OK (200).
		if (res.getStatus().code() != 200) {
			res.content().writeBytes(
					Unpooled.copiedBuffer(res.getStatus().toString(),
							CharsetUtil.UTF_8));
			setContentLength(res, res.content().readableBytes());
		}

		// Send the response and close the connection if necessary.
		ChannelFuture f = ctx.channel().write(res);
		ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
		if (!isKeepAlive(req) || res.getStatus().code() != 200) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	private static String getWebSocketLocation(FullHttpRequest req) {
		return "ws://" + req.headers().get(HOST) + WEBSOCKET_PATH;
	}
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// 客户端断开连接的处理。
		// 在此将优化ClassuChannelManager
		super.channelInactive(ctx);
	}
	
}
