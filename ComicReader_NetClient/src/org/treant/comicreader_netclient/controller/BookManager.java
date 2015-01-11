package org.treant.comicreader_netclient.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.treant.comicreader_netclient.R;
import org.treant.comicreader_netclient.activity.BookTypeActivity;
import org.treant.comicreader_netclient.activity.HomeActivity;
import org.treant.comicreader_netclient.pojo.BookCategory;
import org.treant.comicreader_netclient.pojo.BookInfo;
import org.treant.comicreader_netclient.utils.Constant;
import org.treant.comicreader_netclient.utils.Notice;
import org.treant.comicreader_netclient.utils.Utils;
import org.treant.comicreader_netclient.utils.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class BookManager {
	private Context context;
	private Intent intent;
	private Bundle bundle;
	public String tabFlag = null;

	public BookManager(Context context) {
		this.context = context;
	}

	/**
	 * 由url得到以List<Map<String,String>>形式封装后的BookInfo
	 */
	public List<Map<String, String>> getBookList(String urlPath) {
		List<Map<String,String>> all=new ArrayList<Map<String,String>>();
		List<BookInfo> books=Utils.getBookInfoData(urlPath);
		if(books!=null&&books.size()>0){
			int flag=0;
			for(BookInfo bookInfo:books){
				Map<String,String>map=new HashMap<String,String>();
				map.put("bookCover", Constant.REQUEST_IMAGE_RES_ROOT_URL+bookInfo.getBookId()+".jpg");
				map.put("bookName", bookInfo.getBookName());
				map.put("updateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(bookInfo.getUpdateTime()));
				
				map.put("bookContent", bookInfo.getBookContent());
				map.put("bookZipPath", bookInfo.getBookZipPath());
				
				map.put("bookId", bookInfo.getBookId());
				map.put("bookWriter", bookInfo.getBookWriter());
				map.put("countryName", bookInfo.getCountryName());
				
				map.put("bookGradeNums", String.valueOf(bookInfo.getBookGradeNums()));
				map.put("bookGradeRatio", String.valueOf(bookInfo.getBookGradeRatio()));
				map.put("bookGradeRater", String.valueOf(bookInfo.getBookGradeRater()));
//				map.put("cid", String.valueOf(bookInfo.getBookCategory().getCid()));
				map.put("bookCategory", bookInfo.getBookCategory().getCname());
				map.put("bookIsOver", String.valueOf(bookInfo.getBookIsOver()));
				Log.i("category", bookInfo.getBookCategory().getCname());
				//show various windows
				if(flag==0){
					map.put("book_title", context.getString(R.string.book_title_zd));
					map.put("book_more", "more_zd");
				}
				if(flag==1){
					map.put("book_title", context.getString(R.string.book_title_tj));
					map.put("book_more", "more_tj");
				}
				if(flag==4){
					map.put("book_title", context.getString(R.string.book_title_ph));
					map.put("book_more", "more_ph");
				}
				flag++;
				all.add(map);
			}
		}	
		//第一次下载后就把所有BookInfo信息serialize到Cache下的xml
		if(!new File(Constant.BOOKINFO_CACHE_PATH).exists()&&all.size()>0){
			Utils.serializeBookInfoAsXml(all,Constant.CACHE_PATH, Constant.BOOKINFO_CACHE_PATH);
			Log.i("CacheWork", "缓存了全部");
		}
		return all;
	}
	/**
	 *  获取指定recommend.order.home的BookInfo
	 * @param urlPath
	 * @param type
	 * @param bookType
	 * @return
	 */
	public static List<Map<String, String>> getBookInfoListByType(String urlPath,
			String type, String bookType) {
		List<Map<String, String>> list=new ArrayList<Map<String, String>>();
		List<BookInfo>bookInfos=Utils.getBookInfoData(urlPath);
		if(bookInfos!=null&&bookInfos.size()>0){
			int flag=0;   //对headerView进行特殊标记  
			for(BookInfo bookInfo:bookInfos){
				Map<String,String>map=new HashMap<String,String>();
				if(flag==0){
					map.put("topTitle", type);
					map.put("bookType", bookType);
				}
				map.put("bookCover", Constant.REQUEST_IMAGE_RES_ROOT_URL+bookInfo.getBookId()+".jpg");
				map.put("bookName", bookInfo.getBookName());
				map.put("updateTime", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(bookInfo.getUpdateTime()));
				//bookGrade
				map.put("bookContent", bookInfo.getBookContent());
				map.put("bookZipPath", bookInfo.getBookZipPath());
				map.put("bookId", bookInfo.getBookId());
				map.put("bookWriter", bookInfo.getBookWriter());
				map.put("countryName", bookInfo.getCountryName());
				map.put("bookGradeNums", String.valueOf(bookInfo.getBookGradeNums()));
				map.put("bookGradeRatio", String.valueOf(bookInfo.getBookGradeRatio()));
				map.put("bookGradeRater", String.valueOf(bookInfo.getBookGradeRater()));
				map.put("bookCategory", bookInfo.getBookCategory().getCname());
				map.put("bookIsOver", String.valueOf(bookInfo.getBookIsOver()));
		//		map.put("cid", String.valueOf(bookInfo.getBookCategory().getCid()));
				
				flag++;
				list.add(map);
			}
		}	
		return list;
	}
	/**
	 * 获取所有BookCategory
	 * @param urlPath
	 * @return
	 */
	public static List<Map<String,String>> getBookCategoryList(String urlPath){
		List<Map<String,String>> all=new ArrayList<Map<String,String>>();
		List<BookCategory>bookCategories=Utils.getBookCategoryData(urlPath);
		if(bookCategories!=null&&bookCategories.size()>0){
			for(BookCategory bookCategory:bookCategories){
				Map<String,String>map=new HashMap<String,String>();
				map.put("cid", String.valueOf(bookCategory.getCid()));
				map.put("cname", bookCategory.getCname());
				map.put("ccontent", bookCategory.getCcontent());
				all.add(map);
			}
		}
		return all;
	}
	/**
	 * 通过类别获取到本期主打 的图书信息
	 */
	public List<Map<String, String>> getBookSpecialList(String filePath) {
		return null;

	}

	/**
	 * 头部导航视图
	 * 
	 * @param string
	 * @return
	 */
	public View buildHeader(String tab) {
		View header = null;
		LayoutInflater inflater = LayoutInflater.from(context);
		header = (View) inflater.inflate(R.layout.layout_header, null);
		header.setFocusable(true);
		// First Page
		ViewHolder.home = (ImageButton) header.findViewById(R.id.home);
		ViewHolder.home.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(context, HomeActivity.class);
				bundle = new Bundle();
				bundle.putString("bookType", "home");
				intent.putExtras(bundle);
				context.startActivity(intent);
			}

		});
		// Change ImageButton Background
		if (tab.equals("home")) {
			ViewHolder.home.setBackgroundResource(R.drawable.tab_home_white);
		}
		// Recommend
		ViewHolder.recommend = (ImageButton) header.findViewById(R.id.recommend);
		ViewHolder.recommend.setOnClickListener(new ImageButton.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						intent = new Intent(context, HomeActivity.class);
						bundle = new Bundle();
						bundle.putString("bookType", "recommend");
						intent.putExtras(bundle);
						context.startActivity(intent);
					}

				});
		if (tab.equals("recommend")) {
			ViewHolder.recommend.setBackgroundResource(R.drawable.tab_recommend_white);
		}
		// Order
		ViewHolder.order = (ImageButton) header.findViewById(R.id.order);
		ViewHolder.order.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(context, HomeActivity.class);
				bundle = new Bundle();
				bundle.putString("bookType", "order");
				intent.putExtras(bundle);
				context.startActivity(intent);
			}

		});
		if (tab.equals("order")) {
			ViewHolder.order.setBackgroundResource(R.drawable.tab_order_white);
		}
		// Type
		ViewHolder.type = (ImageButton) header.findViewById(R.id.type);
		ViewHolder.type.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(context, BookTypeActivity.class);
				bundle = new Bundle();
				bundle.putString("bookType", "type");
				intent.putExtras(bundle);
				context.startActivity(intent);
			}

		});
		if (tab.equals("type")) {
			ViewHolder.type.setBackgroundResource(R.drawable.tab_type_white);
		}
		// Notice and Advertisement Info
		String notice = "最新通知:拯救世界,和谐至上...广告招租正在进行...欢迎有道之士前来捧场...For The Lich King...";
		ViewHolder.news = (TextView) header.findViewById(R.id.news);
		ViewHolder.news.setText(notice);
		return header;
	}
	/**
	 * 获取通知或广告信息
	 */
	public String getNoticeNews(String filePath) {
		String content = "";
		List<Notice> notices = Utils.getNoticeList();
		if (notices != null && notices.size() > 0) {
			for (Notice notice : notices) {
				content += notice.getTitle();
				content += "" + notice.getCreateTime();
			}
		}
		return content;
	}

}
