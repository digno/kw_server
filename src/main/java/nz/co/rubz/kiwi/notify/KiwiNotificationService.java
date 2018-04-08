package nz.co.rubz.kiwi.notify;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import nz.co.rubz.kiwi.ConcurrentContext;
import nz.co.rubz.kiwi.jedis.JedisOperator;
import nz.co.rubz.kiwi.protocol.beans.KiwiMessage;
import nz.co.rubz.kiwi.protocol.converter.ProtocolConverter;
import nz.co.rubz.kiwi.server.KiwiChannel;

/**
 * 用户行为通知封装成信件的形式，信件有发件人，收件人，信件内容，投递地址 投递地址：NotificationService
 * 类似于邮局，邮局负责找到收件人的投递地址 在线用户投递：从ClassuChannelManger获取Socket链路，立即投递即可。
 * 离线用户投递：将信件放入Redis的 classu:user:{user_id}:mailbox中完成投递工作。 发件人：通知大部分为系统所发
 * 收件人：由各个Service负责收件人，为List<String> users 信件内容：Context 为信件内容 由各个Service提供
 * 离线用户上线后，首先从信箱里收取信件。
 */

@Component
public class KiwiNotificationService implements ApplicationListener<ContextStartedEvent>{

	private Logger log = Logger.getLogger(KiwiNotificationService.class);


//	@Autowired
//	private MessageServiceImpl messageService;
	
	@Autowired
	private JedisOperator jedis;

	private LinkedBlockingQueue<KiwiMessage> nbox;

//	@PostConstruct
	public void start() {
		nbox = KiwiContext.getNotificationQueue();
		ExecutorService es = Executors.newSingleThreadExecutor();
		for (int i = 0; i <= 5; i++) {
			es.execute(new ClassuNotifictionEmitter());
		}

		log.info("ClassuNotifictionService started.");
	}

	private class ClassuNotifictionEmitter implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					KiwiMessage msg = nbox.take();
					String to = msg.getTo();
					String strMsg = ProtocolConverter.unmarshallMsg(msg);
					KiwiChannel channel = ConcurrentContext.getChannelMapInstance().get(to);
					if (channel != null && channel.isActive()) {
						log.info("channel : [ id = " + channel.getChannelId()
								+ "] send ClassuNotification to " + to
								+ " , Content is " + strMsg);
						channel.getChannel().write(new TextWebSocketFrame(strMsg));
						channel.getChannel().flush();
					} else {
						log.info(to + " is not logon . save notification.");
						jedis.lpushUserNotice(to, strMsg);
					}
					Thread.sleep(50);
				} catch (Exception e) {
					log.error("send notification error .", e);
				}
			}
		}

	}


	@Override
	public void onApplicationEvent(ContextStartedEvent event) {
		start();		
	}

}
