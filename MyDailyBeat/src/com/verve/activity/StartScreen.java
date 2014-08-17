package com.verve.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.verve.Constants;
import com.verve.R;

public class StartScreen extends Activity {

	Button changeprofilepic, makeFriends, date, fling, social, volunteer, go;

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

		changeprofilepic = (Button) findViewById(R.id.button10);
		changeprofilepic.setOnClickListener(new View.OnClickListener() {

			@SuppressLint("InlinedApi")
			@Override
			public void onClick(View v) {

				if (Build.VERSION.SDK_INT < 19) {
					Intent intent = new Intent();
					intent.setType("image/jpeg");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(
							Intent.createChooser(intent, "Select Picture"),
							Constants.SELECT_PICTURE_REQUEST);
				} else {
					Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					intent.setType("image/jpeg");
					startActivityForResult(intent,
							Constants.SELECT_PICTURE_REQUEST_KITKAT);
				}

			}
		});

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

	// To handle when an image is selected from the browser, add the following
	// to your Activity
	@SuppressLint("NewApi")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {

			Uri originalUri = data.getData();

			if (requestCode == Constants.SELECT_PICTURE_REQUEST_KITKAT) {

				final int takeFlags = data.getFlags()
						& (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
				// Check for the freshest data.
				getContentResolver().takePersistableUriPermission(originalUri,
						takeFlags);

				/*
				 * now extract ID from Uri path using getLastPathSegment() and
				 * then split with ":" then call get Uri to for Internal storage
				 * or External storage for media I have used getUri()
				 */

				String id = originalUri.getLastPathSegment().split(":")[1];
				final String[] imageColumns = { MediaStore.Images.Media.DATA };
				final String imageOrderBy = null;

				Uri uri = getUri();
				String selectedImagePath = "path";

				Cursor imageCursor = managedQuery(uri, imageColumns,
						MediaStore.Images.Media._ID + "=" + id, null,
						imageOrderBy);

				if (imageCursor.moveToFirst()) {
					selectedImagePath = imageCursor.getString(imageCursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
					Log.d("path", selectedImagePath);
				}
			} else if (requestCode == Constants.SELECT_PICTURE_REQUEST) {
				String[] projection = { MediaStore.Images.Media.DATA };
				Cursor cursor = this.getContentResolver().query(originalUri,
						projection, null, null, null);
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				String fpath = cursor.getString(column_index);
				Log.d("path", fpath);
			}
		}
	}

	private Uri getUri() {
		String state = Environment.getExternalStorageState();
		if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
			return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

		return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	}

}
