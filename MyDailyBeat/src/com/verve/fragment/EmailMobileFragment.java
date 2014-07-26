package com.verve.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.verve.Constants;
import com.verve.R;
import com.verve.ScreenSlidePagerAdapter;
import com.verve.activity.StartScreen;
import com.verve.api.API;

public class EmailMobileFragment extends Fragment {

	Button go;
	public EditText email, mobile;

	private ScreenSlidePagerAdapter adapter;
	private ViewPager mPager;

	/**
	 * @return the adapter
	 */
	public ScreenSlidePagerAdapter getAdapter() {
		return adapter;
	}

	/**
	 * @param adapter
	 *            the adapter to set
	 */
	public void setAdapter(ScreenSlidePagerAdapter adapter) {
		this.adapter = adapter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.email_mobile_entry, container, false);
		
		email = (EditText) rootView.findViewById(R.id.email);
		mobile = (EditText) rootView.findViewById(R.id.mobile);

		go = (Button) rootView.findViewById(R.id.welcomeButton);
		go.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				new CreateUserTask().execute();

			}
		});

		return rootView;
	}
	
	public class CreateUserTask extends AsyncTask<Void, Void, Void> {
		
		boolean result;
		ProgressDialog d;
		
		public CreateUserTask() {
			d = ProgressDialog.show(getActivity(), "", "Creating User...");
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			d.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			result = API.getInstance(getActivity()).createStandardUser(adapter);
			return null;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Void r) {
			d.cancel();
			if (result) {
				Toast.makeText(getActivity(), "Created user successfully", Toast.LENGTH_SHORT).show();
			}
			
			SharedPreferences prefs = getActivity().getSharedPreferences(
					Constants.PREFS_TAG, 0);
			
			boolean setupDone = prefs.getBoolean(
					Constants.SETUP_COMPLETE_TAG, false);
			if (!setupDone)
				startActivity(new Intent(getActivity(),
						StartScreen.class));
			else {
				getActivity().finish();
			}
			
			
			
		}
		
	}
	
	public void setPager(ViewPager mPager) {
		this.mPager = mPager;
		
	}

}
