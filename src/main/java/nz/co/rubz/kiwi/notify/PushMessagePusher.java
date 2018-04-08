package nz.co.rubz.kiwi.notify;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import com.fasterxml.jackson.databind.ObjectMapper;

import nz.co.rubz.kiwi.annotations.Config;
import nz.co.rubz.kiwi.utils.DateUtils;

@Component
public class PushMessagePusher {

	private Logger log = Logger.getLogger(PushMessagePusher.class);


	@Config("classu_apns_queue")
	private String apnsQueueName = "classu:apns:queue";

//	@Config("classu_xinge_queue")
//	private String ippushQueueName = "classu:xinge:queue";


	@Autowired
	private JedisSentinelPool pool;

	private static final String APNS = "APNS";
	private static final String IPPUSH = "IPPUSH";

	private ObjectMapper om = new ObjectMapper();

	public boolean lpushMessageToQueue(String message, String queueName) {

		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			long result = jedis.lpush(queueName, message);
			log.info("lpush " + message + " to Queue " + queueName);
			return result > 0;
		} finally {
			if (jedis != null)
				jedis.close();
		}

	}

	public void sendPushMessage(HashMap<String, Object> pushMessage) {

		try {
			String message = om.writeValueAsString(pushMessage);
//			String msgType = (String) pushMessage.get("msgType");
			lpushMessageToQueue(message, apnsQueueName);
//			if (msgType.equals(APNS)) {
//				lpushMessageToQueue(message, apnsQueueName);
//			}
//			if (msgType.equals(IPPUSH)){
//				lpushMessageToQueue(message, ippushQueueName);
//			}
		} catch (Exception e) {
			log.error("convert map to json failed.", e);
		}
	}

	public HashMap<String, Object> boxingAPNSPushMessage(String certFileName,
			String certPassword, String isProduction, Set<String> deviceToken,
			String paload) {
		HashMap<String, Object> msgMap = new HashMap<String, Object>();
		msgMap.put("msgType", APNS);
		msgMap.put("createTime", DateUtils.getGdadcTimeStamp());
		HashMap<String, String> configMap = new HashMap<String, String>();
		// certFileName certPassword isProduction
		configMap.put("certFileName", certFileName);
		configMap.put("certPassword", certPassword);
		configMap.put("isProduction", isProduction);
		//deviceToken payload
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("deviceToken", deviceToken);
		try {
			String payload =  Base64.encodeBase64String(paload.getBytes("UTF-8"));
			paramMap.put("payload",payload);
			log.info("boxingAPNSPushMessage payload is : " + payload);
		} catch (UnsupportedEncodingException e) {
			 // never goes here
		}
		msgMap.put("msgConfig", configMap);
		msgMap.put("msgContent", paramMap);
		return msgMap;
	}
	
	public HashMap<String, Object> boxingXinGePushMessage(String accessId,
			String secretKey, String title, String paload,Set<String> deviceToken) {
		HashMap<String, Object> msgMap = new HashMap<String, Object>();
		msgMap.put("msgType", IPPUSH);
		msgMap.put("createTime", DateUtils.getGdadcTimeStamp());
		HashMap<String, String> configMap = new HashMap<String, String>();
		configMap.put("accessId", accessId);
		configMap.put("secretKey", secretKey);
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		try {
			String payload =  Base64.encodeBase64String(paload.getBytes("UTF-8"));
			String basedTitle =  Base64.encodeBase64String(title.getBytes("UTF-8"));
			paramMap.put("title", basedTitle);
			paramMap.put("content",payload);
			
			log.info("boxingXinGePushMessage title is : "+ basedTitle +" content is : " + payload);
		} catch (UnsupportedEncodingException e) {
			 // never goes here
		}
		paramMap.put("deviceToken", deviceToken);
		msgMap.put("msgConfig", configMap);
		msgMap.put("msgContent", paramMap);
		return msgMap;
	}

}
