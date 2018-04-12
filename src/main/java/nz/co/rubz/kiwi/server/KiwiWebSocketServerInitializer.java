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

import org.springframework.stereotype.Component;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import nz.co.rubz.kiwi.InitEnvironment;

/**
 */

@Component
public class KiwiWebSocketServerInitializer extends
		ChannelInitializer<SocketChannel> {

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		
		EventLoopGroup bizGroup = new NioEventLoopGroup();
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("codec-http", new HttpServerCodec());
		pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
		pipeline.addLast(
				"handler",
				InitEnvironment.getAppclicatContextInstance().getBean(
						KiwiWebSocketServerHandler.class));
		pipeline.addLast(
				"auth-handler",
				InitEnvironment.getAppclicatContextInstance().getBean(
						KiwiMessageBasicCheckHandler.class));

		pipeline.addLast(
				"biz-handler",
				InitEnvironment.getAppclicatContextInstance().getBean(
						KiwiMessageProcessHandler.class));
		
		// 以这种方式实现业务线程池，更优雅
//		pipeline.addLast(bizGroup, "biz-handler",
//				InitEnvironment.getAppclicatContextInstance().getBean(
//						ClassuMessageProcessHandler.class));
	}

}
