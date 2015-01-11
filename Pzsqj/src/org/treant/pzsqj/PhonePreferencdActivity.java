package org.treant.pzsqj;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PhonePreferencdActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	private PhoneNotifier mNotifier;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mNotifier=new PhoneNotifier(this);
		mNotifier.updateNotification();
		SharedPreferences sharedPreferences=this.getPreferenceManager().getSharedPreferences();
		sharedPreferences.registerOnSharedPreferenceChangeListener(this);
		this.addPreferencesFromResource(R.xml.preferences);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		this.getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
		super.onDestroy();
	}
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sprefs, String key) {
		// TODO Auto-generated method stub
		if(key.equals("enabled")){
			mNotifier.updateNotification();
		}
			
	}

}
