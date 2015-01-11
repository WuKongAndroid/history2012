package org.treant.comicreader_netclient.adapter;

import java.util.List;
import java.util.Map;

import org.treant.comicreader_netclient.R;
import org.treant.comicreader_netclient.controller.DefaultAdapter;
import org.treant.comicreader_netclient.utils.Utils;
import org.treant.comicreader_netclient.utils.ViewHolder;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BookSpecialAdapter extends DefaultAdapter {

	public BookSpecialAdapter(List<Map<String, String>> list, Context context) {
		super(list, context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		bookMap = bookList.get(position);
		Log.i("map", bookMap.toString()+"->"+position);
		if (position == 0 && bookMap.get("topTitle").equals("special")) {
			View view = mLayoutInflater.inflate(R.layout.layout_book_recommend,null);
			ViewHolder.layout1=(RelativeLayout)view.findViewById(R.id.layout1);
			ViewHolder.layout1.setVisibility(View.VISIBLE);
			ViewHolder.bookType=(TextView)view.findViewById(R.id.bookType);
			ViewHolder.bookType.setText(bookMap.get("bookType"));
			ViewHolder.bookType.setVisibility(View.VISIBLE);
			
			ViewHolder.bookGrade=(RatingBar)view.findViewById(R.id.bookGrade);
			ViewHolder.updateTime=(TextView)view.findViewById(R.id.updateTime);
			ViewHolder.bookName=(TextView)view.findViewById(R.id.bookName);
			ViewHolder.bookContent=(TextView)view.findViewById(R.id.bookContent);
			
			ViewHolder.bookGrade.setRating(Float.parseFloat(bookMap.get("bookGradeNums")));
			ViewHolder.updateTime.setText("["+bookMap.get("updateTime")+"]");					
			ViewHolder.bookName.setText(bookMap.get("bookName"));			
			ViewHolder.bookContent.setText(bookMap.get("bookContent"));
			Utils.asyncLoadDrawable(view, R.id.bookCover, bookMap.get("bookCover"));
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
