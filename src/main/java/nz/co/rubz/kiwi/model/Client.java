package nz.co.rubz.kiwi.model;

import java.util.Date;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;

@Entity(noClassnameStored = true)
public class Client extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Property("user_id")
	private String userId;

	@Property("phone_type")
	private String phoneType;

	@Property("device_id")
	private String deviceId;

	@Property("device_token")
	private String deviceToken;

	@Property("client_name")
	private String clientName;

	@Property("client_ver")
	private String clientVer;

	@Property("report_time")
	private Date reportTime;

	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientVer() {
		return clientVer;
	}

	public void setClientVer(String clientVer) {
		this.clientVer = clientVer;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

}
