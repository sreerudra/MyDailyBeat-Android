package edu.bu.met.vbalabhadrapatruni.mypeeps.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import edu.bu.met.vbalabhadrapatruni.mypeeps.list.DrawerListAdapter;
import edu.bu.met.vbalabhadrapatruni.mypeeps.R;
import edu.bu.met.vbalabhadrapatruni.mypeeps.list.SubtitleMenuItem;
import edu.bu.met.vbalabhadrapatruni.mypeeps.comm.RestAPI;
import edu.bu.met.vbalabhadrapatruni.mypeeps.object.Group;
import edu.bu.met.vbalabhadrapatruni.mypeeps.object.User;

public class MainNavActivity extends AppCompatActivity {

    DrawerLayout drawer;
    boolean firstTime = true;

    @Override
    protected void onResume() {
        super.onResume();
        if (!firstTime) {
            new GetGroupTask().execute();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firstTime = true;

        // instantiate the drawer and the toggle.
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // create the items in the left side "profile" menu
        ArrayList<SubtitleMenuItem> profileItems = new ArrayList<SubtitleMenuItem>();
        User currentUser = RestAPI.getInstance(this).getCurrentUser();
        profileItems.add(new SubtitleMenuItem("Name", currentUser.getName(), -1));
        profileItems.add(new SubtitleMenuItem("Email", currentUser.getEmailAddress(), -1));
        profileItems.add(new SubtitleMenuItem("Mobile", currentUser.getMobilePh(), -1));
        profileItems.add(new SubtitleMenuItem("Zip Code", currentUser.getZipCode(), -1));
        SimpleDateFormat format = new SimpleDateFormat("EEEE, MMM d, yyyy");
        profileItems.add(new SubtitleMenuItem("Date of Birth", format.format(currentUser.getDob()), -1));
        profileItems.add(new SubtitleMenuItem("Settings", "", R.drawable.ic_menu_manage));
        profileItems.add(new SubtitleMenuItem("Log Out", "", -1));

        // setup the profile list view using an adapter instance.
        NavigationView profileView = (NavigationView) findViewById(R.id.profile_view);
        ListView profileList = (ListView) findViewById(R.id.profile_view_items);
        DrawerListAdapter profileAdapter = new DrawerListAdapter(this, profileItems);
        profileList.setAdapter(profileAdapter);
        profileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 6) {
                    RestAPI.getInstance(MainNavActivity.this).logout();
                    SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = preferences.edit();
                    edit.remove("USER_NAME");
                    edit.remove("PASS");
                    edit.commit();
                    MainNavActivity.this.setResult(RESULT_OK);
                    MainNavActivity.this.finish();
                } else if (position == 5) {
                    // TODO: go to settings
                } else {
                    // do nothing
                }
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        Intent intent = getIntent();
        ArrayList<Group> groups = (ArrayList<Group>) intent.getSerializableExtra("GROUPS");
        setupForGroups(groups);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        firstTime = false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)){
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.right_drawer_open_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_button) {
            drawer.openDrawer(GravityCompat.END, true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setupForGroups(final List<Group> groups) {
        // create the items in the right side "navigation" menu
        final ArrayList<SubtitleMenuItem> menuItems = new ArrayList<>();

        for (Group group : groups) {
            menuItems.add(new SubtitleMenuItem(group.getName(), "", R.drawable.ic_group));
        }
        menuItems.add(new SubtitleMenuItem("Create Group", "", R.drawable.ic_new_group));

        // setup the menu list view using an adapter instance.
        NavigationView menuView = (NavigationView) findViewById(R.id.home_view);
        ListView menuList = (ListView) findViewById(R.id.menu_view_items);
        DrawerListAdapter menuAdapter = new DrawerListAdapter(this, menuItems);
        menuList.setAdapter(menuAdapter);
        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: Take action on clicked item.
                if (position < menuItems.size() - 1 && groups.size() > 0){
                    Group g = groups.get(position);
                    Intent intent = new Intent(MainNavActivity.this, GroupActivity.class);
                    intent.putExtra("GROUP", g);
                    startActivity(intent);
                } else {
                    MainNavActivity.this.createGroup();
                }
                drawer.closeDrawer(GravityCompat.END);
            }
        });

        ListView homeList = (ListView) findViewById(R.id.home_page_list);
        DrawerListAdapter homeAdapter = new DrawerListAdapter(this, menuItems);
        homeList.setAdapter(homeAdapter);
        homeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: Take action on clicked item.
                if (position < menuItems.size() - 1 && groups.size() > 0){
                    Group g = groups.get(position);
                    Intent intent = new Intent(MainNavActivity.this, GroupActivity.class);
                    intent.putExtra("GROUP", g);
                    startActivity(intent);
                } else {
                    MainNavActivity.this.createGroup();
                }
            }
        });
    }

    private void createGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainNavActivity.this);
        builder.setTitle("Create Group");
        final EditText groupNameText = new EditText(MainNavActivity.this);
        groupNameText.setHint("Group Name");
        builder.setView(groupNameText);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Create Group", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // create group
                String groupName = groupNameText.getText().toString();
                Log.d("MyPeeps", "Group Name: " + groupName);
                new CreateGroupTask(groupName).execute();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class CreateGroupTask extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog mDialog;
        String groupName;
        public CreateGroupTask(String groupName) {
            this.groupName = groupName;
            mDialog = new ProgressDialog(MainNavActivity.this);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.setMessage("Creating group...");
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setIndeterminate(true);
            mDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.d("MyPeeps", "Group Name 2: " + groupName);
            try {
                return RestAPI.getInstance(MainNavActivity.this).createGroup(groupName);
            } catch (Exception e) {
                Log.e("MyPeeps", e.getLocalizedMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            mDialog.hide();
            new GetGroupTask().execute();
        }
    }

    private class GetGroupTask extends AsyncTask<Void, Void, List<Group>> {
        ProgressDialog mDialog;
        public GetGroupTask() {
            mDialog = new ProgressDialog(MainNavActivity.this);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.setMessage("Getting groups...");
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setIndeterminate(true);
            mDialog.show();
        }

        @Override
        protected List<Group> doInBackground(Void... params) {
            try {
                return RestAPI.getInstance(MainNavActivity.this).getGroupsForCurrentUser();
            } catch (Exception e) {
                Log.e("MyPeeps", e.getLocalizedMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Group> groups) {
            super.onPostExecute(groups);
            mDialog.hide();
            if (groups != null) {
                MainNavActivity.this.setupForGroups(groups);
            } else {
                MainNavActivity.this.setupForGroups(new ArrayList<Group>());
            }
        }
    }
}
