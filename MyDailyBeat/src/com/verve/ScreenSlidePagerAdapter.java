package com.verve;

import java.util.ArrayList;

import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.verve.fragment.EmailMobileFragment;
import com.verve.fragment.PersonalInfo1Fragment;
import com.verve.fragment.ScreenNameFragment;
import com.verve.fragment.WelcomeFragment;
import com.verve.fragment.WelcomeFragment2;

/**
 * A simple pager adapter that represents 7 ScreenSlidePageFragment objects, in
 * sequence.
 */
public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
	
	public ArrayList<android.app.Fragment> views;
	public ViewPager mPager;

    /**
	 * @return the mPager
	 */
	public ViewPager getPager() {
		return mPager;
	}

	/**
	 * @param mPager the mPager to set
	 */
	public void setPager(ViewPager mPager) {
		this.mPager = mPager;
	}

	public ScreenSlidePagerAdapter(android.app.FragmentManager fm) {
		super(fm);
		views = new ArrayList<android.app.Fragment>();
		WelcomeFragment f1 = new WelcomeFragment();
		f1.setAdapter(this);
		f1.setPager(mPager);
		WelcomeFragment2 f2 = new WelcomeFragment2();
		f2.setAdapter(this);
		f2.setPager(mPager);
		PersonalInfo1Fragment f3 = new PersonalInfo1Fragment();
		f3.setAdapter(this);
		f3.setPager(mPager);
		ScreenNameFragment f4 = new ScreenNameFragment();
		f4.setAdapter(this);
		f4.setPager(mPager);
		EmailMobileFragment f5 = new EmailMobileFragment();
		f5.setAdapter(this);
		f5.setPager(mPager);
		views.add(f1);
		views.add(f2);
		views.add(f3);
		views.add(f4);
		views.add(f5);
	}

	@Override
    public android.app.Fragment getItem(int position) {
        return views.get(position);
    }

    @Override
    public int getCount() {
        return views.size();
    }
}