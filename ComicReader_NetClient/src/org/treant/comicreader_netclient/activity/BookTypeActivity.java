package org.treant.comicreader_netclient.activity;

import java.util.List;
import java.util.Map;

import org.treant.comicreader_netclient.R;
import org.treant.comicreader_netclient.adapter.BookTypesGridAdapter;
import org.treant.comicreader_netclient.controller.BookManager;
import org.treant.comicreader_netclient.utils.Constant;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

public class BookTypeActivity extends BaseActivity {
	private GridView gridView;
	private List<Map<String, String>> list;
	private ImageButton home,recommend,order,type;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.layout_booktype);
		allActivity.add(this);
		initWidgets();
		
		initAdapter();
		super.getMenu();
	}
	private void initWidgets() {
		this.gridView=(GridView)super.findViewById(R.id.gridView);
		this.home=(ImageButton)super.findViewById(R.id.home);
		this.recommend=(ImageButton)super.findViewById(R.id.recommend);
		this.order=(ImageButton)super.findViewById(R.id.order);
		this.type=(ImageButton)super.findViewById(R.id.type);
		this.type.setBackgroundResource(R.drawable.tab_type_white);
		this.home.setOnClickListener(topBarListener);
		this.recommend.setOnClickListener(topBarListener);
		this.order.setOnClickListener(topBarListener);
		this.type.setOnClickListener(topBarListener);
	}
	
	
	private void initAdapter(){
		list=BookManager.getBookCategoryList(Constant.REQUEST_ALLBOOKCATEGORY_URL);
		Log.i("类型22222多少", list.size()+"");
		gridView.setAdapter(new BookTypesGridAdapter(list,this));
		gridView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				Toast.makeText(BookTypeActivity.this, list.get(position).get("cname"), 5).show();
				Intent intent=new Intent();
				intent.setClass(BookTypeActivity.this, HomeActivity.class);
				Bundle bundle=new Bundle();
				bundle.putString("bookType","type");
				bundle.putString("bookCategory", list.get(position).get("cname"));
				intent.putExtras(bundle);
				startActivity(intent);
			}
			
		});
	
	}
	
	private View.OnClickListener topBarListener=new View.OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			Bundle bundle=new Bundle();
			switch (v.getId()){
			case R.id.home:
				intent.setClass(BookTypeActivity.this, HomeActivity.class);
				bundle.putString("bookType", "home");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.recommend:
				intent.setClass(BookTypeActivity.this, HomeActivity.class);
				bundle.putString("bookType", "recommend");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.order:
				intent.setClass(BookTypeActivity.this, HomeActivity.class);
				bundle.putString("bookType", "order");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.type:
				onCreate(null);
				break;
			}
		}
		
	};
}
