package org.treant.comicreader_netclient.adapter;

import java.util.List;
import java.util.Map;

import org.treant.comicreader_netclient.R;
import org.treant.comicreader_netclient.utils.Constant;
import org.treant.comicreader_netclient.utils.Utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BookTypesGridAdapter extends BaseAdapter {
	private Context context;
	private List<Map<String, String>> list;
	private LayoutInflater mLayoutInflater;

	public BookTypesGridAdapter(List<Map<String, String>> list, Context context) {
		this.context = context;
		this.list = list;
		this.mLayoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list != null && list.size() > 0) {
			return list.size();
		} else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewWrapper vw;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.grid_item, null);
			vw = new ViewWrapper();
			vw.imageView = (ImageView) convertView.findViewById(R.id.itemImage);
			vw.typeText = (TextView) convertView.findViewById(R.id.itemText);
			vw.countText = (TextView) convertView.findViewById(R.id.count);
			convertView.setTag(vw);
		} else {
			vw = (ViewWrapper) convertView.getTag();
		}
		Map<String, String> map = list.get(position);
		List<Integer> drawableList = Utils.loadDrawableByReflect("g");
		vw.imageView.setImageResource(drawableList.get(position));
		String bookCategory = map.get("cname");
		vw.typeText.setText(bookCategory);
		int count = Utils.getBooInfoCountsByAttribute(Constant.BOOKINFO_CACHE_PATH, "bookCategory", bookCategory);
		if(count==0){
			Log.i("type=0", bookCategory+"->"+count);
			convertView.setVisibility(View.GONE);
		}
		vw.countText.setText(String.valueOf(count));
		Animation animation = AnimationUtils.loadAnimation(context,
				R.anim.alpha_emerge);
		vw.countText.startAnimation(animation);
		return convertView;
	}
}

class ViewWrapper {
	ImageView imageView;
	TextView typeText;
	TextView countText;
}
