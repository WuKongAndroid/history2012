package org.treant.comicreader_netclient.activity;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.treant.comicreader_netclient.R;
import org.treant.comicreader_netclient.adapter.BookRecentlyReadAdapter;
import org.treant.comicreader_netclient.utils.Constant;
import org.treant.comicreader_netclient.utils.Utils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class BookRecentlyReadActivity extends BaseActivity {
	private ListView bookList;
	private Map<String, String> bookMap;
	public static List<Map<String, String>> recentlyreadBooks = Utils
			.parseRelatedBookInfoFromXml(Constant.RECENTLYREAD_PATH);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.layout_shelf_list);
		allActivity.add(this);
		this.bookList = (ListView) super.findViewById(R.id.listView);
		super.getMenu();
		setInitAdapter();
	}

	public void setInitAdapter() {
		// this.recentlyreadlist=getDataResources();
		this.bookList.setFocusable(true);
		refreshListView(recentlyreadBooks);
		this.bookList.setOnItemClickListener(toBookInfo);
		this.bookList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(BookRecentlyReadActivity.this)
						.setIcon(R.drawable.dota_chicken)
						.setTitle(R.string.book_recently_title)
						.setItems(R.array.rece_dele,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										switch (which) {
										case 0:
											deleteSelectedItem(position);
											break;
										case 1:
											deleteAllCollection();
											break;
										}
									}
								})
						.setNegativeButton(R.string.cancel,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								}).show();
				return false;
			}

		});
	}

	private ListView.OnItemClickListener toBookInfo = new ListView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			bookMap = recentlyreadBooks.get(position);
			// 将获取的数据通过intent传递至BookInfoActivity
			Intent intent = new Intent(BookRecentlyReadActivity.this,
					BookInfoActivity.class);
			Bundle bundle = new Bundle();
			if (bookMap.get("picPath") != null
					&& bookMap.get("picPath").length() > 0) {
				intent = new Intent(BookRecentlyReadActivity.this,
						MainActivity.class);
				bundle.putString("bookName", bookMap.get("bookName"));
				bundle.putString("bookId", bookMap.get("bookId"));
				bundle.putString("picPath", bookMap.get("picPath"));
				intent.putExtras(bundle);
			} else {
				intent = new Intent(BookRecentlyReadActivity.this,
						BookInfoActivity.class);
				bundle.putSerializable("bookMap", (Serializable)bookMap);
				bundle.putString("Activity", "shelf");
				intent.putExtra("bookInfo", bundle);
			}
			startActivity(intent);
		}

	};

	private void refreshListView(List<Map<String, String>> list) {
		BookRecentlyReadAdapter adapter = new BookRecentlyReadAdapter(
				recentlyreadBooks, this);
		bookList.setAdapter(adapter);
	}

	private void deleteSelectedItem(int position) {
		recentlyreadBooks.remove(position);
		refreshListView(recentlyreadBooks);
		if (recentlyreadBooks.size() == 0) {
			finish();
		}
		Utils.serializeBookInfoAsXml(recentlyreadBooks,Constant.CONFIG_PATH,
				Constant.RECENTLYREAD_PATH);
	}

	private void deleteAllCollection() {
		new AlertDialog.Builder(BookRecentlyReadActivity.this)
				.setTitle(R.string.logout_title)
				.setIcon(R.drawable.dota_cm)
				.setMessage(R.string.book_collection_deleteAll)
				.setPositiveButton(R.string.confirm,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								recentlyreadBooks.clear();
								refreshListView(recentlyreadBooks);
								finish();
								new File(Constant.RECENTLYREAD_PATH).delete();
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						}).show();
	}
}
