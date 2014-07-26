package com.verve.activity;

import com.verve.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class VolunteerPrefs extends PreferenceActivity {

	/* (non-Javadoc)
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.addPreferencesFromResource(R.xml.volunteer_prefs);
	}

}
