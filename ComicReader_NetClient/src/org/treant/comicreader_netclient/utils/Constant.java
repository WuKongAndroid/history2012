package org.treant.comicreader_netclient.utils;

import java.io.File;

public class Constant {
	//New Of My
	public static final String REQUEST_ROOT="http://10.0.2.2:8080/ComicReader_NetServer/bookActions!";
	public static final String REQUEST_ALLBOOKCATEGORY_URL=REQUEST_ROOT+"toAllBookCategory";
	public static final String REQUEST_ALLBOOKINFO_URL=REQUEST_ROOT+"toAllBookInfo";
	public static final String REQUEST_SPECIALLIST_URL = REQUEST_ROOT+"toSpecialBookInfo";
	public static final String REQUEST_RECOMMEND_URL=REQUEST_ROOT+"toRecommendBookInfo";
	public static final String REQUEST_RANKLIST_URL=REQUEST_ROOT+"toRankListBookInfo";
	public static final String REQUEST_TYPE_BASE_URL=REQUEST_ROOT+"toTypeBookInfo?bookInfo.bookCategory.cname=";
	public static final String REQUEST_SEARCHName_URL=REQUEST_ROOT+"toSearchBookInfoByName?bookInfo.bookName=";
	public static final String REQUEST_SEARCHWriter_URL=REQUEST_ROOT+"toSearchBookInfoByWriter?bookInfo.bookWriter=";
	public static final String REQUEST_SEARCHContent_URL=REQUEST_ROOT+"toSearchBookInfoByContent?bookInfo.bookContent=";

	public static final String REQUEST_IMAGE_RES_ROOT_URL="http://10.0.2.2:8080/ComicReader_NetServer/IMAGE-DATA/";
	public static final String REQUEST_ZIP_RES_ROOT_URL="http://10.0.2.2:8080/ComicReader_NetServer/ZIP-DATA/";
	public static final String REQUEST_SEARCHBOOKINFO_BYID=REQUEST_ROOT+"toSearchBookInfoById?bookInfo.bookId=";
	public static final String REQUEST_UPDATEBOOKINFO_BYID=REQUEST_ROOT+"toUpdateBookInfoById";
	
	public static final String REQUEST_ADVISE_URL="http://10.0.2.2:8080/ComicReader_NetServer/userActions!toAddUserInfo";
	
	public static final String SEPARATOR=File.separator;
	public static final String ROOT_PATH=Utils.getSDCardRoot()+SEPARATOR+"ComicReader"+SEPARATOR;
	public static final String CACHE_PATH=ROOT_PATH+"Cache"+SEPARATOR;
	public static final String INCOMING_PATH=ROOT_PATH+"Incoming"+SEPARATOR;
	public static final String TEMP_PATH=ROOT_PATH+"Temp"+SEPARATOR;
	public static final String CONFIG_PATH=ROOT_PATH+"config"+SEPARATOR;
	public static final String COLLECTION_PATH=CONFIG_PATH+"collection.xml";
	public static final String RECENTLYREAD_PATH=CONFIG_PATH+"recentlyread.xml";
	public static final String BOOKINFO_CACHE_PATH=CACHE_PATH+"currentBookInfo.xml";
	
	public static final String COMPRESS_DIR_PATH=INCOMING_PATH+"Compress"+SEPARATOR;
	public static final String DECOMPRESS_DIR_PATH=INCOMING_PATH+"Decompress"+SEPARATOR;
	
//	// 保存在shareParefence中的数据信息
//	public static final String BROWSER_POSITION = "position";
//	public static final String BROWSER_PICPATH = "picPath";
//	public static final String BROWSER_BOOKID = "bookId";
//	public static final String BROWSER_BOOKGRADE = "bookGrade";
//	public static final String BROWSER_BOOKGRADENUMS = "bookGradeNums";
//	public static final String BROWSER_TOTALPAGE = "totalPage";
//	public static final String BROWSER_TIME = "browserTime";
//	public static final String BROWSER_UNZIPBOOKPATH = "unzipBookPath"; // shareParefence中的存储的信息名
//	public static final String SHARE_FILES = "bookinfo";
//	// shareParefence的读写模式 私有的
//	public static final int SHARE_MODEL = Context.MODE_PRIVATE
//			+ Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE;

}
