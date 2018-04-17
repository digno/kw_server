package nz.co.rubz.kiwi.apns;

import java.util.HashMap;

/**
 * Base PUSH Message
 *
 */
public class PushMessage {

	private String createTime;

	private String msgType;

	private HashMap<String, Object> msgContent;
	private HashMap<String, String> msgConfig;

	public HashMap<String, String> getMsgConfig() {
		return msgConfig;
	}

	public void setMsgConfig(HashMap<String, String> msgConfig) {
		this.msgConfig = msgConfig;
	}

	public HashMap<String, Object> getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(HashMap<String, Object> msgContent) {
		this.msgContent = msgContent;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

}
