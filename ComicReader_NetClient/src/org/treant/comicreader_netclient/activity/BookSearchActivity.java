package org.treant.comicreader_netclient.activity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.treant.comicreader_netclient.R;
import org.treant.comicreader_netclient.adapter.BookSearchAdapter;
import org.treant.comicreader_netclient.controller.BookManager;
import org.treant.comicreader_netclient.utils.Constant;
import org.treant.comicreader_netclient.utils.Utils;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class BookSearchActivity extends BaseActivity {
	private ListView bookList;
	private List<Map<String, String>> list;
	private Map<String, String> bookMap;
	private BookManager bookSearchManager;
	public ProgressDialog progressDialog;
	private EditText searchEt;
	private ImageButton search;
	private RadioGroup searchRg;
	private String keyWords;
	private String searchUrl=Constant.REQUEST_SEARCHName_URL;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.layout_search);
		allActivity.add(this);
		initWidgets();
		super.getMenu();
		initAdapter();
	}

	private void initWidgets() {
		this.bookList = (ListView) super.findViewById(R.id.listView);
		this.searchEt=(EditText)super.findViewById(R.id.keyWords);
		this.search=(ImageButton)super.findViewById(R.id.search);
		this.searchRg=(RadioGroup)super.findViewById(R.id.searchRg);
		Animation scaleAnimation=AnimationUtils.loadAnimation(this, R.anim.scale);
		this.search.startAnimation(scaleAnimation);
		this.search.setOnClickListener(new ImageButton.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bookSearchManager = new BookManager(BookSearchActivity.this);
				keyWords=searchEt.getText().toString().trim();
				try {
					list=bookSearchManager.getBookList(searchUrl+ URLEncoder.encode(keyWords, "gbk"));
					refreshSearchList(list);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		this.searchRg.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch(checkedId){
				case R.id.byName:
					searchUrl=Constant.REQUEST_SEARCHName_URL;
					Toast.makeText(BookSearchActivity.this, "name", 1).show();
					break;
				case R.id.byWriter:
					searchUrl=Constant.REQUEST_SEARCHWriter_URL;
					Toast.makeText(BookSearchActivity.this, "writer", 1).show();
					break;
				case R.id.byContent:
					searchUrl=Constant.REQUEST_SEARCHContent_URL;
					Toast.makeText(BookSearchActivity.this, "content", 1).show();
					break;
				}
			}
			
		});
	}

	public void initAdapter(){
		bookSearchManager = new BookManager(BookSearchActivity.this);
		list=Utils.parseRelatedBookInfoFromXml(Constant.BOOKINFO_CACHE_PATH);
		refreshSearchList(list);
	}

	private void refreshSearchList(List<Map<String, String>> list){
		bookList.setAdapter(new BookSearchAdapter (list,this));
		bookList.setDividerHeight(1);
		bookList.setOnItemClickListener(toBookInfo);
	}
	private ListView.OnItemClickListener toBookInfo = new ListView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			position -= bookList.getHeaderViewsCount();
			bookMap = (Map<String, String>) list.get(position);
			Intent intent = new Intent(BookSearchActivity.this,
					BookInfoActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("bookMap", (Serializable)bookMap);
			bundle.putString("Activity", "search");
			intent.putExtra("bookInfo", bundle);
			bookMap = null;
			startActivity(intent);
		}

	};
}
