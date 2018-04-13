package nz.co.rubz.kiwi.bean;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

@Embedded
public class PromiseDelivery {

	/**
	 * 承诺送达时间,分钟
	 */
	@Property("promise_delivery_time")
	private String promiseDeliveryTime;
	/**
	 * 是否参加, 参考 是否参与准时达
	 */
	@Property("platform_compensate")
	private String platformCompensate;

	public String getPromiseDeliveryTime() {
		return promiseDeliveryTime;
	}

	public void setPromiseDeliveryTime(String promiseDeliveryTime) {
		this.promiseDeliveryTime = promiseDeliveryTime;
	}

	public String getPlatformCompensate() {
		return platformCompensate;
	}

	public void setPlatformCompensate(String platformCompensate) {
		this.platformCompensate = platformCompensate;
	}

}
