package com.verve.activity;

import com.verve.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class MakeFriendsPrefs extends PreferenceActivity {

	/* (non-Javadoc)
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.make_friends_layout);
		this.addPreferencesFromResource(R.xml.make_friends_prefs);
	}

}
