package nz.co.rubz.kiwi.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;

import nz.co.rubz.kiwi.model.BaseEntity;

@Entity(noClassnameStored = true)
@Deprecated
public class Classu extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Property("class_name")
	private String className;
	
	@Property("class_id")
	private String classId;
	
	@Property("owner")
	private String owner;
	
	@Property("owner_name")
	private String ownerName;
	
	@Property("class_mail")
	private String classMail;
	
	@Property("ctime")
	private Date ctime;
	
	@Property("content")
	private String content;
	
	@Embedded("members")  
	private List<ClassuMember> members = new ArrayList<ClassuMember>();

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<ClassuMember> getMembers() {
		return members;
	}

	public void setMembers(List<ClassuMember> members) {
		this.members = members;
	}

	public String getClassMail() {
		return classMail;
	}

	public void setClassMail(String classMail) {
		this.classMail = classMail;
	}

	
	
	
	

}
