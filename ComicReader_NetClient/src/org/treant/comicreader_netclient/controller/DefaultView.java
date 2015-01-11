package org.treant.comicreader_netclient.controller;

import java.util.Map;

import org.treant.comicreader_netclient.R;
import org.treant.comicreader_netclient.utils.Utils;
import org.treant.comicreader_netclient.utils.ViewHolder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

public class DefaultView {
	public static void showItems(View view, Map<String,String>bookMap){
		ViewHolder.bookName=(TextView)view.findViewById(R.id.bookName);
		ViewHolder.updateTime=(TextView)view.findViewById(R.id.updateTime);
		ViewHolder.bookGrade=(RatingBar)view.findViewById(R.id.bookGrade);
	}
}


//@Override
//public View getView(int position, View convertView, ViewGroup parent) {
//	// TODO Auto-generated method stub
//	convertView=null;
//	bookMap=bookList.get(position);
//	if(convertView==null){
//		convertView=mLayoutInflater.inflate(R.layout.layout_collection,	null);
//	}
//	ViewHolder.bookName=(TextView)convertView.findViewById(R.id.bookName);
//	ViewHolder.updateTime=(TextView)convertView.findViewById(R.id.updateTime);
//	ViewHolder.bookGrade=(RatingBar)convertView.findViewById(R.id.bookGrade);
//	ViewHolder.bookZipPath=(TextView)convertView.findViewById(R.id.bookZipPath);
//	
//	Utils.asyncLoadDrawable(convertView, R.id.bookCover, bookMap.get("bookCover"));
//	ViewHolder.bookName.setText(bookMap.get("bookName"));
//	ViewHolder.updateTime.setText("["+bookMap.get("updateTime")+"]");
//	ViewHolder.bookGrade.setRating(Float.parseFloat(bookMap.get("bookGradeNums")));
//	ViewHolder.bookZipPath.setText("Â·¾¶:"+bookMap.get("bookZipPath"));  //ÓÐÐÞ¸Ä
//	return convertView;
//}
