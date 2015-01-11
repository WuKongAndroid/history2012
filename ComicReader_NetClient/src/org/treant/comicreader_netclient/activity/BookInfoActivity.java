package org.treant.comicreader_netclient.activity;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

import org.treant.comicreader_netclient.R;
import org.treant.comicreader_netclient.controller.AsyncDownloadZip;
import org.treant.comicreader_netclient.utils.AsyncDownloadImage;
import org.treant.comicreader_netclient.utils.AsyncDownloadImage.AsyncMissionCallBack;
import org.treant.comicreader_netclient.utils.Constant;
import org.treant.comicreader_netclient.utils.Utils;
import org.treant.comicreader_netclient.utils.ViewHolder;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

public class BookInfoActivity extends BaseActivity {
	private Map<String,String> bookMap;
	private Intent intent;
	private Bundle bundle;
	private AsyncDownloadZip asyncDownloadZip;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.layout_bookinfo);
		allActivity.add(this);
		super.getMenu();
		bundle = this.getIntent().getBundleExtra("bookInfo");
		bookMap=(Map<String, String>) bundle.getSerializable("bookMap");
		initBookInfo();
	}
	/**
	 * 参数初始化
	 */
	private void initBookInfo() {
		
		ViewHolder.bookCover = (ImageView)findViewById(R.id.bookCover);
		ViewHolder.bookName = (TextView)findViewById(R.id.bookName);
		ViewHolder.updateTime=(TextView)findViewById(R.id.updateTime);
		ViewHolder.bookGrade = (RatingBar)findViewById(R.id.bookGrade);
		ViewHolder.bookContent = (TextView)findViewById(R.id.bookContent);
		ViewHolder.bookZipPath=(TextView)findViewById(R.id.bookZipPath);
		ViewHolder.bookId=(TextView)findViewById(R.id.bookId);	
		ViewHolder.bookWriter = (TextView)findViewById(R.id.bookWriter);
		ViewHolder.countryName=(TextView)findViewById(R.id.countryName);	
		ViewHolder.bookGradeNums=(TextView)findViewById(R.id.bookGradeNums);
		ViewHolder.bookGradeRatio=(TextView)findViewById(R.id.bookGradeRatio);
		ViewHolder.bookGradeRater=(TextView)findViewById(R.id.bookGradeRater);
		ViewHolder.bookCategory = (TextView)findViewById(R.id.bookCategory);
		ViewHolder.bookIsOver = (TextView)findViewById(R.id.bookIsOver);
	
		ViewHolder.download_arrow=(ImageView)super.findViewById(R.id.download_arrow);
		ViewHolder.download_percent=(TextView)findViewById(R.id.download_percent);
		ViewHolder.progressBar=(ProgressBar)findViewById(R.id.progressBar);
		ViewHolder.collection = (ImageButton) findViewById(R.id.collection);
		ViewHolder.collection.setOnClickListener(this);
		ViewHolder.read = (ImageButton) findViewById(R.id.read);
		ViewHolder.read.setOnClickListener(this);
		ViewHolder.goBack = (ImageButton) findViewById(R.id.goback);
		ViewHolder.goBack.setOnClickListener(this);
		Drawable drawable=AsyncDownloadImage.getInstance().loadDrawable(bookMap.get("bookCover"), new AsyncMissionCallBack(){

			@Override
			public void handleDrawable(Drawable drawable) {
				// TODO Auto-generated method stub
				ViewHolder.bookCover.setImageDrawable(drawable);
			}
			
		});
		if(drawable!=null){
			ViewHolder.bookCover.setImageDrawable(drawable);
		}
		ViewHolder.bookName.setText(bookMap.get("bookName"));
		ViewHolder.updateTime.setText("["+bookMap.get("updateTime")+"]");
		ViewHolder.bookGrade.setRating(Float.parseFloat(bookMap.get("bookGradeNums")));
		ViewHolder.bookContent.setText("详情:"+bookMap.get("bookContent"));
		ViewHolder.bookZipPath.setText("路径:"+bookMap.get("bookZipPath"));
		ViewHolder.bookId.setText("ID:"+bookMap.get("bookId"));
		ViewHolder.bookWriter.setText("作者:"+bookMap.get("bookWriter"));
		ViewHolder.countryName.setText("国家:"+bookMap.get("countryName"));
		ViewHolder.bookGradeNums.setText("分数:"+bookMap.get("bookGradeNums"));
		ViewHolder.bookGradeRatio.setText("评分率:"+bookMap.get("bookGradeRatio"));
		ViewHolder.bookGradeRater.setText("人气:"+bookMap.get("bookGradeRater"));	
		ViewHolder.bookCategory.setText("类型:"+bookMap.get("bookCategory"));
		ViewHolder.bookIsOver.setText("是否完结:"+bookMap.get("bookIsOver"));
	}
	/**
	 * 收藏 阅读 返回的监听
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		final String idStr=bookMap.get("bookId");    //文件的唯一id
		switch (v.getId()) {
		case R.id.collection:
			if (Utils.getBooInfoCountsByAttribute(Constant.COLLECTION_PATH,"bookId", idStr) == 0) {
				Log.i("bookMap", "元素个数:"+bookMap.size());
				BookCollectionActivity.collectedBooks.add(bookMap);
				Utils.serializeBookInfoAsXml(BookCollectionActivity.collectedBooks,Constant.CONFIG_PATH, Constant.COLLECTION_PATH);
				Utils.showToastMsg(BookInfoActivity.this, R.string.bookInfo_collection_success);
			} else {
				Utils.showToastMsg(BookInfoActivity.this,
						R.string.book_collection_exist);
			}
			break;
		case R.id.read:
			// 待扩展 下载阅读模块
			String zipUrl=Constant.REQUEST_ZIP_RES_ROOT_URL+idStr+".zip";
			final String imagesFolderDir=Constant.DECOMPRESS_DIR_PATH+idStr;
			if(!new File(imagesFolderDir).exists()){
				asyncDownloadZip=new AsyncDownloadZip(Constant.COMPRESS_DIR_PATH,idStr+".zip"){
					@Override
					protected void onPreExecute() {
						// TODO Auto-generated method stub
						ViewHolder.download_arrow.setImageResource(R.drawable.download_icon);
						Animation animation=AnimationUtils.loadAnimation(BookInfoActivity.this, R.anim.download_anim);
						animation.setFillAfter(true);
						ViewHolder.download_arrow.startAnimation(animation);
						
						ViewHolder.progressBar.setVisibility(View.VISIBLE);
						ViewHolder.progressBar.setProgress(0);
						
						ViewHolder.download_percent.setVisibility(View.VISIBLE);
						ViewHolder.download_percent.setText(R.string.book_download_begin);
					}
					
					@Override
					protected void onProgressUpdate(Integer... values) {
						// TODO Auto-generated method stub
						ViewHolder.progressBar.setMax(values[1]);
						ViewHolder.progressBar.setProgress(values[0]); //String.format("%.2f KB", sizeByBytes / kb);
						ViewHolder.download_percent.setText(String.format("%.2f", ((float)values[0]/(float)values[1])*100)+"/100");
					}
					
					@Override
					protected void onPostExecute(Boolean result) {
						// TODO Auto-generated method stub
						if(result){
							Utils.showToastMsg(BookInfoActivity.this, R.string.download_refresh);
							Utils.upZip(Constant.COMPRESS_DIR_PATH+idStr+".zip", Constant.DECOMPRESS_DIR_PATH);
							toMainActivity(imagesFolderDir);
						}else{
							Utils.showToastMsg(BookInfoActivity.this, R.string.download_failed);
						}
						
					}
				};	
				asyncDownloadZip.execute(zipUrl);
			}else{
				Utils.showToastMsg(BookInfoActivity.this, R.string.download_exists);
				toMainActivity(imagesFolderDir);
			}
			break;
		case R.id.goback:
			bundle = this.getIntent().getBundleExtra("bookInfo");
			if (bundle != null && bundle.size() > 0) {
				String activity = bundle.getString("Activity");
				if (activity.equals("home")) {
					intent = new Intent(this, HomeActivity.class);
					bundle.putString("bookType", "home");
					intent.putExtras(bundle);
					startActivity(intent);
				} else if (activity.equals("collection")) {
					intent = new Intent(this,BookShelfActivity.class);
					startActivity(intent);
				} else if (activity.equals("search")) {
					intent = new Intent(this, BookSearchActivity.class);
					startActivity(intent);
					break;
				}
			}

		}
	}
	private void toMainActivity(String imagesFolderDir) {
		intent=new Intent(BookInfoActivity.this, MainActivity.class);
		//解压后以id命名的图片文件夹中的第一张图片的路径
		String firstImagePath=(new File(imagesFolderDir).listFiles())[0].getAbsolutePath();
		//经排序后的第一张图片  保证从第一张开始浏览
		String orderedFirstImagePath=Utils.getImagesPathesOfParentFolder(firstImagePath).get(0);
//		Log.i("第一个Main的ImagePath", orderedFirstImagePath);
		intent.putExtra("imagePath", orderedFirstImagePath);
		Log.i("bookMap到Main", bookMap.size()+"");
		intent.putExtra("bookMap",(Serializable) bookMap);
		startActivity(intent);
	}
	
}
