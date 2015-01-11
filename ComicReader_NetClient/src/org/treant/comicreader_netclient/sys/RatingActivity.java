package org.treant.comicreader_netclient.sys;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.treant.comicreader_netclient.R;
import org.treant.comicreader_netclient.pojo.BookInfo;
import org.treant.comicreader_netclient.utils.Constant;
import org.treant.comicreader_netclient.utils.Utils;
import org.treant.comicreader_netclient.utils.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class RatingActivity extends Activity implements OnClickListener {
	private Date lastRateTime;
	private String bookId;
	private float bookGradeNums;
	private int bookGradeRater;
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private static final int INTERVAL = 600;
	private BookInfo bookInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.layout_rating);
		initWidgets();

		bookId = this.getIntent().getStringExtra("bookId");
		List<BookInfo> bookInfoList = Utils.getBookInfoData(Constant.REQUEST_SEARCHBOOKINFO_BYID+ bookId);
		Log.i("进入rating", bookId);
		bookInfo = bookInfoList.get(0);
		bookGradeNums = bookInfo.getBookGradeNums();
		bookGradeRater = bookInfo.getBookGradeRater();
		sp = getSharedPreferences("lastRateTime", Context.MODE_PRIVATE);
		editor = sp.edit();
		try {
			//从SharedPreferences中获取   如果是第一次使用软件 没有lastRateTime 返回2012-01-01 01:01:01
			lastRateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
			.parse(sp.getString("lastRateTime", "2011-11-11 11:11:11"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ViewHolder.currentMark.setText(String.valueOf(bookGradeNums));
		ViewHolder.personAmount.setText(String.valueOf(bookGradeRater));
		ViewHolder.bookGrade.setRating(bookGradeNums);
	}

	private void initWidgets() {
		ViewHolder.submitRate = (Button) findViewById(R.id.submitRate);
		ViewHolder.goback = (Button) findViewById(R.id.goback);
		ViewHolder.cancelRate = (Button) findViewById(R.id.cancelRate);
		ViewHolder.currentMark = (TextView) findViewById(R.id.currentMark);
		ViewHolder.personAmount = (TextView) findViewById(R.id.personAmount);
		ViewHolder.bookGrade = (RatingBar) findViewById(R.id.bookGrade);

		ViewHolder.submitRate.setOnClickListener(this);
		ViewHolder.goback.setOnClickListener(this);
		ViewHolder.cancelRate.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.submitRate:
			doRate();
			break;
		case R.id.goback:
			finish();
			break;
		case R.id.cancelRate:
			finish();
			break;
		}
	}

	private void doRate() {
		// First Rating
		long between = (new Date().getTime() - lastRateTime.getTime()) / 1000; // 换算时间间隔为秒
		if (between >= INTERVAL) {
			bookGradeNums = (bookGradeNums * bookGradeRater + ViewHolder.bookGrade
					.getRating()) / ++bookGradeRater;
			if (doUpdate()) {
				editor.putString("lastRateTime", Utils.getCurrentTime());    //Save Current Time
				editor.commit();
				ViewHolder.currentMark.setText(String.format("%.2f", bookGradeNums));
				ViewHolder.personAmount.setText(String.valueOf(bookGradeRater));
				ViewHolder.bookGrade.setRating(bookGradeNums);
				Utils.showToastMsg(this, R.string.rate_success);
			}else{
				Utils.showToastMsg(this, R.string.rate_failed);
			}
		} else {
			int interval = (int) (INTERVAL - between);
			String timeRemaining = null;
			if (interval < 60) {
				timeRemaining = interval + "秒";
			} else {
				timeRemaining = (interval / 60) + "分" + (interval % 60) + "秒";
			}
			ViewHolder.bookGrade.setRating(bookGradeNums);
			Toast.makeText(this, "对不起,至少还需:" + timeRemaining + "才能再次评分...",
					Toast.LENGTH_LONG).show();
		}
		ViewHolder.submitRate.setBackgroundColor(0xff7A7A7A);
		ViewHolder.submitRate.setClickable(false);
	}

	private boolean doUpdate() {
		String formStr="bookInfo.bookId="+bookId
				+"&bookInfo.bookGradeNums="+Float.parseFloat(String.format("%.2f", bookGradeNums))
				+"&bookInfo.bookGradeRater="+bookGradeRater;
		Log.i("formStr", formStr);
		return Utils.submitPost(Constant.REQUEST_UPDATEBOOKINFO_BYID, formStr.getBytes());
	}
}
