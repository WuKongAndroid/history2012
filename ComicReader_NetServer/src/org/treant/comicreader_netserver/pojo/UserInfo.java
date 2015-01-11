package org.treant.comicreader_netserver.pojo;

/**
 * UserInfo entity. @author MyEclipse Persistence Tools
 */

public class UserInfo implements java.io.Serializable {

	// Fields

	private Integer uid;
	private String uname;
	private String usex;
	private String ubirthday;
	private String ulocation;
	private String uemail;
	private String uhobby;
	private String uadvise;

	// Constructors

	/** default constructor */
	public UserInfo() {
	}

	/** full constructor */
	public UserInfo(String uname, String usex, String ubirthday,
			String ulocation, String uemail, String uhobby, String uadvise) {
		this.uname = uname;
		this.usex = usex;
		this.ubirthday = ubirthday;
		this.ulocation = ulocation;
		this.uemail = uemail;
		this.uhobby = uhobby;
		this.uadvise = uadvise;
	}

	// Property accessors

	public Integer getUid() {
		return this.uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getUname() {
		return this.uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getUsex() {
		return this.usex;
	}

	public void setUsex(String usex) {
		this.usex = usex;
	}

	public String getUbirthday() {
		return this.ubirthday;
	}

	public void setUbirthday(String ubirthday) {
		this.ubirthday = ubirthday;
	}

	public String getUlocation() {
		return this.ulocation;
	}

	public void setUlocation(String ulocation) {
		this.ulocation = ulocation;
	}

	public String getUemail() {
		return this.uemail;
	}

	public void setUemail(String uemail) {
		this.uemail = uemail;
	}

	public String getUhobby() {
		return this.uhobby;
	}

	public void setUhobby(String uhobby) {
		this.uhobby = uhobby;
	}

	public String getUadvise() {
		return this.uadvise;
	}

	public void setUadvise(String uadvise) {
		this.uadvise = uadvise;
	}

}