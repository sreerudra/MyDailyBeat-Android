package com.verve.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

import com.verve.R;

public class TabsFragment extends Fragment implements OnTabChangeListener {

	private static final String TAG = "FragmentTabs";
	public static final String TAB_PEOPLE = "PEOPLE";
	public static final String TAB_MESSAGING = "MESSAGING";
	public static final String TAB_HOME = "HOME";
	public static final String TAB_SHOPPING = "SHOPPING";
	public static final String TAB_FLING = "FLING";
	public static final String TAB_VOLUNTEERING = "VOLUNTEERING";

	private View mRoot;
	private TabHost mTabHost;
	private int mCurrentTab = 2;
	Handler h = new Handler();

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRoot = inflater.inflate(R.layout.status_page, null);
		mTabHost = (TabHost) mRoot.findViewById(android.R.id.tabhost);
		setupTabs();
		return mRoot;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);

		mTabHost.setOnTabChangedListener(this);
		mTabHost.setCurrentTab(mCurrentTab);
		// manually start loading stuff in the first tab
		updateTab(TAB_HOME, R.id.tab_3);
		h.post(new Runnable(){

			@Override
			public void run() {
				switch (mCurrentTab) {
				case 0:
					updateTab(TAB_PEOPLE, R.id.tab_1);
					break;
				case 1:
					updateTab(TAB_MESSAGING, R.id.tab_2);
					break;
				case 2:
					updateTab(TAB_HOME, R.id.tab_3);
					break;
				case 3:
					updateTab(TAB_SHOPPING, R.id.tab_4);
					break;
				case 5:
					updateTab(TAB_FLING, R.id.tab_5);
					break;
				case 6:
					updateTab(TAB_VOLUNTEERING, R.id.tab_6);
					break;
				}
				
				h.postDelayed(this, 300000);
				
			}
			
		});
	}

	private void setupTabs() {
		mTabHost.setup(); // you must call this before adding your tabs!
		mTabHost.addTab(newTab(TAB_PEOPLE, R.id.tab_1, R.drawable.people));
		mTabHost.addTab(newTab(TAB_MESSAGING, R.id.tab_2, R.drawable.messaging));
		mTabHost.addTab(newTab(TAB_HOME, R.id.tab_3, R.drawable.home));
		mTabHost.addTab(newTab(TAB_SHOPPING, R.id.tab_4, R.drawable.shopping));
		mTabHost.addTab(newTab(TAB_FLING, R.id.tab_5, R.drawable.fling));
		mTabHost.addTab(newTab(TAB_VOLUNTEERING, R.id.tab_6, R.drawable.people));
	}

	private TabSpec newTab(String tag, int tabContentId, int imageResId) {
		Log.d(TAG, "buildTab(): tag=" + tag);

		TabSpec tabSpec = mTabHost.newTabSpec(tag);
		tabSpec.setIndicator(tag,
				getActivity().getResources().getDrawable(imageResId));
		tabSpec.setContent(tabContentId);
		return tabSpec;
	}

	@Override
	public void onTabChanged(String tabId) {
		Log.d(TAG, "onTabChanged(): tabId=" + tabId);
		if (TAB_PEOPLE.equals(tabId)) {
			updateTab(tabId, R.id.tab_1);
			mCurrentTab = 0;
			return;
		} else if (TAB_MESSAGING.equals(tabId)) {
			updateTab(tabId, R.id.tab_2);
			mCurrentTab = 1;
			return;
		} else if (TAB_HOME.equals(tabId)) {
			updateTab(tabId, R.id.tab_3);
			mCurrentTab = 2;
			return;
		} else if (TAB_SHOPPING.equals(tabId)) {
			updateTab(tabId, R.id.tab_4);
			mCurrentTab = 3;
			return;
		} else if (TAB_FLING.equals(tabId)) {
			updateTab(tabId, R.id.tab_5);
			mCurrentTab = 4;
			return;
		} else if (TAB_VOLUNTEERING.equals(tabId)) {
			updateTab(tabId,R.id.tab_6);
			mCurrentTab = 5;
			return;
		}
	}

	private void updateTab(String tabId, int placeholder) {
		FragmentManager fm = getFragmentManager();
		if (fm.findFragmentByTag(tabId) == null) {
			if (TAB_PEOPLE.equals(tabId)) {
				fm.beginTransaction()
						.replace(placeholder, new PeopleStatusFragment(), tabId)
						.commit();
			} else if (TAB_MESSAGING.equals(tabId)) {
				fm.beginTransaction()
						.replace(placeholder, new MessageStatusFragment(),
								tabId).commit();
			} else if (TAB_HOME.equals(tabId)) {
				fm.beginTransaction()
						.replace(placeholder, new HomeStatusFragment(), tabId)
						.commit();
			} else if (TAB_SHOPPING.equals(tabId)) {
				fm.beginTransaction()
						.replace(placeholder, new ShoppingStatusFragment(),
								tabId).commit();
			} else if (TAB_FLING.equals(tabId)) {
				fm.beginTransaction()
						.replace(placeholder, new FlingStatusFragment(), tabId)
						.commit();
			} else if (TAB_VOLUNTEERING.equals(tabId)) {
				fm.beginTransaction()
				.replace(placeholder, new VolunteeringStatusFragment(), tabId)
				.commit();
			}
		}

	}
}
