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

public class VolunteerPrefs extends PreferenceActivity {

	Button okButton;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.preferences_with_ok);
		this.addPreferencesFromResource(R.xml.volunteer_prefs);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		prefs.edit().clear();
		PreferenceManager.setDefaultValues(this, R.xml.volunteer_prefs, true);

		okButton = (Button) findViewById(R.id.button1);
		okButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new SaveVolunteeringPreferencesTask().execute();
				finish();

			}
		});
	}

	public class LoadVolunteeringPreferencesTask extends
			AsyncTask<Void, Void, ArrayList<Boolean>> {
		/**
* 
*/
		public LoadVolunteeringPreferencesTask() {
			p = ProgressDialog.show(VolunteerPrefs.this, "",
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

			return API.getInstance(VolunteerPrefs.this)
					.retreiveVolunteeringPreferences();

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
						.getDefaultSharedPreferences(VolunteerPrefs.this);
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
				Toast.makeText(VolunteerPrefs.this, "Load failed",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	public class SaveVolunteeringPreferencesTask extends
			AsyncTask<Void, Void, Boolean> {

		/**
		 * 
		 */
		public SaveVolunteeringPreferencesTask() {
			p = ProgressDialog.show(VolunteerPrefs.this, "",
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
					.getDefaultSharedPreferences(VolunteerPrefs.this);
			Map<String, ?> keys = prefs.getAll();

			for (Map.Entry<String, ?> entry : keys.entrySet()) {
				Log.d("map values", entry.getKey() + ": "
						+ entry.getValue().toString());
				selected.add((Boolean) entry.getValue());
			}

			return API.getInstance(VolunteerPrefs.this)
					.saveVolunteeringPreferences(selected);
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
				Toast.makeText(VolunteerPrefs.this, "Save failed",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

}
