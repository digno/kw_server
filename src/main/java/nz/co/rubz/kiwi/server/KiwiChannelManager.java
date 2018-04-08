package nz.co.rubz.kiwi.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import nz.co.rubz.kiwi.ConcurrentContext;
import nz.co.rubz.kiwi.MsgConstants;
import nz.co.rubz.kiwi.annotations.Config;
import nz.co.rubz.kiwi.bean.User;
import nz.co.rubz.kiwi.dao.UserDao;
import nz.co.rubz.kiwi.jedis.JedisOperator;
import nz.co.rubz.kiwi.notify.KiwiContext;
import nz.co.rubz.kiwi.protocol.beans.KiwiMessage;
import nz.co.rubz.kiwi.protocol.beans.Content;
import nz.co.rubz.kiwi.protocol.converter.ProtocolConverter;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

//firefox 在每次请求后回自动关闭上个websocket连接，而chrom则不会
// 因此在开发期间，无论同步异步消息均需要merge连接
// 问题1：有一部分同步消息，没有email(用户无需登录)，如果请求中from == null,则用户无法收到聊天信息的提示
// 问题2：但是如果每次都megre 连接会导致2个 CurrentHashMap的比对，如果连接太多，会影响性能

@Component
public class KiwiChannelManager implements ApplicationListener<ContextStartedEvent>{

	private Logger log = Logger.getLogger(KiwiChannelManager.class);

	@Config("idel_peroid")
	private int idelPeroid = 300;

	@Autowired
	private JedisOperator jedis;
	
	@Autowired
	private UserDao userDao;

	// 登陆后的链路信息key为用户的 user_id
	private ConcurrentHashMap<String, KiwiChannel> channelsMap = ConcurrentContext
			.getChannelMapInstance();

	// 所有的链路集合 key 为channelid
	private ConcurrentHashMap<Integer, KiwiChannel> cMap = ConcurrentContext
			.getAvailableChannelMapInstance();

	/**
	 * channelsMap(user_id,classuChannel),cMap(channel_id,classuChannel) 如果服务器收到
	 * close 的textFrame 1：关闭channel 2：删除cMap中的channel 3: 删除channelMap 中的channel
	 * 4: 更新 reids 中lastloginTime
	 * 
	 * 如果服务器收到业务处理消息 1：from为空 2：不做任何处理，此时连接数量上cMap>channelsMap 3：from不为空
	 * 4：检查channelsMap 中key 为from 的连接是否与 cMap中的相同 if (channelMap.get(email) ==
	 * cMap.get(channelMap.get(email).getChannelId()));
	 * 5：如果不同，channelMap.replace(email,channel)
	 * 
	 * 
	 */

	public synchronized void closeClassuChannel(Integer channelId) {
		long start = System.currentTimeMillis();
		KiwiChannel rc = cMap.get(channelId);
		for (String userId : channelsMap.keySet()) {
			KiwiChannel temp = channelsMap.get(userId);
			if (channelId.intValue() == temp.getChannelId().intValue()) {
				channelsMap.remove(userId);
				log.info("remove logined channel [ " + userId + " ].");
				jedis.setLastLogoutTime(userId);
				break;
			}
		}
		if (rc != null) {
			ChannelFuture cf = rc.getChannel().close();
			log.info("Close Channel Status [close:"+cf.isSuccess()+"] [done:"+cf.isDone()+"]");
			if (!cf.isSuccess()){
				log.error("*****close is not successed.*****", cf.cause());;
			}
		} else {
			log.error(" ***** CAN NOT FOUND CHANNEL BY ID ["+channelId+"] *****");
		}
		cMap.remove(channelId);
		long end = System.currentTimeMillis();
		log.info("closeClassuChannel used " + (end - start) + " ms.");
	}

	public synchronized void registerClassuChannel(Channel channel) {
		KiwiChannel classuChannel = new KiwiChannel();
		classuChannel.setChannelId(channel.hashCode());
		classuChannel.setChannel(channel);
		cMap.put(channel.hashCode(), classuChannel);
	}

	/**
	 * 将客户端的websocket连接与用户的user_id对应起来 如果连接channel与 缓存中user_id对应的channel 不同
	 * 则关闭缓存中的 channel 并将新的channel更新至缓存
	 * 
	 * @param userId
	 * @param channel
	 */
	public synchronized void mergeRiderChannels(String userId, Channel channel) {
		long start = System.currentTimeMillis();
		if (StringUtils.isBlank(userId) || "null".equals(userId)) {
			log.warn("userid is null .");
			// logChannels();
			return;
		}
		KiwiChannel rc = cMap.get(channel.hashCode());
		Integer nowChannelId = rc.getChannelId();
		KiwiChannel oldChannel = channelsMap.get(userId);
		if (oldChannel != null) {
			Integer oldChannelId = oldChannel.getChannelId();
			if (nowChannelId.intValue() != oldChannelId.intValue()) {
				cMap.remove(oldChannelId);
				channelsMap.replace(userId, rc).getChannel().close();
				log.warn(oldChannelId + " Channel for " + userId + " meraged! ");
			}
		} else {
			channelsMap.put(userId, rc);
			sendNotices(userId);
			
		}
		long end = System.currentTimeMillis();
		log.info("mergeClassuChannels used " + (end - start) + " ms.");
		logChannels();

	}
	
	public synchronized void checkRiderChannels(String userId, Channel channel) {
		long start = System.currentTimeMillis();
		if (StringUtils.isBlank(userId) || "null".equals(userId)) {
			log.warn("userid is null .");
			// logChannels();
			return;
		}
		KiwiChannel rc = cMap.get(channel.hashCode());
		Integer nowChannelId = rc.getChannelId();
		KiwiChannel oldChannel = channelsMap.get(userId);
		if (oldChannel != null) {
			Integer oldChannelId = oldChannel.getChannelId();
			if (nowChannelId.intValue() != oldChannelId.intValue()) {
				cMap.remove(oldChannelId);
				channelsMap.replace(userId, rc).getChannel().close();
				log.warn(oldChannelId + " Channel for " + userId + " meraged! ");
			}
		} else {
			channelsMap.put(userId, rc);
			sendNotices(userId);
			
		}
		long end = System.currentTimeMillis();
		log.info("mergeClassuChannels used " + (end - start) + " ms.");
		logChannels();

	}

	private void sendNotices(String userId){
		
		User user = userDao.findById(userId);
		if (user!=null){
		String aa = user.getId().toHexString();
		log.info("send notice on user "+userId+" logon .");
		for (int i= 0;i<jedis.llenUserNotice(aa);i++){
			String msg = jedis.rpopUserNotice(aa);
			log.info("offline notice is : "+msg);
			KiwiMessage m = ProtocolConverter.marshallBasicMsg(msg);
			try {
				KiwiContext.getNotificationQueue().put(m);
			} catch (InterruptedException e) {
				log.info("sendNotices error !",e);
			}
		}}
			
	}
	
	
	public synchronized void updateLastAliveTime(Integer channel_id) {
		KiwiChannel rc = cMap.get(channel_id);
		if (rc != null) {
			rc.setLastAliveTime(System.currentTimeMillis());
		}
	}

	public boolean isTheSameChannel(String userId, Integer channelid) {
		// 如果 email 为空则返回用户需要登陆
		return channelsMap.get(userId) == cMap.get(channelid);
	}

	public void checkClassuChannel() {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						// 3个5分钟
						long checkPeriod = idelPeroid * 1000; //
						for (Integer channelid : cMap.keySet()) {
							KiwiChannel rc = cMap.get(channelid);
							// 如果链接不可用，立即关闭
							if (!isValidChannel(rc.getChannel())) {
								closeClassuChannel(channelid);
							}
							// 如果长时间闲置则关闭
							if ((System.currentTimeMillis() - rc
									.getLastAliveTime()) > checkPeriod * 3) {
								log.info((System.currentTimeMillis() - rc.getLastAliveTime())/1000 );
								closeClassuChannel(channelid);
							}
						}
						for (String userId : channelsMap.keySet()){
							KiwiChannel rc = channelsMap.get(userId);
							if (!isValidChannel(rc.getChannel())) {
								closeClassuChannel(rc.getChannelId());
							}
						}
						logChannels();
						Thread.sleep(checkPeriod);
					} catch (Exception e) {
						e.printStackTrace();
						log.error("check classuChannel error !");
					}
				}
			}
		};
		Thread checkThread = new Thread(r);
		checkThread.start();
	}
	
	private boolean isValidChannel(Channel channel){
		if (channel == null) {
			log.warn("channel is null .");
			return false;
		}
		if (!channel.isActive() || !channel.isWritable()) {
			log.warn("channel channel is not activie or writable");
			return false;
		}
		
		return true;
	}

	@Deprecated
	private boolean sendPingMsg(Channel channel) {
		if (!channel.isActive() || !channel.isWritable()) {
			log.info("channel is null , channel is not activie or writable");
			return false;
		}
		KiwiMessage message = new KiwiMessage();
		message.setMsg_id("-1");
		message.setFrom(MsgConstants.SERVER);
		message.setTo(MsgConstants.CLIENT);
		message.setSubject(MsgConstants.SYSTEM);
		Content content = new Content();
		content.setType(MsgConstants.PING);
		content.setData(new HashMap<String, Object>());
		message.setContent(content);
		message.setCreateDate(Long.toString(System.currentTimeMillis()));
		message.setVersion(MsgConstants.VERSION);
		String msgStr = ProtocolConverter.unmarshallMsg(message);
		ChannelFuture cf = channel
				.writeAndFlush(new TextWebSocketFrame(msgStr));
	
		return cf.isSuccess();

	}

	// Channel 状态变化过程 open -> register -> active
	// isWritable @see http://netty.io/4.0/api/io/netty/buffer/ByteBuf.html
	// Returns true if and only if this buffer has enough room to allow writing
	// the specified number of elements.
	private void logChannels() {
		log.info("total online user is " + channelsMap.size() + " channel size is "
				+ cMap.size());
		if (log.isDebugEnabled()){
		log.info("------------ channelsMap -----------------");
		for (String e : channelsMap.keySet()) {
			KiwiChannel r = channelsMap.get(e);
			log.info("        " + e + " [" + r.getChannelId() + "] : " + r
					+ " isActiveChannel status : " + r.getChannel().isActive());
		}
		log.info("--------------- cMap --------------------");
		for (Integer e : cMap.keySet()) {
			KiwiChannel r = cMap.get(e);
			log.info("        " + e + " : " + cMap.get(e) + " isActiveChannel status : "
					+ r.getChannel().isActive());
		}
		}
	}


	@Override
	public void onApplicationEvent(ContextStartedEvent event) {
		checkClassuChannel();
	}

}
