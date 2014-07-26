package com.verve.fragment;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.verve.R;
import com.verve.ScreenSlidePagerAdapter;

public class PersonalInfo1Fragment extends Fragment {
	
	private ScreenSlidePagerAdapter adapter;
	public Spinner spinYear;
	public Spinner spinMonth;
	public EditText firstName;
	public EditText lastName;
	public EditText zipCode;
	private ViewPager mPager;
	private Button next;
	
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
				R.layout.personal_info_1, container, false);
		ArrayList<String> years = new ArrayList<String>();
		int thisYear = Calendar.getInstance().get(Calendar.YEAR);
		for (int i = thisYear; i >= 1900; i--) {
			years.add(Integer.toString(i));
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, years);

		spinYear = (Spinner) rootView.findViewById(R.id.spinner2);
		spinYear.setAdapter(adapter);
		
		firstName = (EditText) rootView.findViewById(R.id.firstName);
		lastName = (EditText) rootView.findViewById(R.id.lastName);
		spinMonth = (Spinner) rootView.findViewById(R.id.spinner1);
		zipCode = (EditText) rootView.findViewById(R.id.zipCode);
		
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
