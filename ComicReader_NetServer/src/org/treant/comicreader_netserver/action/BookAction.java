package org.treant.comicreader_netserver.action;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.treant.comicreader_netserver.dao.BookCategoryDao;
import org.treant.comicreader_netserver.dao.BookInfoDao;
import org.treant.comicreader_netserver.pojo.BookCategory;
import org.treant.comicreader_netserver.pojo.BookInfo;

import com.opensymphony.xwork2.ActionSupport;

public class BookAction extends ActionSupport{
	private HttpServletResponse response;
	private List<BookCategory> bookCategoryList;
	private List<BookInfo> bookInfoList;
	private BookInfo bookInfo;
//	private float bookGradeNums;
//	private int bookGradeRater;
	public List<BookCategory> getBookCategoryList() {
		return this.bookCategoryList;
	}

	public void setBookCategoryList(List<BookCategory> bookCategoryList) {
		this.bookCategoryList = bookCategoryList;
	}

	public List<BookInfo> getBookInfoList() {
		return this.bookInfoList;
	}

	public void setBookInfoList(List<BookInfo> bookInfoList) {
		this.bookInfoList = bookInfoList;
	}

	public BookInfo getBookInfo() {
		return bookInfo;
	}

	public void setBookInfo(BookInfo bookInfo) {
		this.bookInfo = bookInfo;
	}
	
	
//	public float getBookGradeNums() {
//		return bookGradeNums;
//	}
//
//	public void setBookGradeNums(float bookGradeNums) {
//		this.bookGradeNums = bookGradeNums;
//	}
//
//	public int getBookGradeRater() {
//		return bookGradeRater;
//	}
//
//	public void setBookGradeRater(int bookGradeRater) {
//		this.bookGradeRater = bookGradeRater;
//	}

	// /////////////////////////////////////////////////
	public String toAllBookCategory() {
		bookCategoryList = BookCategoryDao.getInstance().getAllBookCategory();
		response = ServletActionContext.getResponse();
		response.setCharacterEncoding("gbk");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			StringBuffer sb = new StringBuffer();
			sb.append("{\"AllBookCategory\":[");
			for (BookCategory item : bookCategoryList) {
				sb.append("{\"cid\":");
				sb.append(item.getCid() + ",");
				sb.append("\"cname\":");
				sb.append("\"" + item.getCname() + "\",");
				sb.append("\"ccontent\":");
				sb.append("\"" + item.getCcontent() + "\"},");
				// sb.append("\"containsBooInfoCounts\":");
				// sb.append("\"" + item.getBookInfos().size() + "\"},");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("]}");
			writer.print(sb.toString());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String toAllBookInfo() {
		bookInfoList = BookInfoDao.getInstance().getAllBookInfo();
		this.getJsonData(bookInfoList);
		return null;
	}

	public void toSpecialBookInfo() {
		bookInfoList = BookInfoDao.getInstance().getSpecialBookInfo();
		this.getJsonData(bookInfoList);
	}

	public void toRecommendBookInfo() {
		bookInfoList = BookInfoDao.getInstance().getRecommendBookInfo();
		this.getJsonData(bookInfoList);
	}

	public String toRankListBookInfo() {
		bookInfoList = BookInfoDao.getInstance().getRankListBookInfo();
		this.getJsonData(bookInfoList);
		return null;
	}

	// Get BookInfo By bookInfo.bookCatetory.cname
	// new String(***.getBytes("ISO-8859-1),"gbk")
	// new String(***.getBytes(Charset.forName("ISO8859-1")),"gbk")
	public String toTypeBookInfo() {
		try {
			bookInfoList = BookInfoDao.getInstance().getBookInfoByCategoryName(
					new String(bookInfo.getBookCategory().getCname().getBytes(
							Charset.forName("ISO8859-1")), "gbk"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			System.out.println(new String(bookInfo.getBookCategory().getCname().getBytes(
					Charset.forName("ISO8859-1")), "gbk"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.getJsonData(bookInfoList);
		return null;
	}
	// Search By Id
	public String toSearchBookInfoById() {
		BookInfo b=BookInfoDao.getInstance().getUniqueBookInfoById(bookInfo.getBookId());
		bookInfoList=new ArrayList<BookInfo>();
		bookInfoList.add(b);
		this.getJsonData(bookInfoList);
		return null;
	}
	// Update By Id
	public String toUpdateBookInfoById(){
		BookInfoDao.getInstance().updateBookInfoById(bookInfo.getBookId(), 
				bookInfo.getBookGradeNums(), bookInfo.getBookGradeRater());
		System.out.println(bookInfo.getBookId()+"->"+
				bookInfo.getBookGradeNums()+"->"+bookInfo.getBookGradeRater());
		return null;
	}
	// Search By Name
	public String toSearchBookInfoByName() {
		System.out.println("Action中源码是:" + bookInfo.getBookName());
		try {
			System.out.println("Action中关键字重编码后:"
					+ new String(bookInfo.getBookName().getBytes("ISO8859-1"),
							"gbk"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			bookInfoList = BookInfoDao.getInstance().getBookInfoByName(
					new String(bookInfo.getBookName().getBytes("ISO8859-1"),
							"gbk"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.getJsonData(bookInfoList);
		return null;
	}

	// Search By Writer
	public String toSearchBookInfoByWriter() {
		try {
			bookInfoList = BookInfoDao.getInstance().getBookInfoByWriter(
					new String(bookInfo.getBookWriter().getBytes("ISO8859-1"),
							"gbk"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.getJsonData(bookInfoList);
		return null;
	}

	// Search By Content
	public String toSearchBookInfoByContent() {
		try {
			bookInfoList = BookInfoDao.getInstance().getBookInfoByContent(
					new String(bookInfo.getBookContent().getBytes("ISO8859-1"),
							"gbk"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.getJsonData(bookInfoList);
		return null;
	}

	// {"AllBookInfo",[{"**":**,"**",**},{},{}]} /////////////////
	public void getJsonData(List<BookInfo> bookInfoList) {
		response = ServletActionContext.getResponse();
		response.setCharacterEncoding("gbk");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (bookInfoList != null && bookInfoList.size() > 0) {
			StringBuffer sb = new StringBuffer();
			sb.append("{" + "\"AllBookInfo\":[");
			for (int i = 0; i < bookInfoList.size(); i++) {
				BookInfo item = bookInfoList.get(i);
				sb.append("{\"bookId\":");
				sb.append("\"" + item.getBookId() + "\",");

				sb.append("\"bookName\":");
				sb.append("\"" + item.getBookName() + "\",");

				sb.append("\"bookContent\":");
				sb.append("\"" + item.getBookContent() + "\",");

				sb.append("\"bookIsOver\":");
				sb.append(item.getBookIsOver() + ",");

				sb.append("\"bookZipPath\":");
				sb.append("\"" + item.getBookZipPath() + "\",");

				sb.append("\"bookWriter\":");
				sb.append("\"" + item.getBookWriter() + "\",");

				sb.append("\"countryName\":");
				sb.append("\"" + item.getCountryName() + "\",");

				sb.append("\"updateTime\":");
				sb.append("\""
						+ new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
								.format(item.getUpdateTime()) + "\",");

				sb.append("\"bookGradeNums\":");
				sb.append(item.getBookGradeNums() + ",");

				sb.append("\"bookGradeRatio\":");
				sb.append(item.getBookGradeRatio() + ",");

				sb.append("\"bookGradeRater\":");
				sb.append(item.getBookGradeRater() + ",");

				sb.append("\"cid\":");
				sb.append(item.getBookCategory().getCid() + ",");

				sb.append("\"cname\":");
				sb.append("\"" + item.getBookCategory().getCname() + "\"},");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("]}");
			writer.print(sb.toString());
		}
		writer.close();
	}
}
