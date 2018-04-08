package nz.co.rubz.kiwi.bean;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

@Embedded
public class Attach implements Serializable {

	private static final long serialVersionUID = 1L;
	@Property("attach_type")
	private String attachType;

	@Property("attach_url")
	private String attachUrl;

	@Property("pic_width")
	private String picWidth;
	@Property("pic_height")
	private String picHeight;
	
	public String getAttachType() {
		return attachType;
	}

	public void setAttachType(String attachType) {
		this.attachType = attachType;
	}

	public String getAttachUrl() {
		return attachUrl;
	}

	public void setAttachUrl(String attachUrl) {
		this.attachUrl = attachUrl;
	}
	public String getPicWidth() {
		return picWidth;
	}

	public void setPicWidth(String picWidth) {
		this.picWidth = picWidth;
	}

	public String getPicHeight() {
		return picHeight;
	}

	public void setPicHeight(String picHeight) {
		this.picHeight = picHeight;
	}
}
