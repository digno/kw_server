package nz.co.rubz.kiwi.bean;

import org.mongodb.morphia.annotations.Embedded;



@Embedded
@Deprecated
public class Parent {
	
	
	private String pid;
	
	private String mobile;
	
	private String relation;
	
	private  String name;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}


	
	
}
