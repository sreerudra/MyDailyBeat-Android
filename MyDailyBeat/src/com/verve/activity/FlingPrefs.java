package com.verve.activity;

import com.verve.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class FlingPrefs extends PreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.addPreferencesFromResource(R.xml.fling_prefs);
	}

}
