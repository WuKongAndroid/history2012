package org.treant.comicreader_netclient.adapter;

import java.util.List;
import java.util.Map;

import org.treant.comicreader_netclient.R;
import org.treant.comicreader_netclient.controller.DefaultAdapter;
import org.treant.comicreader_netclient.utils.Utils;
import org.treant.comicreader_netclient.utils.ViewHolder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

public class BookCollectionAdapter extends DefaultAdapter {

	public BookCollectionAdapter(List<Map<String, String>> list, Context context) {
		super(list, context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		bookMap=bookList.get(position);
		if(convertView==null){
			convertView=mLayoutInflater.inflate(R.layout.layout_collection,	null);
		}
		ViewHolder.bookName=(TextView)convertView.findViewById(R.id.bookName);
		ViewHolder.updateTime=(TextView)convertView.findViewById(R.id.updateTime);
		ViewHolder.bookGrade=(RatingBar)convertView.findViewById(R.id.bookGrade);
		
		Utils.asyncLoadDrawable(convertView, R.id.bookCover, bookMap.get("bookCover"));
		ViewHolder.bookName.setText(bookMap.get("bookName"));
		ViewHolder.updateTime.setText("["+bookMap.get("updateTime")+"]");
		ViewHolder.bookGrade.setRating(Float.parseFloat(bookMap.get("bookGradeNums")));
		return convertView;
	}

}
