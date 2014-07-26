package com.verve;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddFriendsListAdapter extends BaseAdapter {
	
	public ArrayList<VerveStandardUser> friendList;
	public Context mContext;

	public AddFriendsListAdapter(Context c) {
		super();
		mContext = c;
		friendList = new ArrayList<VerveStandardUser>();
		loadMockData();
	}

	private void loadMockData() {
		friendList.add(new VerveStandardUser());
		friendList.add(new VerveStandardUser());
		friendList.add(new VerveStandardUser());
		friendList.add(new VerveStandardUser());
		friendList.add(new VerveStandardUser());
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return friendList.size();
	}

	@Override
	public VerveStandardUser getItem(int arg0) {
		// TODO Auto-generated method stub
		return friendList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		layoutParams.setMargins(5, 5, 5, 5);

		final View data = View.inflate(mContext,
				R.layout.status_block, null);
		
		TextView text = (TextView) data.findViewById(R.id.title);
		ImageView image = (ImageView) data.findViewById(R.id.user_pic_ImageView);
		
		text.setText(this.getItem(arg0).name);
		
		return data;
	}

}
