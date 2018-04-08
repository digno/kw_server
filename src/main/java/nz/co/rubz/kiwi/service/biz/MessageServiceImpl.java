package nz.co.rubz.kiwi.service.biz;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nz.co.rubz.kiwi.dao.MessageDao;
import nz.co.rubz.kiwi.protocol.beans.KiwiMessage;
import nz.co.rubz.kiwi.protocol.converter.ProtocolConverter;


@Service
public class MessageServiceImpl {

	private Logger log = Logger.getLogger(MessageServiceImpl.class);
	
 
	@Autowired
	private MessageDao messageDao; 
	
	
 

	public void saveUndeliverMessage(String mobile, String message) {
		KiwiMessage baseMessage = ProtocolConverter.marshallBasicMsg(message);
		messageDao.saveMessage(mobile, baseMessage);
	}
	
	public void saveUndeliverMessage(KiwiMessage message) {
		messageDao.saveMessage(message);
	}
	
	public void saveDeliveredMessage(String mobile, String message) {
		KiwiMessage baseMessage = ProtocolConverter.marshallBasicMsg(message);
		messageDao.saveMessage(mobile, baseMessage);
	}
	
	
	
}
