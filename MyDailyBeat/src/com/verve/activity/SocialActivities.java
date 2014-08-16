package com.verve.activity;

import java.util.ArrayList;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.verve.R;
import com.verve.api.API;

public class SocialActivities extends PreferenceActivity {

	Button okButton;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.make_friends_layout);
		this.addPreferencesFromResource(R.xml.social_prefs);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		prefs.edit().clear();
		PreferenceManager.setDefaultValues(this, R.xml.social_prefs, true);
		new LoadSocialPreferencesTask().execute();

		okButton = (Button) findViewById(R.id.button1);
		okButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new SaveSocialPreferencesTask().execute();
				finish();

			}
		});
	}

	public class LoadSocialPreferencesTask extends
			AsyncTask<Void, Void, ArrayList<Boolean>> {
		/**
 * 
 */
		public LoadSocialPreferencesTask() {
			p = ProgressDialog.show(SocialActivities.this, "",
					"Loading Preferences...");
		}

		ProgressDialog p;

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			p.show();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected ArrayList<Boolean> doInBackground(Void... arg0) {

			return API.getInstance(SocialActivities.this)
					.retreiveSocialPreferences();

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(ArrayList<Boolean> result) {
			p.dismiss();
			if (result != null) {
				SharedPreferences prefs = PreferenceManager
						.getDefaultSharedPreferences(SocialActivities.this);
				Map<String, ?> keys = prefs.getAll();
				SharedPreferences.Editor edit = prefs.edit();
				int i = 0;
				for (Map.Entry<String, ?> entry : keys.entrySet()) {
					Log.d("map values", entry.getKey() + ": "
							+ entry.getValue().toString());
					edit.putBoolean(entry.getKey(), result.get(i++));
				}
				edit.apply();

			} else {
				Toast.makeText(SocialActivities.this, "Load failed",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	public class SaveSocialPreferencesTask extends
			AsyncTask<Void, Void, Boolean> {

		/**
		 * 
		 */
		public SaveSocialPreferencesTask() {
			p = ProgressDialog.show(SocialActivities.this, "",
					"Saving Preferences...");
		}

		ProgressDialog p;

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			p.show();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Boolean doInBackground(Void... arg0) {

			ArrayList<Boolean> selected = new ArrayList<Boolean>();

			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(SocialActivities.this);
			Map<String, ?> keys = prefs.getAll();

			for (Map.Entry<String, ?> entry : keys.entrySet()) {
				Log.d("map values", entry.getKey() + ": "
						+ entry.getValue().toString());
				selected.add((Boolean) entry.getValue());
			}

			return API.getInstance(SocialActivities.this)
					.saveSocialPreferences(selected);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Boolean result) {
			p.dismiss();
			if (result) {
				finish();
			} else {
				Toast.makeText(SocialActivities.this, "Save failed",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

}
