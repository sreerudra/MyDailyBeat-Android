package com.verve.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.verve.Constants;
import com.verve.R;
import com.verve.api.API;

public class FlingPrefs extends PreferenceActivity {

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
		this.addPreferencesFromResource(R.xml.fling_prefs);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		prefs.edit().clear();
		PreferenceManager.setDefaultValues(this, R.xml.fling_prefs, true);
		new LoadFlingPreferencesTask().execute();

		okButton = (Button) findViewById(R.id.button1);
		okButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new SaveFlingPreferencesTask().execute();
				finish();

			}
		});
	}

	public class LoadFlingPreferencesTask extends
			AsyncTask<Void, Void, Pair<Integer, Integer>> {

		/**
* 
*/
		public LoadFlingPreferencesTask() {
			p = ProgressDialog.show(FlingPrefs.this, "",
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
		protected Pair<Integer, Integer> doInBackground(Void... arg0) {

			return API.getInstance(FlingPrefs.this).retrieveFlingPreferences();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Pair<Integer, Integer> result) {
			p.dismiss();
			if (result != null) {
				SharedPreferences prefs = PreferenceManager
						.getDefaultSharedPreferences(FlingPrefs.this);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("sexual_preference", result.first.toString());
				editor.putInt("age", result.second);
				editor.apply();
			} else {
				Toast.makeText(FlingPrefs.this, "Load failed",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	public class SaveFlingPreferencesTask extends
			AsyncTask<Void, Void, Boolean> {

		/**
		 * 
		 */
		public SaveFlingPreferencesTask() {
			p = ProgressDialog.show(FlingPrefs.this, "",
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

			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(FlingPrefs.this);

			return API.getInstance(FlingPrefs.this)
					.saveFlingPreferences(
							Integer.parseInt(prefs.getString(
									"sexual_preference", "0")),
							Integer.parseInt(prefs.getString("age",
									Constants.DEFAULT_AGE)));
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
				Toast.makeText(FlingPrefs.this, "Save failed",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

}
