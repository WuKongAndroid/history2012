package org.treant.comicreader_netclient.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.json.JSONArray;
import org.json.JSONObject;
import org.treant.comicreader_netclient.R;
import org.treant.comicreader_netclient.pojo.BookCategory;
import org.treant.comicreader_netclient.pojo.BookInfo;
import org.treant.comicreader_netclient.utils.AsyncDownloadImage.AsyncMissionCallBack;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Utils {
	
	public static void showToastMsg(Context context, int stringId) {
		Toast toast=Toast.makeText(context, context.getResources().getString(stringId),Toast.LENGTH_SHORT);
		LinearLayout layout=(LinearLayout)toast.getView();
		layout.setGravity(Gravity.CENTER_HORIZONTAL);
		ImageView image=new ImageView(context);
		List<Integer> imageList=loadDrawableByReflect("dota");
		image.setImageResource(imageList.get(new Random().nextInt(imageList.size())));
		LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layout.addView(image, 1, params);
		toast.setView(layout);
		toast.show();
	}
	public static String getCurrentTime(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
	public static String getSDCardRoot(){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		}else
			return null;
	}
	public static List<Notice> getNoticeList() {
		// TODO Auto-generated method stub
		return null;
	}
/**
 * 把List<BookInfo> serialize 到 XML
 * @param list
 * @param savePath
 */
	public static void serializeBookInfoAsXml(List<Map<String,String>> list,String parentPath, String savePath){
		try {
			File pFile=new File(parentPath);
			File cacheFile=new File(savePath);
			if(!pFile.exists()){
				pFile.mkdirs();
				if(!cacheFile.exists()){
					cacheFile.createNewFile();
				}
			}
			OutputStream os=new FileOutputStream(cacheFile);
			if(list!=null&&list.size()!=0){
				XmlSerializer xmlSerializer=Xml.newSerializer();
				xmlSerializer.setOutput(os, "UTF-8");
				xmlSerializer.startDocument("UTF-8", true);
				xmlSerializer.startTag(null, "BookCollection");
				for(Map<String,String> map:list){
					xmlSerializer.startTag(null, "book");
					Set<Map.Entry<String,String>>set=map.entrySet();
					for (Map.Entry<String, String> entry:set) {
					 //	sb.append(entry.getKey()+"="+URLEncoder.encode(entry.getValue(),enconding)+"&");
						xmlSerializer.startTag(null, entry.getKey());
						xmlSerializer.text(entry.getValue());
						xmlSerializer.endTag(null, entry.getKey());
					}
					//用for循环优化  减少代码量
					xmlSerializer.endTag(null, "book");
				}
				xmlSerializer.endTag(null, "BookCollection");
				xmlSerializer.endDocument();
			}
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	public static void serializeRecentlyReadAsXml(List<Map<String,String>>list){
//
//		try {
//			File file=new File(Constant.RECENTLYREAD_PATH);
//			if(!file.exists()){
//				file.createNewFile();
//			}
//			OutputStream os=new FileOutputStream(file);
//			if(list!=null&&list.size()!=0){
//				XmlSerializer xmlSerializer=Xml.newSerializer();
//				xmlSerializer.setOutput(os, "UTF-8");
//				xmlSerializer.startDocument("UTF-8", true);
//				xmlSerializer.startTag(null, "BookCollection");
//				for(Map<String,String> map:list){
//					xmlSerializer.startTag(null, "book");
//					Set<Map.Entry<String, String>>set=map.entrySet();
//					for(Map.Entry<String, String>entry:set){
//						xmlSerializer.startTag(null, entry.getKey());
//						xmlSerializer.text(entry.getValue());
//						xmlSerializer.endTag(null, entry.getKey());
//					}		
//					xmlSerializer.endTag(null, "book");
//				}
//				xmlSerializer.endTag(null, "BookCollection");
//				xmlSerializer.endDocument();
//			}
//			os.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	
//	}
	/**
	 * 由URL路径得到以List<BookInfo>形式封装的相关BookInfo集合
	 * @param urlPath
	 * @return
	 */
	public static List<BookInfo> getBookInfoData(String urlPath) {
		List<BookInfo> bookInfoList = new ArrayList<BookInfo>();
		String jsonData=getJsonDataByUrl(urlPath);
		if(jsonData!=null&&jsonData.length()>0){
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				JSONArray jsonArray = jsonObject.getJSONArray("AllBookInfo");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObjItem = jsonArray.getJSONObject(i);
					BookInfo bookInfo = new BookInfo();
					bookInfo.setBookId(jsonObjItem.getString("bookId"));
					bookInfo.setBookName(jsonObjItem.getString("bookName"));
					bookInfo.setBookContent(jsonObjItem.getString("bookContent"));
					bookInfo.setBookIsOver(Boolean.valueOf(jsonObjItem.getString("bookIsOver")));
					bookInfo.setBookZipPath(jsonObjItem.getString("bookZipPath"));
					bookInfo.setBookWriter(jsonObjItem.getString("bookWriter"));
					bookInfo.setCountryName(jsonObjItem.getString("countryName"));
					bookInfo.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").
							parse(jsonObjItem.getString("updateTime")));
					bookInfo.setBookGradeNums(Float.parseFloat(jsonObjItem
							.getString("bookGradeNums")));
					bookInfo.setBookGradeRatio(Float.parseFloat(jsonObjItem
							.getString("bookGradeRatio")));
					bookInfo.setBookGradeRater(Integer.parseInt(jsonObjItem
							.getString("bookGradeRater")));
					BookCategory bookCategory = new BookCategory();
					bookCategory.setCid(Integer.parseInt(jsonObjItem
							.getString("cid")));
					bookCategory.setCname(jsonObjItem.getString("cname"));
					bookInfo.setBookCategory(bookCategory);
					bookInfoList.add(bookInfo);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return bookInfoList;
	}
	/**
	 * 由URL路径得到BookCategory的所有数据
	 * @param urlPath
	 * @return
	 */
	public static List<BookCategory> getBookCategoryData(String urlPath) {
		List<BookCategory> bookCategoryList=new ArrayList<BookCategory>();
		String jsonData=getJsonDataByUrl(urlPath);
		try {
			JSONObject jsonObject=new JSONObject(jsonData);
			JSONArray jsonArray=(JSONArray) jsonObject.get("AllBookCategory");  //注意是.get方法 向下转换类型
			for(int i=0;i<jsonArray.length();i++){
				JSONObject jsonItem=jsonArray.getJSONObject(i);
				BookCategory bookCategory=new BookCategory();
				bookCategory.setCid(Integer.parseInt(jsonItem.getString("cid")));
				bookCategory.setCname(jsonItem.getString("cname"));
				bookCategory.setCcontent(jsonItem.getString("ccontent"));
				bookCategoryList.add(bookCategory);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bookCategoryList;
	}
	/**
	 * 根据url得到Json
	 * @param urlPath
	 * @return
	 */
	public static String getJsonDataByUrl(String urlPath){
		StringBuilder sb=new StringBuilder();
		try {
			URL url = new URL(urlPath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setReadTimeout(5 * 1000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			InputStream is = conn.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "gbk"));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			is.close();
			conn.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
	/**
	 * 根据URL下载压缩包,并指定解压路径及名称
	 * 
	 * @param zipUrl
	 * @param uncompressDir
	 * @param zipFileName
	 */
//	public static boolean downloadZipByUrl(String zipUrl, String uncompressDir, String zipFileName) {
//		boolean flag=false;
//		try {
//			URL url = new URL(zipUrl);
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			conn.setRequestMethod("POST");
//			conn.setDoInput(true);
//			InputStream is = conn.getInputStream();
//			if (!new File(uncompressDir).exists()) {
//				if (new File(uncompressDir).mkdirs()) {
//					Log.i("ok", "create success...");
//				}
//			}
//			File zipFile = new File(uncompressDir,zipFileName + ".zip");
//			OutputStream os = new FileOutputStream(zipFile);
//			byte[] b = new byte[4096];
//			int len = 0;
//			while ((len = is.read(b)) != -1) {
//				os.write(b, 0, len);
//			}
//			os.flush();
//			os.close();
//			is.close();
//			if (conn.getResponseCode() == 200) {
//				Log.i("Zip", "download-Zip-ok");
//				flag=true;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return flag;
//	}
	/**
	 * 解压缩
	 * @param zipFilePath 源zip文件路径
	 * @param unZipDirPath 解压后所在的文件夹路径  解压后与原zip文件同名
	 */
	public static void upZip(String zipFilePath, String unZipDirPath) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			try {//Be careful  Import the Apache zipFile class
				ZipFile zipFile = new ZipFile(zipFilePath,"gbk");
				@SuppressWarnings("unchecked")
				Enumeration<ZipEntry> enumeration = zipFile.getEntries();
				while(enumeration.hasMoreElements()){
					ZipEntry zipEntry=(ZipEntry) enumeration.nextElement();
					String temp=zipEntry.getName();
					Log.i("entryName", temp);
					File aimFile=new File(unZipDirPath, temp);
					if(zipEntry.isDirectory()){
						aimFile.mkdirs();
					}else{
						InputStream is=zipFile.getInputStream(zipEntry);
						File parent=aimFile.getParentFile();
						if(!parent.exists()){
							parent.mkdirs();
						}
						OutputStream os=new FileOutputStream(aimFile);
						byte[] b=new byte[4096];
						int len;
						while((len=is.read(b))!=-1){
							os.write(b, 0, len);
						}
						os.flush();
						os.close();
						is.close();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 获取枚举类Enumeration
		// 遍历Enumeration
		// 得到当前文件的名字 zipEntry.getName()
		// 判断被压缩文件是否为文件夹
		// 由ZipEntry获取输入流
		// 创建目标保存文件对象
		// 判断父路径是否存在
		// 关闭流
	}
	/**
	 * Pull解析Xml 统计某个Xml文件中某个标签下某值出现的次数
	 * @param xmlPath
	 * @param tagName
	 * @param keyWords
	 * @return
	 */
	public static int getBooInfoCountsByAttribute(String xmlPath,String tagName,String keyWords){
		File file=new File(xmlPath);
		int count=0;
		if(file.exists()&&file.isFile()&&file.getName().toLowerCase().endsWith(".xml")){
			try {
				XmlPullParser parser=Xml.newPullParser();
				InputStream is=new FileInputStream(file);
				parser.setInput(is, "UTF-8");
				int eventType=parser.getEventType();
				while(eventType!=XmlPullParser.END_DOCUMENT){
					switch(eventType){
					case XmlPullParser.START_TAG:
						if(parser.getName().equals(tagName)){
							if(parser.nextText().equals(keyWords)){
								count++;
							}
						}
					break;
					}
					eventType=parser.next();
				}
				is.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return count;
	}
	/**
	 * 反射动态加载Drawable ArrayList
	 * @param startStr
	 * @return
	 */
	public static List<Integer> loadDrawableByReflect(String startStr){
		List<Integer>list=new ArrayList<Integer>();
		Field[] fields=R.drawable.class.getDeclaredFields();
		for(int i=0;i<fields.length;i++){
			if(fields[i].getName().startsWith(startStr)){
				try {
					int imageId=fields[i].getInt(R.drawable.class);
					list.add(imageId);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	/**
	 * 从指定路径Pull解析xml为List<Map<String,String>>
	 * @param xmlPath
	 * @return
	 */
	public static List<Map<String, String>> parseRelatedBookInfoFromXml(String xmlPath) {
		File file = new File(xmlPath);
		if (!file.exists()) {
			Log.i("newList", "为解析xml生成List<Map>新建了file");
			return new ArrayList<Map<String,String>>();
		}
		List<Map<String, String>> all = null;
		Map<String, String> map = null;
		XmlPullParser parser = Xml.newPullParser();
		try {
			InputStream is=new FileInputStream(file);
			parser.setInput(is, "UTF-8");
			int eventType=parser.getEventType();
			while(eventType!=XmlPullParser.END_DOCUMENT){
				switch(eventType){
				case XmlPullParser.START_DOCUMENT:
					all=new ArrayList<Map<String,String>>();
					break;
				case XmlPullParser.START_TAG:
					String tag=parser.getName();
					if(tag.equals("book")){
						map=new HashMap<String,String>();
					}else{
						if(map!=null){
							map.put(parser.getName(), parser.nextText());
						}
					}
					break;
				case XmlPullParser.END_TAG:
					if(parser.getName().equals("book")){
						all.add(map);
						map=null;
					}
					break;
				}
				eventType=parser.next();
			}
			is.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return all;
	}	
	/**
	 * 调用异步加载
	 * @param parent
	 * @param id
	 * @param url
	 */
	public static void asyncLoadDrawable(final View parent,final int id,String url){	
		Drawable drawable=AsyncDownloadImage.getInstance().loadDrawable(url, new AsyncMissionCallBack(){

			@Override
			public void handleDrawable(Drawable drawable) {
				// TODO Auto-generated method stub
				((ImageView)parent.findViewById(id)).setImageDrawable(drawable);
			}
			
		});
		if(drawable!=null){
			((ImageView)parent.findViewById(id)).setImageDrawable(drawable);
		}
		
	}
	/**
	 * 根据某张图片路径获取其父目录中的所有图片的路径集合(兄弟图片)
	 * @param imagePath
	 * @return
	 */
	public static List<String> getImagesPathesOfParentFolder(String imagePath){
		List<String> all=new ArrayList<String>();
		File[] siblings=getFilesByOrder(new File(imagePath).getParentFile());
		for(int i=0;i<siblings.length;i++){
			String filePath=siblings[i].getAbsolutePath();
			if(isImage(filePath)){
				all.add(filePath);
			}
		}
		return all;
	}
	/**
	 * 冒泡法排序返回子目录文件数组
	 * @param file
	 * @return
	 */
	public static File[] getFilesByOrder(File file) {
		File[] files = null;
		if (file.exists()) {
			files = file.listFiles();
			File temp;
			if (files != null && files.length > 0) {
				for (int i = 0; i < files.length; i++) {
					for (int j = 0; j < files.length - i - 1; j++) {
						if (files[j].getName().compareTo(files[j + 1].getName()) > 0) {
							temp = files[j];
							files[j] = files[j + 1];
							files[j + 1] = temp;
						}
					}
				}
			}
		}
		return files;
	}
	/**
	 * 根据路径判断文件是否为图片格式
	 * @param path
	 * @return
	 */
	public static boolean isImage(String path) {
		boolean flag = false;
		if (path != null) {
			String filePathName = new File(path).getName();
			String fileType = filePathName.substring(filePathName
					.lastIndexOf("."));
			if (fileType.equalsIgnoreCase(".jpg")
					|| fileType.equalsIgnoreCase(".png")
					|| fileType.equalsIgnoreCase(".bmp")
					|| fileType.equalsIgnoreCase(".gif")) {
				flag = true;
			}
		}
		return flag;
	}
	/**
	 * 提交Post请求
	 * @param url  服务器地址
	 * @param formData  封装了表单数据的Map
	 * @param charsetName  编码
	 * @return
	 */
	public static boolean prepareCommitPost(String urlPath, Map<String,String> formData, String charsetName){
		StringBuilder sb=new StringBuilder();
		if(formData!=null&&formData.size()>0){
	//		sb.append(urlPath+"?");
			Set<Map.Entry<String, String>>set=formData.entrySet();
			for(Map.Entry<String, String>entry:set){
				try {
					sb.append(entry.getKey()+"="+URLEncoder.encode(entry.getValue(), charsetName)+"&");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			sb.deleteCharAt(sb.length()-1);
		}Log.i("ddd", sb.toString());
		return submitPost(urlPath,sb.toString().getBytes());
	}
	
	public static boolean submitPost(String urlPath, byte[] formBytes){
		boolean flag=false;
		try {
			URL url=new URL(urlPath);
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setReadTimeout(5*1000);
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", String.valueOf(formBytes.length));
			OutputStream os=conn.getOutputStream();
			os.write(formBytes);
			os.flush();
			os.close();
			if(conn.getResponseCode()==200){
				flag=true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}
	
}
