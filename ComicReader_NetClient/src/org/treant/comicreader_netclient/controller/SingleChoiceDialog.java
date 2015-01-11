package org.treant.comicreader_netclient.controller;


import org.treant.comicreader_netclient.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
/*
 * �Զ������        �Ի����ұߴ���ѡ��ť,�ײ���ȷ����ȡ����ť    .setSingleChoiceItems()
 */
public abstract class SingleChoiceDialog extends AlertDialog.Builder {

	public SingleChoiceDialog(Context context, int itemsId) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setPositiveButton(R.string.confirm,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						positiveAction();
					}

				})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}

						})
				.setSingleChoiceItems(itemsId, -1,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								singleChoiceAction(which);
							}
						});

	}

	public abstract void positiveAction();

	public abstract void singleChoiceAction(int which);

}
