package org.treant.comicreader_netclient.controller;


import org.treant.comicreader_netclient.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
/*
 * �Զ���ĶԻ�������һ��    �Ի���ײ�ûȷ����ť,ֻ��һ��ȡ����ť , �м�Ϊ�ı�  �ұ�û��ѡ��ť. ��.setItems()  ����setSingleChoiceItems()
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
