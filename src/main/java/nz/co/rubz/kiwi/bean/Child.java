package nz.co.rubz.kiwi.bean;

import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;

@Entity(noClassnameStored=true)
@Deprecated
public class Child extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Property("child_name")
	private String childName;
	
	@Property("child_photo")
	private String childPhoto;
	
	private List<Parent> parents;

	public String getChildName() {
		return childName;
	}

	public void setChildName(String childName) {
		this.childName = childName;
	}

	public String getChildPhoto() {
		return childPhoto;
	}

	public void setChildPhoto(String childPhoto) {
		this.childPhoto = childPhoto;
	}

	public List<Parent> getParents() {
		return parents;
	}

	public void setParents(List<Parent> parents) {
		this.parents = parents;
	}
	
	
	
}
