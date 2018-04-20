package nz.co.rubz.kiwi.model;

import java.util.Date;
import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexed;

import nz.co.rubz.kiwi.model.attrs.Address;
import nz.co.rubz.kiwi.model.attrs.Creditcard;

@Entity(noClassnameStored = true)
public class KiwiUser extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String name;

	@Indexed(options = @IndexOptions(name = "idx_user_mobile", unique = true))
	private String mobile;

	private String gender;

	private String password;

	private String email;

	private Date ctime;

	private String type;

	private String avatar;
	
	private String pushflag;

	private List<Creditcard> creditcards;

	private List<Address> addrs;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public List<Creditcard> getCreditcards() {
		return creditcards;
	}

	public void setCreditcards(List<Creditcard> creditcards) {
		this.creditcards = creditcards;
	}

	public List<Address> getAddrs() {
		return addrs;
	}

	public void setAddrs(List<Address> addrs) {
		this.addrs = addrs;
	}

	public String getPushflag() {
		return pushflag;
	}

	public void setPushflag(String pushflag) {
		this.pushflag = pushflag;
	}

	@Override
	public String toString() {
		return "KiwiUser [name=" + name + ", mobile=" + mobile + ", gender=" + gender + ", password=" + password
				+ ", email=" + email + ", ctime=" + ctime + ", type=" + type + ", avatar=" + avatar + ", pushflag="
				+ pushflag + ", creditcards=" + creditcards + ", addrs=" + addrs + "]";
	}

	

}
