package nz.co.rubz.kiwi.protocol.converter;

import java.util.HashMap;

import nz.co.rubz.kiwi.MsgConstants;
import nz.co.rubz.kiwi.protocol.beans.Content;

public class ResponseContentHelper {

	public static Content genSimpleResponseContentWithoutType(String result,String msg){
		Content rc =new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(MsgConstants.RESULT, result);
		resultMap.put(MsgConstants.MSG, msg);
		rc.setData(resultMap);
		return rc;
	}
	
	public static Content genSimpleResponseContentWithDefaultType(String result,String msg){
		Content rc =new Content();
		rc.setType(MsgConstants.SERVER);
//		rc.setType(MsgConstants.CHAT);
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(MsgConstants.RESULT, result);
		resultMap.put(MsgConstants.MSG, msg);
		rc.setData(resultMap);
		return rc;
	}
	
}
