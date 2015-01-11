package org.treant.pzsqj;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.TelephonyManager;

public class PhoneReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		// 加载preferences
		SharedPreferences sprefs = PreferenceManager.getDefaultSharedPreferences(context);
		// 检测来电信息
		String phone_state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
		String phone_number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

		if (phone_state.equals(TelephonyManager.EXTRA_STATE_RINGING)
				&& sprefs.getBoolean("enable", false)) {
			// 检测 二次来电 限制项
			if (sprefs.getBoolean("no_second_call", false)) {
				AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
				if (audioManager.getMode() == AudioManager.MODE_IN_CALL) {
					return;
				}
			}
			// 检测联系人列表 限制项
			String which_contact = sprefs.getString("which_contact", "all");
			if (!which_contact.equals("all")) {
				int is_starred = isStarred(context, phone_number);
				if (which_contact.equals("contacts") && is_starred < 0) {
					return;
				} else if (which_contact.equals("starred") && is_starred < 1) {
					return;
				}
				// 启动Service
				context.startService(new Intent(context, PhoneIntentService.class));
			}

		}

	}

	// 如果不在contacts中 return -1 如果是starred返回1 不是starred返回0
	private int isStarred(Context context, String number) {
		int starred = -1;
		Cursor cursor = context.getContentResolver().query(
				Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, number),
				new String[] { PhoneLookup.STARRED }, null, null, null);
		if(cursor!=null){
			if(cursor.moveToFirst()){
				starred=cursor.getInt(0);
			}
			cursor.close();
		}
		return starred;
	}

}
