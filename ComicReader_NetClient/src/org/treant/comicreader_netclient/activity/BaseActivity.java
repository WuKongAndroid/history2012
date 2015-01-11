package org.treant.comicreader_netclient.activity;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.treant.comicreader_netclient.R;
import org.treant.comicreader_netclient.utils.Constant;
import org.treant.comicreader_netclient.utils.Utils;
import org.treant.comicreader_netclient.utils.ViewHolder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class BaseActivity extends Activity implements OnClickListener{
	public static final int ABOUT=Menu.FIRST;
	public static final int LOGOUT=Menu.FIRST+1;
	public static final int ADVISE=Menu.FIRST+2;
	public static List<Activity> allActivity=new ArrayList<Activity>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		allActivity.add(this);
	}
	/**
	 * �ײ������˵�
	 */
	public void getMenu(){
		ViewHolder.bookIndex=(ImageButton)super.findViewById(R.id.bookIndex);
		ViewHolder.bookIndex.setOnClickListener(this);
		
		ViewHolder.bookShelf=(ImageButton)super.findViewById(R.id.bookShelf);
		ViewHolder.bookShelf.setOnClickListener(this);
		
		ViewHolder.bookBrowser=(ImageButton)super.findViewById(R.id.bookBrowser);
		ViewHolder.bookBrowser.setOnClickListener(this);
		
		ViewHolder.bookSearch=(ImageButton)super.findViewById(R.id.bookSearch);
		ViewHolder.bookSearch.setOnClickListener(this);
		
		ViewHolder.bookMore=(ImageButton)super.findViewById(R.id.bookMore);
		ViewHolder.bookMore.setOnClickListener(this);		
	}
	/**
	 * Ϊ�ײ������˵�����OnClick�����¼�
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.bookIndex:
			startActivity(new Intent(this, HomeActivity.class));
			break;
		case R.id.bookShelf:
			startActivity(new Intent(this, BookShelfActivity.class));
			break;
		case R.id.bookBrowser:
			Intent intent=new Intent(this, MainActivity.class);
			//�������BookInfoActivity�н���Main,����browse���� ���ȡ����Ķ���¼�����һ��
			Map<String,String>bookMap=null;
			List<Map<String,String>> recentlyReadList=Utils.parseRelatedBookInfoFromXml(Constant.RECENTLYREAD_PATH);
			//һ�������ȡ����Ķ��б������һ��
			Log.i("recentlyReadSize", recentlyReadList.size()+"");
			if(recentlyReadList.size()>0){         
				bookMap = recentlyReadList.get(recentlyReadList.size()-1);	
				Log.i("û��info��", "������Ķ���ȡ");
			}
			SharedPreferences sp=getSharedPreferences("lastReadImagePath", Context.MODE_PRIVATE);
			String lastReadImagePath=sp.getString("lastReadImagePath", null);
			 /**
			  * ����ǵ�һ��ʹ�����  ��lastReadImagePathΪnull  ��Ҫ������
			  * ������.��ѹ�ɹ����Ȼ�Զ�����MainActivity  �˳�ʱ��onStop�лᱣ��lastReadImagePath   
			  * ����Ϊ�����û��ɹ�������->��ѹ->����->�˳������������SharedPreference�����  ��������ص�����ļ����ж�ȡ���һ��ͼƬ
			  */
			if(lastReadImagePath==null){  
				File decompDir=new File(Constant.DECOMPRESS_DIR_PATH);
				if(decompDir.exists()&&decompDir.listFiles().length>0){
					File[] imagesDirs=decompDir.listFiles();
					File ranImageDir=imagesDirs[new Random().nextInt(imagesDirs.length)];           
					if(ranImageDir.exists()&&ranImageDir.listFiles().length>0){
						File[] images=ranImageDir.listFiles();
						File ranImage=images[new Random().nextInt(images.length)];
						if(ranImage.exists()&&ranImage.length()>0){
							lastReadImagePath=ranImage.getAbsolutePath();                 
							Log.i("ͼƬ��", lastReadImagePath);
							for(Map<String,String>map : Utils.parseRelatedBookInfoFromXml(Constant.BOOKINFO_CACHE_PATH)){   
								if(map.containsValue(ranImageDir.getName())){
									bookMap=map;            
									intent.putExtra("imagePath", lastReadImagePath);
									intent.putExtra("bookMap", (Serializable)bookMap);
									startActivity(intent);
								}
							}
						}else{
							Utils.showToastMsg(this, R.string.emptyDir);
						}
					}else{
						Utils.showToastMsg(this, R.string.noPics);
					}
				}else{
					Utils.showToastMsg(this, R.string.noPics);
				}
			}else{
				intent.putExtra("imagePath", lastReadImagePath);
				intent.putExtra("bookMap", (Serializable)bookMap);
				startActivity(intent);
			}
			break;
		case R.id.bookSearch:
			startActivity(new Intent(this, BookSearchActivity.class));
			break;
		case R.id.bookMore:
			showMenuMore();
			break;
		}
	}	
	public void showMenuMore(){
		new AlertDialog.Builder(this).setTitle(R.string.menu_more)
		.setIcon(R.drawable.menu_more)
		.setItems(R.array.menumore, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				switch(which){
				case 0:
					startActivity(new Intent(BaseActivity.this,AboutActivity.class));
					break;
				case 1:
					startActivity(new Intent(BaseActivity.this,AdviseActivity.class));
					break;
				case 2:
					exitRightNow(BaseActivity.this);
					break;
				}
			}
			
		}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
			
		}).show();
	}
	/**
	 * ���menu��ť ��ʾ"����"�˵�
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.addSubMenu(0, ABOUT, 0, "����");
		menu.addSubMenu(0, ADVISE, 1, "����");
		menu.addSubMenu(0, LOGOUT, 2, "�˳�");
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case ABOUT:
			startActivity(new Intent(BaseActivity.this,AboutActivity.class));
			break;
		case ADVISE:
			startActivity(new Intent(BaseActivity.this,AdviseActivity.class));
			break;
		case LOGOUT:
			exitRightNow(this);
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	/**
	 * ���"back"��ʱ �˳�ϵͳ ��дOnKeyDown����
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(KeyEvent.KEYCODE_BACK==keyCode&&event.getRepeatCount()==0){
			exitRightNow(BaseActivity.this);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public static void exitRightNow(Context context){
		new AlertDialog.Builder(context).setTitle(R.string.logout_title)
		.setIcon(R.drawable.menu_more)
		.setMessage(R.string.logout_msg_body)
		.setPositiveButton(R.string.logout_confirm,new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				for(Activity activity:allActivity){
					if(activity!=null){
						activity.finish();
					}
				}
			}
		}).setNegativeButton(R.string.logout_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		}).show();
	}
}
