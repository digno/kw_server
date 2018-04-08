package nz.co.rubz.kiwi.bean;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

@Embedded
public class ClassuMember implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 注意此处仅仅为了避免发通知时的N+1查询，所设置的冗余字段
	// 一定不可以在老师获取班级列表时返回给老师
	@Property("user_id")
	private String userId;
	@Property("child_photo")
	private String childPhoto;

	@Property("child_name")
	private String childName;
	
	@Property("child_id")
	private String childId;

	private String relation;

	

	public String getChildPhoto() {
		return childPhoto;
	}

	public void setChildPhoto(String childPhoto) {
		this.childPhoto = childPhoto;
	}

	public String getChildName() {
		return childName;
	}

	public void setChildName(String childName) {
		this.childName = childName;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getChildId() {
		return childId;
	}

	public void setChildId(String childId) {
		this.childId = childId;
	}

}
