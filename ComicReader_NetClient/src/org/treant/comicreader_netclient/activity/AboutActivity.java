package org.treant.comicreader_netclient.activity;


import org.treant.comicreader_netclient.R;

import android.os.Bundle;

public class AboutActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.layout_about);
		allActivity.add(this);
		super.getMenu();
	}
}
