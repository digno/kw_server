package nz.co.rubz.kiwi.notify;

import java.util.Set;

public class KiwiPushMessage {
	private Set<String> deviceTokens;

	private String pushType;
	 

	public Set<String> getDeviceTokens() {
		return deviceTokens;
	}

	public void setDeviceTokens(Set<String> deviceTokens) {
		this.deviceTokens = deviceTokens;
	}

	public String getPushType() {
		return pushType;
	}

	public void setPushType(String pushType) {
		this.pushType = pushType;
	}

	
	
}
