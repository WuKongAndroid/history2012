package org.treant.comicreader_netclient.adapter;

import java.util.List;
import java.util.Map;

import org.treant.comicreader_netclient.R;
import org.treant.comicreader_netclient.activity.HomeActivity;
import org.treant.comicreader_netclient.utils.Utils;
import org.treant.comicreader_netclient.utils.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BookAdapter extends BaseAdapter implements ListAdapter {
	private Context context;
	private Intent intent;
	private Bundle bundle;
	private Map<String, String> bookMap;
	private List<Map<String, String>> bookList;
	private LayoutInflater mLayoutInflater;

	public BookAdapter(List<Map<String, String>> bookList, Context context) {
		this.bookList = bookList;
		this.context=context;
		this.mLayoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return (bookList != null && bookList.size() > 0)?bookList.size():0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return bookList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView=null;
		bookMap=bookList.get(position);
		if(position==0&&bookMap.get("book_more").equals("more_zd")){
			//设置本期主打分块内容
			View view=mLayoutInflater.inflate(R.layout.layout_book_title,null);
			ViewHolder.layout1=(RelativeLayout)view.findViewById(R.id.layout1);
			ViewHolder.layout1.setVisibility(View.VISIBLE);
			ViewHolder.bookType=(TextView)view.findViewById(R.id.bookType);
			ViewHolder.bookType.setText(bookMap.get("book_title"));
			ViewHolder.bookType.setVisibility(View.VISIBLE);
			ViewHolder.bookMore=(ImageButton)view.findViewById(R.id.bookMore);
			ViewHolder.bookMore.setOnClickListener(new ImageButton.OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					intent=new Intent(context,HomeActivity.class);
					bundle=new Bundle();
					bundle.putString("bookType","special");
					intent.putExtras(bundle);
					context.startActivity(intent);
				}
				
			});
			ViewHolder.bookName=(TextView)view.findViewById(R.id.bookName);
			ViewHolder.updateTime=(TextView)view.findViewById(R.id.updateTime);
			ViewHolder.bookGrade=(RatingBar)view.findViewById(R.id.bookGrade);
			ViewHolder.bookContent=(TextView)view.findViewById(R.id.bookContent);

			Utils.asyncLoadDrawable(view, R.id.bookCover, bookMap.get("bookCover"));
			ViewHolder.bookName.setText(bookMap.get("bookName"));
			ViewHolder.updateTime.setText("["+bookMap.get("updateTime")+"]");
			ViewHolder.bookGrade.setRating(Float.parseFloat(bookMap.get("bookGradeNums")));
			ViewHolder.bookContent.setText(bookMap.get("bookContent"));

			return view;
		}
		if(position==1&&bookMap.get("book_more").equals("more_tj")){
			View view =mLayoutInflater.inflate(R.layout.layout_book_title, null);
			ViewHolder.layout1=(RelativeLayout)view.findViewById(R.id.layout1);
			ViewHolder.layout1.setVisibility(View.VISIBLE);
			ViewHolder.bookType=(TextView)view.findViewById(R.id.bookType);
			ViewHolder.bookType.setText(bookMap.get("book_title"));
			ViewHolder.bookType.setVisibility(View.VISIBLE);
			ViewHolder.bookMore=(ImageButton)view.findViewById(R.id.bookMore);
			ViewHolder.bookMore.setOnClickListener(new ImageButton.OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					intent=new Intent(context,HomeActivity.class);
					bundle=new Bundle();
					bundle.putString("bookType", "recommend");
					intent.putExtras(bundle);
					context.startActivity(intent);
				}
				
			});
			ViewHolder.bookName=(TextView)view.findViewById(R.id.bookName);
			ViewHolder.updateTime=(TextView)view.findViewById(R.id.updateTime);
			ViewHolder.bookGrade=(RatingBar)view.findViewById(R.id.bookGrade);
			ViewHolder.bookContent=(TextView)view.findViewById(R.id.bookContent);

			Utils.asyncLoadDrawable(view, R.id.bookCover, bookMap.get("bookCover"));
			ViewHolder.bookName.setText(bookMap.get("bookName"));
			ViewHolder.updateTime.setText("["+bookMap.get("updateTime")+"]");
			ViewHolder.bookGrade.setRating(Float.parseFloat(bookMap.get("bookGradeNums")));
			ViewHolder.bookContent.setText(bookMap.get("bookContent"));
			return view;
		}
		if(position==4&&bookMap.get("book_more").equals("more_ph")){
			View view =mLayoutInflater.inflate(R.layout.layout_book_title, null);
			ViewHolder.layout1=(RelativeLayout)view.findViewById(R.id.layout1);
			ViewHolder.layout1.setVisibility(View.VISIBLE);
			ViewHolder.bookType=(TextView)view.findViewById(R.id.bookType);
			ViewHolder.bookType.setText(bookMap.get("book_title"));
			ViewHolder.bookType.setVisibility(View.VISIBLE);
			ViewHolder.bookMore=(ImageButton)view.findViewById(R.id.bookMore);
			ViewHolder.bookMore.setOnClickListener(new ImageButton.OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					intent=new Intent(context,HomeActivity.class);
					bundle=new Bundle();
					bundle.putString("bookType", "order");
					intent.putExtras(bundle);
					context.startActivity(intent);
				}
				
			});
	
			ViewHolder.bookName=(TextView)view.findViewById(R.id.bookName);
		   	ViewHolder.updateTime=(TextView)view.findViewById(R.id.updateTime);
		   	ViewHolder.bookGrade=(RatingBar)view.findViewById(R.id.bookGrade);
		   	ViewHolder.bookContent=(TextView)view.findViewById(R.id.bookContent);
			
			Utils.asyncLoadDrawable(view, R.id.bookCover, bookMap.get("bookCover"));
		    ViewHolder.bookName.setText(bookMap.get("bookName"));
		   	ViewHolder.updateTime.setText("["+bookMap.get("updateTime")+"]");
		  	ViewHolder.bookGrade.setRating(Float.parseFloat(bookMap.get("bookGradeNums")));
		   	ViewHolder.bookContent.setText(bookMap.get("bookContent"));	    				
			return view;
		}
		convertView =mLayoutInflater.inflate(R.layout.layout_book, null);

		ViewHolder.bookName=(TextView)convertView.findViewById(R.id.bookName);
		ViewHolder.updateTime=(TextView)convertView.findViewById(R.id.updateTime);
		ViewHolder.bookGrade=(RatingBar)convertView.findViewById(R.id.bookGrade);
		ViewHolder.bookContent=(TextView)convertView.findViewById(R.id.bookContent);

		Utils.asyncLoadDrawable(convertView, R.id.bookCover, bookMap.get("bookCover"));
		ViewHolder.bookName.setText(bookMap.get("bookName"));
		ViewHolder.updateTime.setText("["+bookMap.get("updateTime")+"]");
		ViewHolder.bookGrade.setRating(Float.parseFloat(bookMap.get("bookGradeNums")));
		ViewHolder.bookContent.setText(bookMap.get("bookContent"));
		return convertView;
	}

}
