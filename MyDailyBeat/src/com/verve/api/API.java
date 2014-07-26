package com.verve.api;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.verve.R;
import com.verve.ScreenSlidePagerAdapter;
import com.verve.VerveStandardUser;
import com.verve.fragment.EmailMobileFragment;
import com.verve.fragment.PersonalInfo1Fragment;
import com.verve.fragment.ScreenNameFragment;

public class API {
	
	private static API instance = null;
	private static VerveStandardUser currentUser;

	public static Context mContext;
	
	public final static String BASE_URL = "https://1-dot-mydailybeat-api.appspot.com/_ah/api/mydailybeat/v1";

	public static API getInstance(Context mContext) {
		if (instance == null) {
			instance = new API();
			API.mContext = mContext;
		}
		return instance;
	}
	
	public boolean createSession(String screenName, String password) {
		Log.d("api", "Inside createSession");
		String res;
		try {
			System.out.println("Inside try");
			res = makeRequest(BASE_URL, "users/getInfo",
					"screen_name=" + URLEncoder.encode(screenName, "UTF-8") + "&password="
							+ URLEncoder.encode(password, "UTF-8"), "GET", null);
			JSONObject result = new JSONObject(res);

			if (result.getString("name") != null) {
				currentUser = VerveStandardUser.userFromJSON(result);
				return true;
			}
		} catch (Exception e) {
			return false;
		}

		return false;
	}
	
	
	public boolean createStandardUser(ScreenSlidePagerAdapter adapter) {
		
		PersonalInfo1Fragment f1 = (PersonalInfo1Fragment) adapter.views.get(2);
		String name = f1.firstName.getText().toString() + " " + f1.lastName.getText().toString();
		String birth_month = mContext.getResources().getStringArray(R.array.months)[f1.spinMonth.getSelectedItemPosition()];
		long birth_year = Long.parseLong((String) f1.spinYear.getSelectedItem());
		String zip = f1.zipCode.getText().toString();
		
		ScreenNameFragment f2 = (ScreenNameFragment) adapter.views.get(3);
		String screenName = f2.screenName.getText().toString();
		String password = f2.pass1.getText().toString();
		
		EmailMobileFragment f3 = (EmailMobileFragment) adapter.views.get(4);
		String email = f3.email.getText().toString();
		String mobile = f3.mobile.getText().toString();
		
		JSONObject obj = new JSONObject();
		try {
			obj.put("name", name);
			obj.put("birth_month", birth_month);
			obj.put("birth_year", birth_year);
			obj.put("zipcode", zip);
			obj.put("screenName", screenName);
			obj.put("password", password);
			obj.put("email", email);
			obj.put("mobile", mobile);
			String response = makeRequest(BASE_URL, "users/register", "",
					"POST", obj);
			JSONObject res = new JSONObject(response);
			if (res.getString("response").equalsIgnoreCase("Operation succeeded")) {
				return true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		
		return false;
		
		
	}
	public static boolean hasConnectivity() {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}
	
	/**
	 * @return the currentUser
	 */
	public static VerveStandardUser getCurrentUser() {
		return currentUser;
	}

	private String makeRequest(String baseURL, String path, String parameters,
			String reqType, JSONObject postData) {

		Log.d("API", "makeRequest");

		byte[] mPostData = null;

		int mstat = 0;
		try {
			URL url = new URL(baseURL + "/" + path + "?" + parameters);
			System.out.println("Connect to: " + url);

			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			urlConnection.setRequestMethod(reqType);
			urlConnection.setRequestProperty("Accept", "application/json");
			// urlConnection.setDoOutput(true);
			if (postData != null) {
				System.out.println("Post data: " + postData);
				mPostData = postData.toString().getBytes();
				urlConnection.setRequestProperty("Content-Length",
						Integer.toString(mPostData.length));
				urlConnection.setRequestProperty("Content-Type",
						"application/json");
				OutputStream out = urlConnection.getOutputStream();
				out.write(mPostData);
				out.close();
			}

			mstat = urlConnection.getResponseCode();
			InputStream in;
			System.out.println("Status: " + mstat);
			if (mstat >= 200 && mstat < 300) {
				in = new BufferedInputStream(urlConnection.getInputStream());
			} else {
				in = new BufferedInputStream(urlConnection.getErrorStream());
			}
			try {
				ByteArrayOutputStream bo = new ByteArrayOutputStream();
				int i = in.read();
				while (i != -1) {
					bo.write(i);
					i = in.read();
				}
				return bo.toString();
			} catch (IOException e) {
				return "";
			} finally {
				in.close();
			}
		} catch (ConnectException ce) {
			System.err
					.println("Connection failed: ENETUNREACH (network not reachable)");
			ce.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "Error: status " + mstat;
	}

	
}
