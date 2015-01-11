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

public class BookRecentlyReadAdapter extends DefaultAdapter {

	public BookRecentlyReadAdapter(List<Map<String, String>> list,
			Context context) {
		super(list, context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		bookMap = bookList.get(bookList.size()-1-position);  //Inverse Sequence Display
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.layout_recentlyread,null);
		}
		ViewHolder.bookName=(TextView)convertView.findViewById(R.id.bookName);
		ViewHolder.lastReadTime=(TextView)convertView.findViewById(R.id.lastReadTime);
		ViewHolder.pageIndex=(TextView)convertView.findViewById(R.id.pageIndex);
		ViewHolder.pageAmount=(TextView)convertView.findViewById(R.id.pageAmount);
		ViewHolder.bookGrade=(RatingBar)convertView.findViewById(R.id.bookGrade);
		
		Utils.asyncLoadDrawable(convertView, R.id.bookCover, bookMap.get("bookCover"));
		ViewHolder.bookName.setText(bookMap.get("bookName"));
		ViewHolder.lastReadTime.setText(bookMap.get("lastReadTime"));
		ViewHolder.pageIndex.setText(bookMap.get("lastReadPageIndex"));
		ViewHolder.pageAmount.setText(bookMap.get("pagesAmount"));
		ViewHolder.bookGrade.setRating(Float.parseFloat(bookMap.get("bookGradeNums")));
		return convertView;
	}

}
