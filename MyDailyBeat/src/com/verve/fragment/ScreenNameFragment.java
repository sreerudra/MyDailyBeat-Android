package com.verve.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.verve.R;
import com.verve.ScreenSlidePagerAdapter;

public class ScreenNameFragment extends Fragment {
	
	private ScreenSlidePagerAdapter adapter;
	public EditText screenName, pass1, pass2;
	private ViewPager mPager;
	private Button next;
	
	/**
	 * @return the adapter
	 */
	public ScreenSlidePagerAdapter getAdapter() {
		return adapter;
	}

	/**
	 * @param adapter the adapter to set
	 */
	public void setAdapter(ScreenSlidePagerAdapter adapter) {
		this.adapter = adapter;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.screen_name_and_password, container, false);
		
		screenName = (EditText) rootView.findViewById(R.id.screenName);
		pass1 = (EditText) rootView.findViewById(R.id.pass1);
		pass2 = (EditText) rootView.findViewById(R.id.pass2);
		
		next = (Button) rootView.findViewById(R.id.next);
		next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);
				
			}
		});

        return rootView;
	}
	
	public void setPager(ViewPager mPager) {
		this.mPager = mPager;
		
	}
	
}
