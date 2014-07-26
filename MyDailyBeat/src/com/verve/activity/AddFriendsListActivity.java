package com.verve.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.verve.AddFriendsListAdapter;
import com.verve.R;

public class AddFriendsListActivity extends Activity {
	
	ListView list;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addfriends_list);

		list = (ListView) findViewById(R.id.list);
		AddFriendsListAdapter adapter = new AddFriendsListAdapter(this);
		
		list.setAdapter(adapter);
	}
	
	
	

}
