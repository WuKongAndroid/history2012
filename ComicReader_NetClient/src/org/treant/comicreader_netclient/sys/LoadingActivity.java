package org.treant.comicreader_netclient.sys;


import java.io.File;

import org.treant.comicreader_netclient.R;
import org.treant.comicreader_netclient.activity.HomeActivity;
import org.treant.comicreader_netclient.utils.Constant;
import org.treant.comicreader_netclient.utils.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class LoadingActivity extends Activity implements AnimationListener {
	private ImageView load_start;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.layout_loading);
		
		this.load_start=(ImageView)super.findViewById(R.id.load_start);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			if (Utils.getSDCardRoot() != null) {
				createTempFile();
				Animation animFadein = AnimationUtils.loadAnimation(this,
						R.anim.alpha_fadein);
				animFadein.setAnimationListener(this);
				load_start.setAnimation(animFadein);
			} else {
				Utils.showToastMsg(LoadingActivity.this,R.string.sdcard_no_files);
			}
		} else {
			Utils.showToastMsg(LoadingActivity.this,R.string.sdcard_no_sdcard);
			finish();
		}
	}
	
	private void createTempFile() {
		File file = new File(Constant.ROOT_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		Animation animFadeout = AnimationUtils.loadAnimation(
				LoadingActivity.this, R.anim.alpha_fadeout);
		load_start.setAnimation(animFadeout);
		load_start.startAnimation(animFadeout);
		animFadeout.setFillAfter(true);
		animFadeout.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				startActivity(new Intent(LoadingActivity.this,HomeActivity.class));
				finish();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}
	
}
