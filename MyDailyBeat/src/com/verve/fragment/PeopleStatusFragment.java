package com.verve.fragment;

import java.util.Calendar;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.verve.R;
import com.verve.StatusUpdate;
import com.verve.StatusUpdateQueue;

public class PeopleStatusFragment extends Fragment {
	
	private View mRoot;
	

	public StatusUpdateQueue queue;

	protected LinearLayout scrollQueue;
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRoot = inflater.inflate(R.layout.status_inner_view, null);
		scrollQueue = (LinearLayout) mRoot.findViewById(R.id.scrollqueue);

		queue = new StatusUpdateQueue(getActivity().getApplicationContext());
		this.loadMockData();
		return mRoot;
	}


	public void deleteViewFromQueue(int id) {
		StatusUpdate ds = queue.removeItemWithKey(id);

	}

	

	private void loadMockData() {
		queue.addDataSetToQueue(new StatusUpdate("Virinchi Balabhadrapatruni", "6/15/14 6:15 PM", "is going out to see a movie!"));
		queue.addDataSetToQueue(new StatusUpdate("Srinivas Balabhadrapatruni", "6/15/14 12:30 PM", "is going out to see a movie!"));
		queue.addDataSetToQueue(new StatusUpdate("Kesava Rao Balabhadrapathruni", "6/15/14 9:15 AM", "is going out to see a movie!"));
		queue.addDataSetToQueue(new StatusUpdate("Suseela Balabhadrapathruni", "6/16/14 2:45 PM", "is going out to see a movie!"));
		queue.addDataSetToQueue(new StatusUpdate("Sai Vyas Balabhadrapatruni", "6/14/14 6:15 PM", "is going out to see a movie!"));
		Log.d("Tag", "Loading Mock Data");
		fillScrollQueue();
	}

	/* (non-Javadoc)
	 * @see com.verve.activity.StatusActivity#makeBlock(android.view.View, com.verve.StatusUpdate)
	 */
	protected void makeBlock(View view, StatusUpdate ds) {
		TextView tv = (TextView) view.findViewById(R.id.title);
		tv.setText(ds.name + " " + ds.lastEvent);

		TextView desc = (TextView) view.findViewById(R.id.subtitle);
		desc.setTextColor(Color.parseColor("#898f9c"));
		Calendar now = Calendar.getInstance();
		DateTime dNow = new DateTime(now.getTime());
		DateTime dThen = new DateTime(ds.when.getTime());

		if (Days.daysBetween(dThen, dNow).getDays() <= 0) {
			if (Hours.hoursBetween(dThen, dNow).getHours() <= 0) {
				if (Minutes.minutesBetween(dThen, dNow).getMinutes() <= 0) {
					if (Seconds.secondsBetween(dThen, dNow).getSeconds() <= 0) {
						desc.setText("Just now");
					} else {
						if (Seconds.secondsBetween(dThen, dNow).getSeconds() == 1) {
							desc.setText("1 second ago");
						} else {
							desc.setText(Seconds.secondsBetween(dThen, dNow).getSeconds() + " seconds ago");
						}
					}
				} else {
					if (Minutes.minutesBetween(dThen, dNow).getMinutes() == 1) {
						desc.setText("1 minute ago");
					} else {
						desc.setText(Minutes.minutesBetween(dThen, dNow).getMinutes() + " minutes ago");
					}
				}
			} else {
				if (Hours.hoursBetween(dThen, dNow).getHours() == 1) {
					desc.setText("1 hour ago");
				} else {
					desc.setText(Hours.hoursBetween(dThen, dNow).getHours() + " hours ago");
				}
			}
		} else {
			if (Days.daysBetween(dThen, dNow).getDays() == 1) {
				desc.setText("1 day ago");
			} else {
				desc.setText(Days.daysBetween(dThen, dNow).getDays() + " days ago");
			}
		}


	}
	
	protected void fillScrollQueue() {

		if (scrollQueue.getChildCount() > 0)
			scrollQueue.removeAllViews();

		for (final StatusUpdate ds : queue.mirrorQueue)
			addViewToScrollQueue(ds);

	}
	
	private String checkPrevious(String previous, LinearLayout scrollQueue,
			String ds) {

		LinearLayout space = new LinearLayout(getActivity().getApplicationContext());
		space.setPadding(0, 10, 0, 10);

		if ((!previous.equals(ds)) && (!previous.equals("")))
			scrollQueue.addView(space);

		return ds;
	}

	private void addViewToScrollQueue(final StatusUpdate ds) {

		String previous = "";
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		layoutParams.setMargins(5, 5, 5, 5);

		final View data = View.inflate(getActivity().getApplicationContext(),
				R.layout.status_block, null);

		makeBlock(data, ds);
		previous = checkPrevious(previous, scrollQueue, String.valueOf(ds.UPDATE_ID));

		scrollQueue.addView(data, layoutParams);
		data.setId(ds.UPDATE_ID);
		data.setContentDescription("" + ds.UPDATE_ID);

	}

}
