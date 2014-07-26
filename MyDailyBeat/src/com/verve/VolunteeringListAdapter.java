package com.verve;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VolunteeringListAdapter extends BaseAdapter {
	
	public ArrayList<VolunteeringOpportunity> list;
	public Context c;

	public VolunteeringListAdapter(Context c) {
		this.c = c;
		list = new ArrayList<VolunteeringOpportunity>();
		this.loadMockData();
	}

	private void loadMockData() {
		list.add(new VolunteeringOpportunity("Event 1", "This is a test event", "Test Location", Calendar.getInstance()));
		list.add(new VolunteeringOpportunity("Event 2", "This is another test event", "Test Location 2", Calendar.getInstance()));
		list.add(new VolunteeringOpportunity("Event 3", "This is yet another test event", "Test Location 3", Calendar.getInstance()));
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public VolunteeringOpportunity getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		layoutParams.setMargins(5, 5, 5, 5);

		final View data = View.inflate(c,
				R.layout.new_opportunities_inner, null);
		
		//data.setLayoutParams(layoutParams);
		
		TextView text = (TextView) data.findViewById(R.id.text);
		TextView details = (TextView) data.findViewById(R.id.detailsText);
		
		text.setText(this.getItem(arg0).getTitle());
		details.setText(this.getItem(arg0).getDetails());
		details.setTextColor(Color.WHITE);
				
		return data;
	}

}
