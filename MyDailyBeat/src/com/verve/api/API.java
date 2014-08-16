package com.verve.api;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;

import com.verve.R;
import com.verve.ScreenSlidePagerAdapter;
import com.verve.Statics;
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
			res = makeRequest(BASE_URL, "users/getInfo", "screen_name="
					+ URLEncoder.encode(screenName, "UTF-8") + "&password="
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
		String name = f1.firstName.getText().toString() + " "
				+ f1.lastName.getText().toString();
		String birth_month = mContext.getResources().getStringArray(
				R.array.months)[f1.spinMonth.getSelectedItemPosition()];
		long birth_year = Long
				.parseLong((String) f1.spinYear.getSelectedItem());
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
			if (res.getString("response").equalsIgnoreCase(
					"Operation succeeded")) {
				return true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

		return false;

	}

	public Pair<Integer, Integer> retrieveRelationshipPreferences() {
		String res;
		try {
			res = makeRequest(
					BASE_URL,
					"users/prefs/relationship/retrieve",
					"screen_name="
							+ URLEncoder
									.encode(currentUser.screenName, "UTF-8")
							+ "&password="
							+ URLEncoder.encode(currentUser.password, "UTF-8"),
					"GET", null);
			JSONObject result = new JSONObject(res);

			JSONArray arr = result.getJSONArray("prefs");
			JSONObject sex = arr.getJSONObject(0);
			JSONObject age = arr.getJSONObject(1);
			return Pair.create(Integer.valueOf(sex.getInt("index")),
					Integer.valueOf(age.getInt("data")));

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public boolean saveRelationshipPreferences(int index, int age) {
		JSONObject postData = new JSONObject();

		try {
			postData.put("screenName", currentUser.screenName);
			postData.put("password", currentUser.password);

			JSONArray strings = new JSONArray();
			strings.put("Man looking for Woman");
			strings.put("Woman looking for Man");
			strings.put("Man looking for Man");
			strings.put("Woman looking for Woman");
			strings.put("Couple looking for Couple");
			strings.put("Bisexual looking for Bisexual");

			JSONObject pref = new JSONObject();
			pref.put("strings", strings);
			pref.put("index", index);

			JSONArray prefArray = new JSONArray();
			prefArray.put(0, pref);
			prefArray.put(1, age);

			postData.put("prefs", prefArray);

			String response = makeRequest(BASE_URL,
					"users/prefs/relationship/save", "", "POST", postData);
			JSONObject res = new JSONObject(response);
			if (res.getString("response").equalsIgnoreCase(
					"Operation succeeded")) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return false;
	}

	public Pair<Integer, Integer> retrieveFlingPreferences() {
		String res;
		try {
			res = makeRequest(
					BASE_URL,
					"users/prefs/fling/retrieve",
					"screen_name="
							+ URLEncoder
									.encode(currentUser.screenName, "UTF-8")
							+ "&password="
							+ URLEncoder.encode(currentUser.password, "UTF-8"),
					"GET", null);
			JSONObject result = new JSONObject(res);

			JSONArray arr = result.getJSONArray("prefs");
			JSONObject sex = arr.getJSONObject(0);
			JSONObject age = arr.getJSONObject(1);
			return Pair.create(Integer.valueOf(sex.getInt("index")),
					Integer.valueOf(age.getInt("data")));

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public boolean saveFlingPreferences(int index, int age) {
		JSONObject postData = new JSONObject();

		try {
			postData.put("screenName", currentUser.screenName);
			postData.put("password", currentUser.password);

			JSONArray strings = new JSONArray();
			strings.put("Man looking for Woman");
			strings.put("Woman looking for Man");
			strings.put("Man looking for Man");
			strings.put("Woman looking for Woman");
			strings.put("Couple looking for Couple");
			strings.put("Bisexual looking for Bisexual");

			JSONObject pref = new JSONObject();
			pref.put("strings", strings);
			pref.put("index", index);

			JSONArray prefArray = new JSONArray();
			prefArray.put(0, pref);
			prefArray.put(1, age);

			postData.put("prefs", prefArray);

			String response = makeRequest(BASE_URL, "users/prefs/fling/save",
					"", "POST", postData);
			JSONObject res = new JSONObject(response);
			if (res.getString("response").equalsIgnoreCase(
					"Operation succeeded")) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return false;
	}

	public boolean saveVolunteeringPreferences(ArrayList<Boolean> selected) {
		JSONObject postData = new JSONObject();

		try {
			postData.put("screenName", currentUser.screenName);
			postData.put("password", currentUser.password);

			JSONArray strings = Statics.createVolunteeringJSON();

			JSONArray sel = new JSONArray();
			for (int i = 0; i < selected.size(); i++) {
				sel.put(i, selected.get(i));
			}

			postData.put("options", strings);
			postData.put("selected", sel);

			String response = makeRequest(BASE_URL,
					"users/prefs/volunteering/save", "", "POST", postData);
			JSONObject res = new JSONObject(response);
			if (res.getString("response").equalsIgnoreCase(
					"Operation succeeded")) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return false;
	}

	public ArrayList<Boolean> retreiveVolunteeringPreferences() {
		String res;
		try {
			res = makeRequest(
					BASE_URL,
					"users/prefs/volunteering/retrieve",
					"screen_name="
							+ URLEncoder
									.encode(currentUser.screenName, "UTF-8")
							+ "&password="
							+ URLEncoder.encode(currentUser.password, "UTF-8"),
					"GET", null);
			JSONObject result = new JSONObject(res);

			ArrayList<Boolean> listdata = new ArrayList<Boolean>();
			JSONArray jArray = (JSONArray) result.getJSONArray("selected");
			if (jArray != null) {
				for (int i = 0; i < jArray.length(); i++) {
					listdata.add((Boolean) jArray.get(i));
				}
			}

			return listdata;

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public boolean saveHobbyPreferences(ArrayList<Boolean> selected) {
		JSONObject postData = new JSONObject();

		try {
			postData.put("screenName", currentUser.screenName);
			postData.put("password", currentUser.password);

			JSONArray strings = Statics.createInterestsJSON();

			JSONArray sel = new JSONArray();
			for (int i = 0; i < selected.size(); i++) {
				sel.put(i, selected.get(i));
			}

			postData.put("options", strings);
			postData.put("selected", sel);

			String response = makeRequest(BASE_URL, "users/prefs/hobby/save",
					"", "POST", postData);
			JSONObject res = new JSONObject(response);
			if (res.getString("response").equalsIgnoreCase(
					"Operation succeeded")) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return false;
	}

	public ArrayList<Boolean> retreiveHobbyPreferences() {
		String res;
		try {
			res = makeRequest(
					BASE_URL,
					"users/prefs/hobby/retrieve",
					"screen_name="
							+ URLEncoder
									.encode(currentUser.screenName, "UTF-8")
							+ "&password="
							+ URLEncoder.encode(currentUser.password, "UTF-8"),
					"GET", null);
			JSONObject result = new JSONObject(res);

			ArrayList<Boolean> listdata = new ArrayList<Boolean>();
			JSONArray jArray = (JSONArray) result.getJSONArray("selected");
			if (jArray != null) {
				for (int i = 0; i < jArray.length(); i++) {
					listdata.add((Boolean) jArray.get(i));
				}
			}

			return listdata;

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public boolean saveSocialPreferences(ArrayList<Boolean> selected) {
		JSONObject postData = new JSONObject();

		try {
			postData.put("screenName", currentUser.screenName);
			postData.put("password", currentUser.password);

			JSONArray strings = Statics.createInterestsJSON();

			JSONArray sel = new JSONArray();
			for (int i = 0; i < selected.size(); i++) {
				sel.put(i, selected.get(i));
			}

			postData.put("options", strings);
			postData.put("selected", sel);

			String response = makeRequest(BASE_URL, "users/prefs/social/save",
					"", "POST", postData);
			JSONObject res = new JSONObject(response);
			if (res.getString("response").equalsIgnoreCase(
					"Operation succeeded")) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return false;
	}

	public ArrayList<Boolean> retreiveSocialPreferences() {
		String res;
		try {
			res = makeRequest(
					BASE_URL,
					"users/prefs/social/retrieve",
					"screen_name="
							+ URLEncoder
									.encode(currentUser.screenName, "UTF-8")
							+ "&password="
							+ URLEncoder.encode(currentUser.password, "UTF-8"),
					"GET", null);
			JSONObject result = new JSONObject(res);

			ArrayList<Boolean> listdata = new ArrayList<Boolean>();
			JSONArray jArray = (JSONArray) result.getJSONArray("selected");
			if (jArray != null) {
				for (int i = 0; i < jArray.length(); i++) {
					listdata.add((Boolean) jArray.get(i));
				}
			}

			return listdata;

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	@SuppressWarnings("deprecation")
	public boolean uploadProfilePicture(File mediaToUpload) throws Exception{
		
		try {
			URL url = new URL(BASE_URL + "/users/uploadProfilePic/");
			System.out.println("Connect to: " + url);

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");

			MultipartEntity entity = new MultipartEntity();
			entity.addPart(
					"upload",
					new FileBody(mediaToUpload, URLConnection
							.guessContentTypeFromName(mediaToUpload.getName())));

			connection.setRequestProperty("Content-Type", entity
					.getContentType().getValue());
			connection.setRequestProperty("Accept", "application/json");
			OutputStream out = connection.getOutputStream();
			try {
				entity.writeTo(out);
			} finally {
				out.close();
			}
			InputStream in = null;
			try {
				int response = connection.getResponseCode();
				if (response >= 200 && response < 300) {
					in = new BufferedInputStream(connection.getInputStream());
				} else {
					in = new BufferedInputStream(connection.getErrorStream());
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return false;
			}
			try {
				ByteArrayOutputStream bo = new ByteArrayOutputStream();
				int i = in.read();
				while (i != -1) {
					bo.write(i);
					i = in.read();
				}
				String output = bo.toString();
				System.out.println("Returning from uploadProfilePic: "
						+ output);
				try {
					JSONObject res = new JSONObject(output);
					if (res.getString("response").equalsIgnoreCase(
							"Operation succeeded")) {
						return true;
					}
				} catch (JSONException e) {
					System.err
							.println("uploadProfilePic: exception formatting JSON:");
					e.printStackTrace();
					return false;
				} catch (Exception e) {
					System.err
							.println("uploadProfilePic: generic exception:");
					e.printStackTrace();
					return false;
				}
			} catch (IOException e) {
				return false;
			} catch (NumberFormatException e) {
				return false;
			} finally {
				in.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
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
