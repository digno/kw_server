package nz.co.rubz.kiwi.bean;

import java.util.Date;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.utils.IndexDirection;

@Entity(noClassnameStored = true)
public class User extends BaseEntity{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;

	@Indexed(value=IndexDirection.ASC, name="idx_user_mobile", unique=true, dropDups=true)  
	private String mobile;

	
	private String gender;

	private String password;
	
	private String email;
	
	private Date ctime;

	private String type;
	
	private String pushflag;
	
	private String smsflag;
	
	private String emailflag;

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPushflag() {
		return pushflag;
	}

	public void setPushflag(String pushflag) {
		this.pushflag = pushflag;
	}

	public String getSmsflag() {
		return smsflag;
	}

	public void setSmsflag(String smsflag) {
		this.smsflag = smsflag;
	}

	public String getEmailflag() {
		return emailflag;
	}

	public void setEmailflag(String emailflag) {
		this.emailflag = emailflag;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
}
