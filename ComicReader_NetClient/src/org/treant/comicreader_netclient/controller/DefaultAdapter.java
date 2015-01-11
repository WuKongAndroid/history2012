package org.treant.comicreader_netclient.controller;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class DefaultAdapter extends BaseAdapter {
	protected List<Map<String, String>> bookList;
	protected Map<String, String> bookMap;
	protected LayoutInflater mLayoutInflater;

	public DefaultAdapter(List<Map<String, String>> list,
			Context context) {
		this.bookList = list;
		this.mLayoutInflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return (bookList!=null&bookList.size()>0)?bookList.size():0;
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
		return null;
	}

}
