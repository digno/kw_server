package nz.co.rubz.kiwi.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import nz.co.rubz.kiwi.MsgConstants;
import nz.co.rubz.kiwi.bean.User;
import nz.co.rubz.kiwi.dao.UserDao;
import nz.co.rubz.kiwi.protocol.beans.KiwiMessage;
import nz.co.rubz.kiwi.protocol.converter.ProtocolConverter;
import nz.co.rubz.kiwi.protocol.converter.ResponseContentHelper;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("prototype")
@Component
public class KiwiMessageBasicCheckHandler extends
		SimpleChannelInboundHandler<Object> {

	private static Logger log = Logger.getLogger(KiwiMessageBasicCheckHandler.class);
	@Autowired
	private UserDao userDao;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) {

		String request = (String) msg;

		String cryptoStr = "";
		KiwiMessage message = ProtocolConverter.marshallBasicMsg(request);
		
		if (message == null ) {
			ctx.writeAndFlush(new TextWebSocketFrame("unrecognized message."));
			return;
		}
		
		String msgId = message.getMsg_id();
		if ("-1".equals(msgId)){
			ctx.fireChannelRead(message);
			return;
		}

		String createDate = message.getCreateDate();
		String userId = message.getFrom();  /// mobile [login,register] // user_id 
		
		User user = userDao.findById(userId);
		if (user != null) {
			String password = user.getPassword();
			cryptoStr = DigestUtils.md5Hex((userId + password + createDate)
					.getBytes());
			log.debug("cryptoStr -> createDate: " +createDate);
			log.debug("cryptoStr -> userId: " +userId);
			log.debug("cryptoStr -> password: " +password);
		}
		log.debug("cryptoStr -> MD5 String : " + cryptoStr );
		if (!cryptoStr.equals(msgId)) {
			message.setSubject("system");
			message.setContent(ResponseContentHelper
					.genSimpleResponseContentWithDefaultType(
							MsgConstants.ILLEGAL_MSG, "Illegal msg_id."));
			String result = ResponseHelper.genSyncResponse(message);
			ctx.writeAndFlush(new TextWebSocketFrame(result));
			return;
		}
		ctx.fireChannelRead(message);
	}

}
