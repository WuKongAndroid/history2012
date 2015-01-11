package org.treant.comicreader_netclient.activity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.treant.comicreader_netclient.R;
import org.treant.comicreader_netclient.adapter.BookAdapter;
import org.treant.comicreader_netclient.adapter.BookHitNumAdapter;
import org.treant.comicreader_netclient.adapter.BookRecommendAdapter;
import org.treant.comicreader_netclient.adapter.BookSpecialAdapter;
import org.treant.comicreader_netclient.adapter.BookTypeAdapter;
import org.treant.comicreader_netclient.controller.BookManager;
import org.treant.comicreader_netclient.utils.Constant;
import org.treant.comicreader_netclient.utils.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class HomeActivity extends BaseActivity {

	private List<Map<String,String>>list;
	private Map<String,String>bookMap;
	private BookManager bookHomeManager;
	private ListView bookList;
	private Bundle bundle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.layout_index);
		allActivity.add(this);
		bookHomeManager=new BookManager(this);
		this.bookList=(ListView)super.findViewById(R.id.listView);
		setInitAdapter();
		super.getMenu();
	}
	/**
	 * Initialization
	 */
	private void setInitAdapter(){
		bundle=this.getIntent().getExtras();
		if(bundle!=null&&bundle.size()>0){
			String bookType=bundle.getString("bookType");
			if(bookType.equals("home")){
				setHomeAdapter();
			}else if(bookType.equals("special")){
				setSpecialAdapter();
			}else if(bookType.equals("recommend")){
				setRecommendAdapter();
			}else if(bookType.equals("order")){
				setHitNumAdapter();
			}else if(bookType.equals("type")){
				setTypeAdapter();
			}
		}else{
			setIndexAdapter();
		}
	}
	/**
	 * Special Adapter
	 */
	public void setSpecialAdapter(){
		list=BookManager.getBookInfoListByType(Constant.REQUEST_SPECIALLIST_URL, "special", getString(R.string.book_title_zd));
		this.bookList.setFocusable(true);
		this.bookList.addHeaderView(bookHomeManager.buildHeader(""));
		this.bookList.setAdapter(new BookSpecialAdapter(list,this)); // 放弃提供的BaseAdapter  gridView使用SimpleAdapter
		this.bookList.setDividerHeight(1);
		this.bookList.setOnItemClickListener(goBookMarkInfo);
	}
	/**
	 * Recommend Adapter
	 */
	private void setRecommendAdapter(){
		list=BookManager.getBookInfoListByType(Constant.REQUEST_RECOMMEND_URL,"recommend",getString(R.string.book_title_tj));
		Log.i("推荐集:", list.size()+"");
		this.bookList.setFocusable(true);
		this.bookList.addHeaderView(bookHomeManager.buildHeader("recommend"));  //添加bookList头信息
		this.bookList.setAdapter(new BookRecommendAdapter(list,this)); //为bookList设置适配器
		this.bookList.setDividerHeight(0);
		this.bookList.setOnItemClickListener(goBookMarkInfo);	
	}
	/**
	 * RankList Adapter
	 */
	public void setHitNumAdapter(){
		list=BookManager.getBookInfoListByType(Constant.REQUEST_RANKLIST_URL, "order", getString(R.string.book_title_ph));
		Log.i("排行List:", list.size()+"");
		this.bookList.setFocusable(true);
		this.bookList.addHeaderView(bookHomeManager.buildHeader("order"));
		this.bookList.setAdapter(new BookHitNumAdapter(list,this));
		this.bookList.setDividerHeight(1);
		this.bookList.setOnItemClickListener(goBookMarkInfo);
	}
	/**
	 * Type Adapter
	 */
	public void setTypeAdapter(){
		//根据TypeActivity的分类去服务器查询
		String temp=bundle.getString("bookCategory");
		try {
			list=BookManager.getBookInfoListByType(Constant.REQUEST_TYPE_BASE_URL+URLEncoder.encode(temp, "gbk"), "type", temp);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("iii", temp+"->"+Constant.REQUEST_TYPE_BASE_URL+temp);
		this.bookList.setFocusable(true);
		this.bookList.addHeaderView(bookHomeManager.buildHeader("type"));
		this.bookList.setAdapter(new BookTypeAdapter(list,this));
		this.bookList.setDividerHeight(1);
		this.bookList.setOnItemClickListener(goBookMarkInfo);
	}
	/**
	 * First Page
	 */
	public void setHomeAdapter(){
		list=bookHomeManager.getBookList(Constant.REQUEST_ALLBOOKINFO_URL);
		this.bookList.setFocusable(true);
		this.bookList.addHeaderView(bookHomeManager.buildHeader("home"));
		this.bookList.setAdapter(new BookAdapter(list,this));
		this.bookList.setDividerHeight(1);
		this.bookList.setOnItemClickListener(goBookMarkInfo);
	}
	/**
	 * Set Index Adapter  For Enter
	 */
	public void setIndexAdapter(){
		list=bookHomeManager.getBookList(Constant.REQUEST_ALLBOOKINFO_URL);
		if(list.size()==0){
			Utils.showToastMsg(HomeActivity.this, R.string.loading_no_connection_service);
		}else{
			this.bookList.setFocusable(true);
			this.bookList.addHeaderView(bookHomeManager.buildHeader(""));   //修改过了
			this.bookList.setAdapter(new BookAdapter(list,this));
			this.bookList.setDividerHeight(1);
			this.bookList.setOnItemClickListener(goBookMarkInfo);
		}
	}
	
	private AdapterView.OnItemClickListener goBookMarkInfo=new AdapterView.OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Log.i("listener", "position-->"+position+"**id-->"+id+"**headView数目->"+bookList.getHeaderViewsCount());
			// 添加了headerView之后    位置position发生改变  经测试 这里position=id+1
			int headNums=bookList.getHeaderViewsCount();
			bookMap=list.get(position-headNums);	//根据列表项位置获取书信息对象bookInfo
			Intent intent=new Intent(HomeActivity.this,BookInfoActivity.class);
			Bundle bundle=new Bundle();

			bundle.putSerializable("bookMap", (Serializable)bookMap);
			bundle.putString("Activity","home"); //在Map的基础上多加一个Activity键  再把Map传过去
			intent.putExtra("bookInfo", bundle);

			startActivity(intent);
		}
		
	};
}
