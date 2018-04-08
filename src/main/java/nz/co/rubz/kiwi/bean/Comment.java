package nz.co.rubz.kiwi.bean;

import java.util.Date;
import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;

@Entity(noClassnameStored = true)
public class Comment extends BaseEntity {


	/**
	 * 
	 */
	private static final long serialVersionUID = -4071556277350898681L;

	@Property("noti_id")
	private String notiId;

	private String relay;

	@Property("relay_to")
	private String relayTo;

	private String comment;
	
	@Property("class_id")
	private List<String> classIds;

	private Date ctime;


	public List<String> getClassIds() {
		return classIds;
	}

	public void setClassIds(List<String> classIds) {
		this.classIds = classIds;
	}

	public String getNotiId() {
		return notiId;
	}

	public void setNotiId(String notiId) {
		this.notiId = notiId;
	}

	public String getRelay() {
		return relay;
	}

	public void setRelay(String relay) {
		this.relay = relay;
	}

	public String getRelayTo() {
		return relayTo;
	}

	public void setRelayTo(String relayTo) {
		this.relayTo = relayTo;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

}
