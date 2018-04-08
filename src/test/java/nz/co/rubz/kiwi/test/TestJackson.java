package nz.co.rubz.kiwi.test;

import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import nz.co.rubz.kiwi.protocol.beans.KiwiMessage;
import nz.co.rubz.kiwi.protocol.beans.Content;

public class TestJackson {

	private static ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
	
	private static String jsonStr = "{\"msg_id\":\"1vafd\",\"from\":\"999991\",\"to\":\"AAAAAA\",\"subject\":\"msg\",\"content\":{\"type\":\"test\",\"data\":{\"length\":\"11\",\"body\":\"hello world\"}},\"createDate\":\"20130227121212\",\"status\":\"0\"}";
	
	private static String jsonStr1 = "{\"msg_id\":\"1vafd\",\"from\":\"999991\",\"to\":\"AAAAAA\",\"subject\":\"msg\",\"content\":{\"type\":\"test\",\"data\":{ \"class_id\":\"000000\",\"author\":\"13910766840\",\"info\":\"xxxxxxxxxxxxxx\",\"attaches\":[{\"attach_type\":\"imge\",\"attach_url\":\"http://xxxx\"},{\"attach_type\":\"audio\",\"attach_url\":\"http://xxxx\"}]}},\"createDate\":\"20130227121212\",\"status\":\"0\"}";
	private static String jsonStr2 = "{\"msg_id\":\"1vafd\",\"from\":\"999991\",\"to\":\"AAAAAA\",\"subject\":\"msg\",\"content\":{\"type\":\"test\",\"data\":{ \"class_id\":\"000000\",\"author\":\"13910766840\",\"info\":\"xxxxxxxxxxxxxx\",\"attaches\":[]}},\"createDate\":\"20130227121212\",\"status\":\"0\"}";

	private static String jsonStr3 = "{\"msg_id\":\"1vafd\",\"from\":\"999991\",\"to\":\"AAAAAA\",\"subject\":\"msg\",\"content\":{\"type\":\"test\",\"data\":{ \"class_id\":\"000000\",\"author\":\"13910766840\",\"info\":\"xxxxxxxxxxxxxx\"}},\"createDate\":\"20130227121212\",\"status\":\"0\"}";

	
	public static void main(String[] args) {
		
		try {
			KiwiMessage r = mapper.readValue(jsonStr2, KiwiMessage.class);
			Content rc = r.getContent();
			System.out.println(rc.getType());
			System.out.println(rc.getData());
			List<HashMap<String,Object>> attaches = (List<HashMap<String, Object>>) rc.getData().get("attaches");
			for(HashMap<String,Object> a : attaches){
				for (String key : a.keySet()){
					System.out.println(a.get(key));
				}
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		HashMap<String, Object> contentMap = new HashMap<String,Object>();
		
		Object a = contentMap.get("a");
		System.out.println((String)a);
		
		long sd=1435632183886L;
        Date dat=new Date(sd);
		   GregorianCalendar gc = new GregorianCalendar(); 
		   gc.setTime(dat);
		   java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		   String sb=format.format(gc.getTime());
		   System.out.println(sb);
		
	}
	
	
}
