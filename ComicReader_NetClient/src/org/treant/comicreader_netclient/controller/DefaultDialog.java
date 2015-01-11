package org.treant.comicreader_netclient.controller;


import org.treant.comicreader_netclient.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
/*
 * 自定义的对话框基类的一种    对话框底部没确定按钮,只有一个取消按钮 , 中间为文本  右边没单选按钮. 用.setItems()  不用setSingleChoiceItems()
 */
public abstract class DefaultDialog extends AlertDialog.Builder{

	public DefaultDialog(Context context, int itemsId) {
		super(context);
		// TODO Auto-generated constructor stub
		DefaultDialog.this.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		}).setItems(itemsId, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				itemsAction(which);
			}
		});
	}

	public abstract void itemsAction(int which);
}
