package nz.co.rubz.kiwi.protocol.converter;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import nz.co.rubz.kiwi.protocol.beans.KiwiMessage;
import nz.co.rubz.kiwi.protocol.beans.Content;
import nz.co.rubz.kiwi.utils.ReflectionUtils;

public class ProtocolConverter {

	private static Logger log = Logger.getLogger(ProtocolConverter.class);

	// can reuse, share globally
	private static ObjectMapper mapper = new ObjectMapper();

	public static KiwiMessage marshallBasicMsg(String requestStr) {
		try {
			KiwiMessage msg = mapper.readValue(requestStr,KiwiMessage.class);
			return msg;
		} catch (Exception e) {
			log.error("marshall request string to WhereMessage error !" ,e );
		}
		return null;
	}
	
	

	public static String unmarshallMsg(KiwiMessage message){
		try {
			return mapper.writeValueAsString(message);
		} catch (Exception e) {
			log.error("unmarshall WhereMessage to response string error ! " ,e);
		}
		return null;
	}

	public static String unmarshallMsgComplicated(KiwiMessage message) {
		StringWriter writer = new StringWriter();
		JsonGenerator gen = null;
		try {
			gen = new JsonFactory().createGenerator(writer);
			mapper.writeValue(gen, message);
			String json = writer.toString();
			return json;
		} catch (Exception e) {
			log.error("unmarshallMsg error!");
			e.printStackTrace();
		} finally {
			try {
				gen.close();
				writer.close();
			} catch (Exception e) {
				log.error("WTF , close gen&writer error!");
			}
		}
		return null;
	}

	public static HashMap<String , Object> convertWhereMessageToMap(KiwiMessage message){
		HashMap<String,Object> map = new HashMap<String,Object>();
		List<String> fieldNames = ReflectionUtils.getDeclaredFieldNames(KiwiMessage.class);
		for(String key : fieldNames){
			Object value = ReflectionUtils.getFieldValue(message, key);
			if(key.equals("content")){
				HashMap<String,Object> contentMap = new HashMap<String,Object>();
				List<String> contentFieldNames = ReflectionUtils.getDeclaredFieldNames(Content.class);
				for(String contentKey : contentFieldNames){
					contentMap.put(contentKey, ReflectionUtils.getFieldValue(value, contentKey));
				}
				value = contentMap;
			}
			map.put(key, value);
		}
		return map.isEmpty() ?  null : map ;
	}
	
	
}
