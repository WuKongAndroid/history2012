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
		this.bookList.setAdapter(new BookSpecialAdapter(list,this)); // �����ṩ��BaseAdapter  gridViewʹ��SimpleAdapter
		this.bookList.setDividerHeight(1);
		this.bookList.setOnItemClickListener(goBookMarkInfo);
	}
	/**
	 * Recommend Adapter
	 */
	private void setRecommendAdapter(){
		list=BookManager.getBookInfoListByType(Constant.REQUEST_RECOMMEND_URL,"recommend",getString(R.string.book_title_tj));
		Log.i("�Ƽ���:", list.size()+"");
		this.bookList.setFocusable(true);
		this.bookList.addHeaderView(bookHomeManager.buildHeader("recommend"));  //���bookListͷ��Ϣ
		this.bookList.setAdapter(new BookRecommendAdapter(list,this)); //ΪbookList����������
		this.bookList.setDividerHeight(0);
		this.bookList.setOnItemClickListener(goBookMarkInfo);	
	}
	/**
	 * RankList Adapter
	 */
	public void setHitNumAdapter(){
		list=BookManager.getBookInfoListByType(Constant.REQUEST_RANKLIST_URL, "order", getString(R.string.book_title_ph));
		Log.i("����List:", list.size()+"");
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
		//����TypeActivity�ķ���ȥ��������ѯ
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
			this.bookList.addHeaderView(bookHomeManager.buildHeader(""));   //�޸Ĺ���
			this.bookList.setAdapter(new BookAdapter(list,this));
			this.bookList.setDividerHeight(1);
			this.bookList.setOnItemClickListener(goBookMarkInfo);
		}
	}
	
	private AdapterView.OnItemClickListener goBookMarkInfo=new AdapterView.OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Log.i("listener", "position-->"+position+"**id-->"+id+"**headView��Ŀ->"+bookList.getHeaderViewsCount());
			// �����headerView֮��    λ��position�����ı�  ������ ����position=id+1
			int headNums=bookList.getHeaderViewsCount();
			bookMap=list.get(position-headNums);	//�����б���λ�û�ȡ����Ϣ����bookInfo
			Intent intent=new Intent(HomeActivity.this,BookInfoActivity.class);
			Bundle bundle=new Bundle();

			bundle.putSerializable("bookMap", (Serializable)bookMap);
			bundle.putString("Activity","home"); //��Map�Ļ����϶��һ��Activity��  �ٰ�Map����ȥ
			intent.putExtra("bookInfo", bundle);

			startActivity(intent);
		}
		
	};
}
