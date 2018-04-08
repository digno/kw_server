package nz.co.rubz.kiwi.server;

import java.io.IOException;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import nz.co.rubz.kiwi.protocol.beans.KiwiMessage;
import nz.co.rubz.kiwi.utils.DateUtils;

public class ResponseHelper {

	private static Logger log = Logger.getLogger(ResponseHelper.class);

	private static ObjectMapper mapper = new ObjectMapper();// can reuse, share globally
	
	// 异步消息无需转化 FROM TO
	public static String genAsyncResponse(KiwiMessage message) {
		String result = "";
		configMapper();

		try {
//			long timeStamp = System.currentTimeMillis();
			message.setMsg_id(UUID.randomUUID().toString());
			message.setCreateDate(DateUtils.getGdadcTimeStamp());
			result = mapper.writeValueAsString(message);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		log.info("Async response to client : " + result);
		return result;
	}

	// 同步消息需要将FROM TO 信息互换
	public static String genSyncResponse(KiwiMessage message) {
		configMapper();
		KiwiMessage newMessage = swapFromTo(message);
		String result = "";
		try {
//			long timeStamp = System.currentTimeMillis();
			newMessage.setMsg_id(UUID.randomUUID().toString());
			newMessage.setCreateDate(DateUtils.getGdadcTimeStamp());
			result = mapper.writeValueAsString(newMessage);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		log.info("Sync response to client : " + result);
		return result;
	}
	
	private static void configMapper(){
		mapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>()  
		        {  
		            @Override  
		            public void serialize(  
		                    Object value,  
		                    JsonGenerator jg,  
		                    SerializerProvider sp) throws IOException, JsonProcessingException  
		            {  
		                jg.writeString("");  
		            }  
		        });  
	}
	
	private static KiwiMessage swapFromTo(KiwiMessage message){
		String from = message.getFrom();
		String to = message.getTo();
		KiwiMessage swapedMsg = message;
		swapedMsg.setFrom(to);
		swapedMsg.setTo(from);
		return swapedMsg;
	}
	

}
