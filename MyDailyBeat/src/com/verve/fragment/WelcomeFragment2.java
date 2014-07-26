package com.verve.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.verve.R;
import com.verve.ScreenSlidePagerAdapter;



public class WelcomeFragment2 extends android.app.Fragment {
	
	private ScreenSlidePagerAdapter adapter;
	private ViewPager mPager;
	private Button next;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.welcome2, container, false);
		next = (Button) rootView.findViewById(R.id.next);
		next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);
				
			}
		});

        return rootView;
	}

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
	
	public void setPager(ViewPager mPager) {
		this.mPager = mPager;
		
	}
	
}
