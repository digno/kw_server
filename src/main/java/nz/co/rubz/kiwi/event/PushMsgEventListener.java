package nz.co.rubz.kiwi.event;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import nz.co.rubz.kiwi.annotations.Config;
import nz.co.rubz.kiwi.notify.KiwiApnsMessage;
import nz.co.rubz.kiwi.notify.KiwiIPPushMessage;
import nz.co.rubz.kiwi.notify.KiwiPushMessage;
import nz.co.rubz.kiwi.notify.PushMessagePusher;

@Component
public class PushMsgEventListener implements ApplicationListener<PushMsgEvent> {

	private Logger log = Logger.getLogger(PushMsgEventListener.class);

	@Config("apns_cert_file_name")
	private String certFileName;
	@Config("apns_cert_password")
	private String certPassword;
	@Config("is_production")
	private String isProduction;
	
	@Config("xinge_access_id")
	private String accessId="2100163718";
	
	@Config("xinge_secret_key")
	private String secretKey="f0a0d98eba529a8f3508748f381e4120";

	@Autowired
	private PushMessagePusher messagePusher;

	// 同步通知
	@Override
	public void onApplicationEvent(PushMsgEvent event) {
		try {
			KiwiPushMessage mail = (KiwiPushMessage) event.getSource();
			if (mail instanceof KiwiApnsMessage){
				KiwiApnsMessage msg = (KiwiApnsMessage) mail;
				HashMap<String ,Object> msgData = messagePusher.boxingAPNSPushMessage(certFileName, certPassword,
						isProduction, mail.getDeviceTokens(), msg.getPayload());
				messagePusher.sendPushMessage(msgData);
			}
			
			if (mail instanceof KiwiIPPushMessage){
				KiwiIPPushMessage msg = (KiwiIPPushMessage) mail;
				HashMap<String ,Object> msgData = messagePusher.boxingXinGePushMessage(accessId, secretKey,
					 msg.getTitle(), msg.getContent(),mail.getDeviceTokens());
				messagePusher.sendPushMessage(msgData);
			}
			
		} catch (Exception e) {
			log.info("notificationQueue full.", e);
		}

	}

}
