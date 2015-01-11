package org.treant.comicreader_netclient.activity;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.treant.comicreader_netclient.R;
import org.treant.comicreader_netclient.adapter.BookCollectionAdapter;
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

public class BookCollectionActivity extends BaseActivity {
	private ListView bookList;
	private Map<String, String> bookMap;
	public static List<Map<String, String>> collectedBooks = Utils
			.parseRelatedBookInfoFromXml(Constant.COLLECTION_PATH);

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
		this.bookList.setFocusable(true);
		refreshListView(collectedBooks);
		this.bookList.setOnItemClickListener(toBookInfo);
		this.bookList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(BookCollectionActivity.this)
						.setIcon(R.drawable.dota_chicken)
						.setTitle(R.string.book_collection_title)
						.setItems(R.array.coll_dele,
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
			bookMap = collectedBooks.get(position);
			Intent intent = new Intent(BookCollectionActivity.this,
					BookInfoActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("bookMap", (Serializable) bookMap);
			bundle.putString("Activity", "collection");
			intent.putExtra("bookInfo", bundle);

			startActivity(intent);
		}

	};

	private void refreshListView(List<Map<String, String>> list) {
		BookCollectionAdapter mAdapter = new BookCollectionAdapter(
				collectedBooks, this);
		this.bookList.setAdapter(mAdapter);
	}

	private void deleteSelectedItem(int position) {
		collectedBooks.remove(position);
		refreshListView(collectedBooks);
		if (collectedBooks.size() == 0) {
			finish();
		}
		Utils.serializeBookInfoAsXml(collectedBooks, Constant.CONFIG_PATH,Constant.COLLECTION_PATH);
	}

	private void deleteAllCollection() {
		new AlertDialog.Builder(BookCollectionActivity.this)
				.setTitle(R.string.logout_title)
				.setIcon(R.drawable.dota_cm)
				.setMessage(R.string.book_collection_deleteAll)
				.setPositiveButton(R.string.confirm,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								collectedBooks.clear();
								refreshListView(collectedBooks);
								finish();
								new File(Constant.COLLECTION_PATH).delete();
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
