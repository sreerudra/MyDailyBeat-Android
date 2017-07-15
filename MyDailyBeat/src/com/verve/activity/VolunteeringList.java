package com.verve.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.verve.R;
import com.verve.VolunteeringListAdapter;

public class VolunteeringList extends Activity {

	ListView list;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_volunteering_opportunities);

		list = (ListView) findViewById(R.id.list);
		VolunteeringListAdapter adapter = new VolunteeringListAdapter(this);
	}

}
