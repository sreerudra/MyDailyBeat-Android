package com.verve.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.verve.Constants;
import com.verve.R;

public class StartScreen extends Activity {

	Button makeFriends, date, fling, social, volunteer;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main1);
		
		SharedPreferences prefs = getSharedPreferences(
				Constants.PREFS_TAG, 0);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(Constants.SETUP_COMPLETE_TAG, true);
		editor.commit();
		
		makeFriends = (Button) findViewById(R.id.button1);
		makeFriends.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(new Intent(StartScreen.this,
						MakeFriendsPrefs.class));

			}
		});
		date = (Button) findViewById(R.id.button2);
		date.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(new Intent(StartScreen.this,
						DateExclusivelyPrefs.class));

			}
		});
		fling = (Button) findViewById(R.id.button3);
		fling.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(new Intent(StartScreen.this, FlingPrefs.class));

			}
		});

		social = (Button) findViewById(R.id.button4);
		social.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(StartScreen.this,
						SocialActivities.class));

			}
		});

		volunteer = (Button) findViewById(R.id.button8);
		volunteer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(new Intent(StartScreen.this, VolunteerPrefs.class));

			}
		});
		
	}

}
