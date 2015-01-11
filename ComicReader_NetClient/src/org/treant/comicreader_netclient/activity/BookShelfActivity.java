package org.treant.comicreader_netclient.activity;

import java.io.File;

import org.treant.comicreader_netclient.R;
import org.treant.comicreader_netclient.utils.Constant;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TabHost;

public class BookShelfActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TabHost tabHost = this.getTabHost();
		LayoutInflater.from(this).inflate(R.layout.layout_tabmain,
				tabHost.getTabContentView());
		// Collection
		TabHost.TabSpec tabSpec1 = tabHost.newTabSpec(this
				.getString(R.string.menu_mycollection));
		tabSpec1.setIndicator(this.getString(R.string.menu_mycollection),
				this.getResources().getDrawable(R.drawable.collection))
				.setContent(
						new Intent(BookShelfActivity.this,
								BookCollectionActivity.class));
		tabHost.addTab(tabSpec1);
		// RecentlyRead
		TabHost.TabSpec tabSpec2 = tabHost.newTabSpec(this
				.getString(R.string.menu_recentlyread));
		tabSpec2.setIndicator(
				this.getString(R.string.menu_recentlyread, this.getResources()
						.getDrawable(R.drawable.read))).setContent(
				new Intent(BookShelfActivity.this, BookRecentlyReadActivity.class));
		tabHost.addTab(tabSpec2);

		if (new File(Constant.COLLECTION_PATH).exists()) {
			tabHost.setCurrentTab(0);
		} else {
			tabHost.setCurrentTab(1);
		}

	}

}
