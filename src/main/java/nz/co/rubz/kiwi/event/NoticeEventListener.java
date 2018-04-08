package nz.co.rubz.kiwi.event;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import nz.co.rubz.kiwi.notify.KiwiContext;
import nz.co.rubz.kiwi.notify.KiwiNoticeMail;
import nz.co.rubz.kiwi.notify.NotificationHelper;
import nz.co.rubz.kiwi.protocol.beans.KiwiMessage;
import nz.co.rubz.kiwi.protocol.converter.ProtocolConverter;

@Component
public class NoticeEventListener implements ApplicationListener<NoticeEvent> {

	private Logger log = Logger.getLogger(NoticeEventListener.class);

	// 同步通知
	@Override
	public void onApplicationEvent(NoticeEvent event) {
		try {
			KiwiNoticeMail mail = (KiwiNoticeMail) event.getSource();
			List<KiwiMessage> messages = NotificationHelper.genNotifications(mail);
			
			for (KiwiMessage m : messages) {
				KiwiContext.getNotificationQueue().put(m);
				log.info("pub notice [ " + ProtocolConverter.unmarshallMsg(m) +"] ");
			}

		} catch (Exception e) {
			log.info("notificationQueue full." ,e );
		}

	}

}
