package nz.co.rubz.kiwi.notify;

public class KiwiApnsMessage extends KiwiPushMessage {
	private String payload;

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}
}
