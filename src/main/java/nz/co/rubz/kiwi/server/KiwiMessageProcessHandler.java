package nz.co.rubz.kiwi.server;

import java.util.HashMap;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import nz.co.rubz.kiwi.MsgConstants;
import nz.co.rubz.kiwi.ServerConstants;
import nz.co.rubz.kiwi.protocol.beans.KiwiMessage;
import nz.co.rubz.kiwi.protocol.beans.Content;
import nz.co.rubz.kiwi.protocol.converter.ProtocolConverter;
import nz.co.rubz.kiwi.protocol.converter.ResponseContentHelper;
import nz.co.rubz.kiwi.service.IProtocolService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("prototype")
@Component
public class KiwiMessageProcessHandler extends
		SimpleChannelInboundHandler<Object> {

	private Logger log = Logger.getLogger(KiwiMessageProcessHandler.class);

	@Autowired
	private IProtocolService protocolService;

	@Autowired
	private KiwiChannelManager rcManager;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
//		String request = (String) msg;
		try {
//			ClassuMessage message = ProtocolConverter.marshallBasicMsg(request);
			KiwiMessage message = (KiwiMessage) msg;
			rcManager.updateLastAliveTime(ctx.channel().hashCode());
			if (message == null) {
				return;
			}
			String result = "";

			// if(MsgConstants.SYSTEM.equals(message.getSubject())){
			// if(MsgConstants.PONG.equals(message.getContent().getType())){
			// return;
			// }
			// }

			if (ServerConstants.CONNECT.equals(message.getSubject())) {
				message.setContent(ResponseContentHelper
						.genSimpleResponseContentWithDefaultType(
								MsgConstants.ERROR_CODE_0, "connected"));
				result = ResponseHelper.genSyncResponse(message);
				ctx.writeAndFlush(new TextWebSocketFrame(result));
				return;
			}

			// 如果TO== server 则表明为同步操作，否则为异步
			if (ServerConstants.SERVER.equals(message.getTo())) {
				result = handleSyncMessage(ctx, message);
			} else {
				result = handleAsyncMessage(ctx,  message);
			}
			// logger.info("current channel is : " + ctx.channel() +
			// " user use channel is : "+
			// channelsMap.get(message.getFrom()).getChannel());
			// ctx.fireChannelRead(result);
			log.debug("channel -------> " +ctx.channel().hashCode());
			ctx.writeAndFlush(new TextWebSocketFrame(result));

		} catch (Exception e) {
			log.error("handleWebSocketFrame error : ", e);
			e.printStackTrace();
		}

	}

	private String handleAsyncMessage(ChannelHandlerContext ctx,
			 KiwiMessage message) {
		String userId = message.getFrom();
		Integer channelid = ctx.channel().hashCode();
		if (userId != null) {
			if (!rcManager.isTheSameChannel(userId, channelid)) {
				rcManager.mergeRiderChannels(message.getFrom(),ctx.channel());
			}
			// if (protocolService.boardcast(channelid,email,message.getTo(),request)) {
			String request = ProtocolConverter.unmarshallMsg(message);
			if (protocolService.route(userId, message.getTo(), request)) {
				message.setContent(ResponseContentHelper
						.genSimpleResponseContentWithDefaultType(
								MsgConstants.ERROR_CODE_0,
								"message send successed."));
			} else {
				message.setContent(ResponseContentHelper
						.genSimpleResponseContentWithDefaultType(
								MsgConstants.ERROR_CODE_1,
								"message send failed."));
			}
		} else {
			message.setContent(ResponseContentHelper
					.genSimpleResponseContentWithDefaultType(
							MsgConstants.ERROR_CODE_2,
							"please login first!"));
		}
		return ResponseHelper.genAsyncResponse(message);
	}

	private String handleSyncMessage(ChannelHandlerContext ctx, KiwiMessage message) {
		Content resultContent;
		resultContent = protocolService.dispatch(message);
		// 如果是登陆请求，成功后才会加入 channelsMap
		// 如果不是登陆请求，则直接将 from作为 key 加入channelMap,因为客户端会缓存登陆成功的email地址
		// 如果登陆失败，则会删除缓存中的数据，email为null.
		
		// TODO 需要检查一下
		if (ServerConstants.SUBJECT_LOGIN.equalsIgnoreCase(message.getContent().getType())) {
			String resultCode = (String) resultContent.getData().get("result");
			if (Integer.valueOf(resultCode) == 0) {
				HashMap<String,Object> loginData =  resultContent.getData();
				HashMap<String,Object> loginedUser = (HashMap<String, Object>) loginData.get("user");
				String userId = (String) loginedUser.get("user_id");
				rcManager.mergeRiderChannels(userId, ctx.channel());
			}
		} else {
			// 是否需要每次都megre呢
			rcManager.mergeRiderChannels(message.getFrom(),ctx.channel());
		}
		message.setContent(resultContent);
		return ResponseHelper.genSyncResponse(message);
	}

}
