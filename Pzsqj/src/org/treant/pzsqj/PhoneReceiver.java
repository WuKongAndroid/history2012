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
		// ����preferences
		SharedPreferences sprefs = PreferenceManager.getDefaultSharedPreferences(context);
		// ���������Ϣ
		String phone_state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
		String phone_number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

		if (phone_state.equals(TelephonyManager.EXTRA_STATE_RINGING)
				&& sprefs.getBoolean("enable", false)) {
			// ��� �������� ������
			if (sprefs.getBoolean("no_second_call", false)) {
				AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
				if (audioManager.getMode() == AudioManager.MODE_IN_CALL) {
					return;
				}
			}
			// �����ϵ���б� ������
			String which_contact = sprefs.getString("which_contact", "all");
			if (!which_contact.equals("all")) {
				int is_starred = isStarred(context, phone_number);
				if (which_contact.equals("contacts") && is_starred < 0) {
					return;
				} else if (which_contact.equals("starred") && is_starred < 1) {
					return;
				}
				// ����Service
				context.startService(new Intent(context, PhoneIntentService.class));
			}

		}

	}

	// �������contacts�� return -1 �����starred����1 ����starred����0
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
