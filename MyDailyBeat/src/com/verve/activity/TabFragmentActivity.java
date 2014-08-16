package com.verve.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.verve.Constants;
import com.verve.R;
import com.verve.api.API;
import com.verve.dialog.LoginDialog;

public class TabFragmentActivity extends FragmentActivity {

	protected android.support.v4.app.Fragment frag;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.tabs_layout);
		frag = this.getSupportFragmentManager().findFragmentById(
				R.id.tabs_fragment);

		final SharedPreferences mPrefs = getSharedPreferences(
				LoginDialog.PREFERENCES_KEY_OBSCURRED_USER_INFO, MODE_PRIVATE);
		String username = mPrefs.getString(
				LoginDialog.PREFERENCES_OBSCURRED_USER_INFO_SUBKEY_USERNAME,
				LoginDialog.DEFAULT_USERNAME);
		if (username.equalsIgnoreCase(LoginDialog.DEFAULT_USERNAME)) {
			startActivityForResult(new Intent(this, LoginDialog.class), Constants.LOGIN_REQUEST);
		} else {
			SharedPreferences prefs = getSharedPreferences(Constants.PREFS_TAG,
					0);

			boolean setupDone = prefs.getBoolean(Constants.SETUP_COMPLETE_TAG,
					false);
			if (!setupDone)
				startActivity(new Intent(this, GettingStarted.class));
			else
				new LoginTask().execute();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_action, menu);
		return true;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.addFriends:
			startActivity(new Intent(this, AddFriendsListActivity.class));
			break;
		case R.id.settings:
			startActivity(new Intent(this, StartScreen.class));
			break;
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent data) {

		if (reqCode == Constants.LOGIN_REQUEST) {
			if (resCode == RESULT_OK) {

			} else {
				finish();
			}
		}
	}

	public class LoginTask extends AsyncTask<Void, Void, Boolean> {

		ProgressDialog d;

		public LoginTask() {
			d = ProgressDialog.show(TabFragmentActivity.this, "",
					"Logging in...");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			d.show();
		}

		@Override
		protected Boolean doInBackground(Void... voids) {
			final SharedPreferences mPrefs = getSharedPreferences(
					LoginDialog.PREFERENCES_KEY_OBSCURRED_USER_INFO,
					MODE_PRIVATE);
			String username = mPrefs
					.getString(
							LoginDialog.PREFERENCES_OBSCURRED_USER_INFO_SUBKEY_USERNAME,
							LoginDialog.DEFAULT_USERNAME);
			String password = mPrefs
					.getString(
							LoginDialog.PREFERENCES_OBSCURRED_USER_INFO_SUBKEY_PASSWORD,
							LoginDialog.DEFAULT_PASSWORD);
			// Login call (passes success to onPostExecute)
			Boolean success = API.getInstance(TabFragmentActivity.this)
					.createSession(username, password);
			return success;
		}

		@Override
		protected void onPostExecute(Boolean success) {

			d.cancel();
			if (success) {
				/* Saved the user's credentials. */

				// return success
				//setResult(RESULT_OK);
				//finish();
			} else {
				finish();
			}
		}

	}

}
