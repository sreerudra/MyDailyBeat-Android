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

public class MakeFriendsPrefs extends PreferenceActivity {

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
		this.addPreferencesFromResource(R.xml.make_friends_prefs);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		prefs.edit().clear();
		PreferenceManager
				.setDefaultValues(this, R.xml.make_friends_prefs, true);
		new LoadHobbyPreferencesTask().execute();

		okButton = (Button) findViewById(R.id.button1);
		okButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new SaveHobbyPreferencesTask().execute();
				finish();

			}
		});
	}

	public class LoadHobbyPreferencesTask extends
			AsyncTask<Void, Void, ArrayList<Boolean>> {
		/**
		 * 
		 */
		public LoadHobbyPreferencesTask() {
			p = ProgressDialog.show(MakeFriendsPrefs.this, "",
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

			return API.getInstance(MakeFriendsPrefs.this)
					.retreiveHobbyPreferences();

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
						.getDefaultSharedPreferences(MakeFriendsPrefs.this);
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
				Toast.makeText(MakeFriendsPrefs.this, "Loading failed",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	public class SaveHobbyPreferencesTask extends
			AsyncTask<Void, Void, Boolean> {

		/**
		 * 
		 */
		public SaveHobbyPreferencesTask() {
			p = ProgressDialog.show(MakeFriendsPrefs.this, "",
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
					.getDefaultSharedPreferences(MakeFriendsPrefs.this);
			Map<String, ?> keys = prefs.getAll();

			for (Map.Entry<String, ?> entry : keys.entrySet()) {
				Log.d("map values", entry.getKey() + ": "
						+ entry.getValue().toString());
				selected.add((Boolean) entry.getValue());
			}

			return API.getInstance(MakeFriendsPrefs.this).saveHobbyPreferences(
					selected);
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
				Toast.makeText(MakeFriendsPrefs.this, "Save failed",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

}
