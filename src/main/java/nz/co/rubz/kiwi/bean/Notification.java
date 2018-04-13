package nz.co.rubz.kiwi.bean;

import java.util.Date;
import java.util.List;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;

@Entity(noClassnameStored = true)
@Deprecated
public class Notification extends BaseEntity implements Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Property("class_name")
	private List<String> classNames;
	@Property("owner_name")
	private String ownerName;

	@Property("class_id")
	private List<String> classIds;
	
	private String owner;

	private Date ctime;
	
	private String actiontTime;

	private String content;

	@Embedded("attaches")
	private List<Attach> attaches;
	
	@Property("target_id")
	private List<String> targetIds;
	@Property("target_name")
	private List<String> targetNames;

	@Property("type")
	private String type; 
	
	@Property("noti_type")
	private String notiType;
	
	
	public List<String> getClassNames() {
		return classNames;
	}

	public void setClassNames(List<String> classNames) {
		this.classNames = classNames;
	}

	public List<String> getClassIds() {
		return classIds;
	}

	public void setClassIds(List<String> classIds) {
		this.classIds = classIds;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	
	
	public String getActiontTime() {
		return actiontTime;
	}

	public void setActiontTime(String actiontTime) {
		this.actiontTime = actiontTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Attach> getAttaches() {
		return attaches;
	}

	public void setAttaches(List<Attach> attaches) {
		this.attaches = attaches;
	}


	public List<String> getTargetIds() {
		return targetIds;
	}

	public void setTargetIds(List<String> targetIds) {
		this.targetIds = targetIds;
	}

	public List<String> getTargetNames() {
		return targetNames;
	}

	public void setTargetNames(List<String> targetNames) {
		this.targetNames = targetNames;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	
	public String getNotiType() {
		return notiType;
	}

	public void setNotiType(String notiType) {
		this.notiType = notiType;
	}

	

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
