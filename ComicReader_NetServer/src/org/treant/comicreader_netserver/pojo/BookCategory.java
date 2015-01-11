package org.treant.comicreader_netserver.pojo;

import java.util.HashSet;
import java.util.Set;

/**
 * BookCategory entity. @author MyEclipse Persistence Tools
 */

public class BookCategory implements java.io.Serializable {

	// Fields

	private Integer cid;
	private String cname;
	private String ccontent;
	private Set bookInfos = new HashSet(0);

	// Constructors

	/** default constructor */
	public BookCategory() {
	}

	/** minimal constructor */
	public BookCategory(String cname) {
		this.cname = cname;
	}

	/** full constructor */
	public BookCategory(String cname, String ccontent, Set bookInfos) {
		this.cname = cname;
		this.ccontent = ccontent;
		this.bookInfos = bookInfos;
	}

	// Property accessors

	public Integer getCid() {
		return this.cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public String getCname() {
		return this.cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getCcontent() {
		return this.ccontent;
	}

	public void setCcontent(String ccontent) {
		this.ccontent = ccontent;
	}

	public Set getBookInfos() {
		return this.bookInfos;
	}

	public void setBookInfos(Set bookInfos) {
		this.bookInfos = bookInfos;
	}

}