package com.evervecorp.mydailybeat.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.util.Date;

import com.evervecorp.mydailybeat.comm.*;
import com.evervecorp.mydailybeat.fragment.*;
import com.evervecorp.mydailybeat.list.*;
import com.evervecorp.mydailybeat.object.*;

import com.evervecorp.mydailybeat.R;

/**
 * Created by Virinchi on 4/10/2017.
 */

public class SignUpActivity extends FragmentActivity {

    private static final int NUM_PAGES = 4;

    private ViewPager pager;
    private FloatingActionButton fab;
    private User newUser;
    private RegistrationPagerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        newUser = new User();
        pager = (ViewPager) findViewById(R.id.pager);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        adapter = new RegistrationPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFragment current = (SignUpFragment) adapter.getItem(pager.getCurrentItem());
                if (current.isValid()) {
                    if (pager.getCurrentItem() == NUM_PAGES - 1) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                        for (int i = 1 ; i < NUM_PAGES ; i++) {
                            SignUpFragment item = (SignUpFragment) adapter.getItem(i);
                            Object[] data = item.getData();
                            if (i == 1) {
                                newUser.setFirstName((String) data[0]);
                                newUser.setLastName((String) data[1]);
                                newUser.setDob((Date) data[2]);
                                newUser.setZipCode((String) data[3]);
                            } else if (i == 2) {
                                newUser.setScreenName((String) data[0]);
                                newUser.setPassword((String) data[1]);
                            } else {
                                newUser.setEmailAddress((String) data[0]);
                                newUser.setMobilePh((String) data[1]);
                            }
                        }
                        new RegisterTask().execute(newUser);
                    } else {
                        if (pager.getCurrentItem() == NUM_PAGES - 2) {
                            fab.setImageResource(R.drawable.ic_go_white);
                        }
                        pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    }
                } else {
                    // TODO: Show errors
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            if (pager.getCurrentItem() == NUM_PAGES - 1) {
                fab.setImageResource(R.drawable.ic_next_white);
            }
            pager.setCurrentItem(pager.getCurrentItem() - 1, true);
        }

    }

    private class RegistrationPagerAdapter extends FragmentStatePagerAdapter {
        public RegistrationPagerAdapter(FragmentManager fm) {
            super(fm);
            this.messageFragment = new MessageFragment();
            messageFragment.setMessage("Welcome to MyDailyBeat!");
            this.personalInfoFragment = new PersonalInfoFragment();
            this.screenNameFragment = new ScreenNameFragment();
            this.emailMobileFragment = new EmailMobileFragment();
        }
        private MessageFragment messageFragment;
        private  PersonalInfoFragment personalInfoFragment;
        private ScreenNameFragment screenNameFragment;
        private EmailMobileFragment emailMobileFragment;

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return messageFragment;
            } else if (position == 1) {
                return personalInfoFragment;
            } else if (position == 2) {
                return screenNameFragment;
            } else if (position == 3){
                return emailMobileFragment;
            } else {
                return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    private class RegisterTask extends AsyncTask<User, Object, Boolean> {
        ProgressDialog mDialog;
        public RegisterTask() {
            mDialog = new ProgressDialog(SignUpActivity.this);
        }

        @Override
        protected Boolean doInBackground(User... params) {
            try {
                return RestAPI.getInstance(getApplicationContext()).createUser(params[0]);
            } catch (Exception e) {
                Log.e("MyDailyBeat", e.getLocalizedMessage());
                return false;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.setMessage("Creating user...");
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setIndeterminate(true);
            mDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            mDialog.hide();
            if (success) {
                SignUpActivity.this.setResult(RESULT_OK);
                SignUpActivity.this.finish();
            } else {
                // TODO: Show errors
            }
        }
    }
}


