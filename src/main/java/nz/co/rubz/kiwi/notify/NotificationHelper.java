package nz.co.rubz.kiwi.notify;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import nz.co.rubz.kiwi.MsgConstants;
import nz.co.rubz.kiwi.protocol.beans.KiwiMessage;
import nz.co.rubz.kiwi.protocol.beans.Content;
import nz.co.rubz.kiwi.utils.DateUtils;

public class NotificationHelper {

	private static Logger log = Logger.getLogger(NotificationHelper.class);

	private static KiwiMessage genBasicNotification() {
		KiwiMessage message = new KiwiMessage();
		message.setMsg_id(UUID.randomUUID().toString());
		message.setCreateDate(DateUtils.getGdadcTimeStamp());
		message.setStatus(MsgConstants.STATUS);
		message.setVersion(MsgConstants.VERSION);
		message.setFrom(MsgConstants.SERVER);
		return message;
	}

	public static List<KiwiMessage> genNotifications(KiwiNoticeMail mail) {
		List<KiwiMessage> messages = new ArrayList<KiwiMessage>();
		if (CollectionUtils.isNotEmpty(mail.getRecipients())) {
			for (String to : mail.getRecipients()) {
				KiwiMessage message = genBasicNotification();
				message.setTo(to);
				message.setSubject(mail.getNoticeType());
				Content cont = mail.getContent();
				HashMap<String, Object> data = cont.getData();
				data.put("ctime", new Date());
				cont.setData(data);
				message.setContent(cont);
				messages.add(message);
			}
		} else {
			log.info("Recipients is empty , no one need this notice.");
		}
		return messages;
	}


}
