package org.treant.comicreader_netserver.pojo;

import java.sql.Timestamp;

/**
 * BookInfo entity. @author MyEclipse Persistence Tools
 */

public class BookInfo implements java.io.Serializable {

	// Fields

	private String bookId;
	private BookCategory bookCategory;
	private String bookName;
	private String bookContent;
	private Boolean bookIsOver;
	private String bookZipPath;
	private String bookWriter;
	private String countryName;
	private Timestamp updateTime;
	private Float bookGradeNums;
	private Float bookGradeRatio;
	private Integer bookGradeRater;

	// Constructors

	/** default constructor */
	public BookInfo() {
	}

	/** minimal constructor */
	public BookInfo(String bookId, String bookName, Boolean bookIsOver,
			String bookZipPath) {
		this.bookId = bookId;
		this.bookName = bookName;
		this.bookIsOver = bookIsOver;
		this.bookZipPath = bookZipPath;
	}

	/** full constructor */
	public BookInfo(String bookId, BookCategory bookCategory, String bookName,
			String bookContent, Boolean bookIsOver, String bookZipPath,
			String bookWriter, String countryName, Timestamp updateTime,
			Float bookGradeNums, Float bookGradeRatio, Integer bookGradeRater) {
		this.bookId = bookId;
		this.bookCategory = bookCategory;
		this.bookName = bookName;
		this.bookContent = bookContent;
		this.bookIsOver = bookIsOver;
		this.bookZipPath = bookZipPath;
		this.bookWriter = bookWriter;
		this.countryName = countryName;
		this.updateTime = updateTime;
		this.bookGradeNums = bookGradeNums;
		this.bookGradeRatio = bookGradeRatio;
		this.bookGradeRater = bookGradeRater;
	}

	// Property accessors

	public String getBookId() {
		return this.bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public BookCategory getBookCategory() {
		return this.bookCategory;
	}

	public void setBookCategory(BookCategory bookCategory) {
		this.bookCategory = bookCategory;
	}

	public String getBookName() {
		return this.bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getBookContent() {
		return this.bookContent;
	}

	public void setBookContent(String bookContent) {
		this.bookContent = bookContent;
	}

	public Boolean getBookIsOver() {
		return this.bookIsOver;
	}

	public void setBookIsOver(Boolean bookIsOver) {
		this.bookIsOver = bookIsOver;
	}

	public String getBookZipPath() {
		return this.bookZipPath;
	}

	public void setBookZipPath(String bookZipPath) {
		this.bookZipPath = bookZipPath;
	}

	public String getBookWriter() {
		return this.bookWriter;
	}

	public void setBookWriter(String bookWriter) {
		this.bookWriter = bookWriter;
	}

	public String getCountryName() {
		return this.countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public Timestamp getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Float getBookGradeNums() {
		return this.bookGradeNums;
	}

	public void setBookGradeNums(Float bookGradeNums) {
		this.bookGradeNums = bookGradeNums;
	}

	public Float getBookGradeRatio() {
		return this.bookGradeRatio;
	}

	public void setBookGradeRatio(Float bookGradeRatio) {
		this.bookGradeRatio = bookGradeRatio;
	}

	public Integer getBookGradeRater() {
		return this.bookGradeRater;
	}

	public void setBookGradeRater(Integer bookGradeRater) {
		this.bookGradeRater = bookGradeRater;
	}

}