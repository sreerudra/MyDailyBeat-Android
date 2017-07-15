package com.verve.activity;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.verve.Constants;
import com.verve.R;
import com.verve.api.API;

public class StartScreen extends Activity {

	

	Button changeprofilepic, makeFriends, date, fling, social, volunteer, go;
	ImageView profilePic;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main1);

		SharedPreferences prefs = getSharedPreferences(Constants.PREFS_TAG, 0);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(Constants.SETUP_COMPLETE_TAG, true);
		editor.commit();

		profilePic = (ImageView) findViewById(R.id.imageView1);
		new GetServingURLTask().execute();

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

		go = (Button) findViewById(R.id.button9);
		go.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	public class UploadProfilePictureTask extends
			AsyncTask<Void, Void, Boolean> {

		ProgressDialog p = ProgressDialog.show(StartScreen.this, "",
				"Uploading profile picture...");
		File fileToUpload;

		public UploadProfilePictureTask(File fileToUpload) {
			super();
			this.fileToUpload = fileToUpload;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			p.show();
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			try {
				return API.getInstance(StartScreen.this).uploadProfilePicture(
						fileToUpload);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (result) {
				Toast.makeText(StartScreen.this, "Upload successful!",
						Toast.LENGTH_SHORT).show();
				finish();
			} else {
				Toast.makeText(StartScreen.this, "Upload failed!",
						Toast.LENGTH_SHORT).show();
			}
		}

	}
	
public class LoadProfilePictureTask extends AsyncTask<URL, Void, Boolean> {
		
		ProgressDialog p = ProgressDialog.show(StartScreen.this, "", "Loading profile picture...");
		Bitmap image;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			p.show();
		}

		@Override
		protected Boolean doInBackground(URL... params) {
			try {
				if ((image = BitmapFactory.decodeStream(params[0].openStream())) != null) {
					return true;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			p.cancel();
			
			if (result) {
				profilePic.setImageBitmap(image);
			}
		}

	}

	public class GetServingURLTask extends AsyncTask<Void, Void, URL> {
		
		ProgressDialog p = ProgressDialog.show(StartScreen.this, "", "Getting profile picture location...");

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			p.show();
		}

		@Override
		protected URL doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return API.getInstance(StartScreen.this).getServingURLForProfilePicture();
		}

		@Override
		protected void onPostExecute(URL result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			p.cancel();
			
			if (result != null) {
				new LoadProfilePictureTask().execute(result);
			}
		}

	}

}
