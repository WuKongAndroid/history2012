package org.treant.pzsqj;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PhoneNotifier {
	private static final int NOTIFICATION_ID=1;
	
	private Context mContext;
	private NotificationManager mNotificationManager;
	private SharedPreferences mSharedPreferences;
	public PhoneNotifier(Context context){
		this.mContext=context;
		this.mNotificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		this.mSharedPreferences=PreferenceManager.getDefaultSharedPreferences(context);
	}
	//update Notification
	public void updateNotification(){
		if(mSharedPreferences.getBoolean("enabled", false)){
			this.enableNotification();
		}else{
			this.disableNotification();
		}
	}
	
	private void enableNotification(){
		//关闭自动应答的intent
		Intent notificationIntent=new Intent(mContext, PhonePreferencdActivity.class);
		PendingIntent pendingIntent=PendingIntent.getActivity(mContext, 0, notificationIntent, 0);
		//创建notification
		Notification notification=new Notification(R.drawable.right,null,0);
		notification.flags |=Notification.FLAG_ONGOING_EVENT|Notification.FLAG_NO_CLEAR;
		notification.setLatestEventInfo(mContext, mContext.getString(R.string.notification_title), 
				mContext.getString(R.string.notification_text), pendingIntent);
		mNotificationManager.notify(NOTIFICATION_ID, notification);
	}
	
	private void disableNotification(){
		mNotificationManager.cancel(NOTIFICATION_ID);
	}
}
