package com.evervecorp.mydailybeat.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import com.evervecorp.mydailybeat.comm.*;
import com.evervecorp.mydailybeat.fragment.*;
import com.evervecorp.mydailybeat.list.*;
import com.evervecorp.mydailybeat.object.*;

import com.evervecorp.mydailybeat.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        final EditText user = (EditText) findViewById(R.id.userNameText);
        final EditText pass = (EditText) findViewById(R.id.passText);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userText = user.getText().toString();
                String passText = pass.getText().toString();
                new LoginTask().execute(userText, passText);
            }
        });

        Button signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
    }

    private class LoginTask extends AsyncTask<String, Void, List<Group>> {
        String uName, pass;
        ProgressDialog mDialog;
        public LoginTask() {
            mDialog = new ProgressDialog(MainActivity.this);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.setMessage("Logging in...");
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setIndeterminate(true);
            mDialog.show();
        }

        @Override
        protected List<Group> doInBackground(String... params) {
            uName = params[0];
            pass = params[1];
            try {
                boolean result = RestAPI.getInstance(MainActivity.this).login(uName, pass);
                if (RestAPI.getInstance(MainActivity.this).getCurrentUser() == null) {
                    return null;
                } else {
                    return RestAPI.getInstance(MainActivity.this).getGroupsForCurrentUser();
                }
            } catch (Exception e) {
                Log.e("MyDailyBeat", e.getLocalizedMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Group> result) {
            super.onPostExecute(result);
            mDialog.hide();
            if (result != null) {
                SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = preferences.edit();
                edit.putString("USER_NAME", uName);
                edit.putString("PASS", pass);
                edit.commit();
                Intent i = new Intent(MainActivity.this, MainNavActivity.class);
                ArrayList<Group> serial = new ArrayList<>();
                serial.addAll(result);
                i.putExtra("GROUPS", serial);
                startActivity(i);
            } else {
                // TODO: Show errors
            }
        }
    }
}
