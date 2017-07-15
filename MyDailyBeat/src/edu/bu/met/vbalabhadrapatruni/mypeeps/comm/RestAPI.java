package edu.bu.met.vbalabhadrapatruni.mypeeps.comm;

import android.content.Context;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.bu.met.vbalabhadrapatruni.mypeeps.object.Group;
import edu.bu.met.vbalabhadrapatruni.mypeeps.object.Post;
import edu.bu.met.vbalabhadrapatruni.mypeeps.object.User;

/**
 * Created by Virinchi on 4/15/2017.
 */

public class RestAPI {
    private static final String PUBLIC_BASE_URL = "https://mydailybeat.herokuapp.com";
    private static final String AUTH_BASE_URL = PUBLIC_BASE_URL + "/api";
    private enum ReqType {
        GET ("GET"),
        POST ("POST");

        private final String rawValue;
        ReqType(String raw) {
            this.rawValue = raw;
        }

        @Override
        public String toString() {
            return rawValue;
        }
    }
    private static final ReqType GET_REQUEST = ReqType.GET;
    private static final ReqType POST_REQUEST = ReqType.POST;
    private static RestAPI instance = null;
    private String auth_token = null;
    private User currentUser = null;
    private Context mContext;
    private RestAPI(Context c) {
        this.mContext = c;
    }

    public static RestAPI getInstance(Context c) {
        if (instance == null) {
            instance = new RestAPI(c);
        }

        return instance;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public boolean hasConnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public boolean createUser(User toBeCreated) throws Exception {
        JSONObject json = new JSONObject();
        json.put("fname", toBeCreated.getFirstName());
        json.put("lname", toBeCreated.getLastName());
        json.put("email", toBeCreated.getEmailAddress());
        json.put("mobile", toBeCreated.getMobilePh());
        json.put("screenName", toBeCreated.getScreenName());
        json.put("password", toBeCreated.getPassword());
        json.put("zipcode", toBeCreated.getZipCode());
        Date dob = toBeCreated.getDob();
        Calendar current = Calendar.getInstance();
        current.setTime(dob);
        json.put("month", current.get(Calendar.MONTH));
        json.put("day", current.get(Calendar.DAY_OF_MONTH));
        json.put("year", current.get(Calendar.YEAR));
        JSONObject result = this.makeRequest(PUBLIC_BASE_URL, "users/register", "", POST_REQUEST.rawValue, json);
        return (result != null);
    }

    public boolean login(String screenName, String password) throws Exception {
        JSONObject json = new JSONObject();
        json.put("screenName", screenName);
        json.put("password", password);
        JSONObject result = this.makeRequest(PUBLIC_BASE_URL, "login", "", POST_REQUEST.rawValue, json);
        Log.d("MyPeeps", result.toString());
        if (result != null) {
            this.currentUser = new User(result);
            auth_token = result.getString("auth_token");
            return true;
        }
        return false;
    }

    public List<Group> getGroupsForCurrentUser() throws Exception {
        JSONObject result = this.makeRequest(AUTH_BASE_URL, "groups/get", "", auth_token, GET_REQUEST.rawValue, null);
        ArrayList<Group> groups = new ArrayList<>();
        Log.d("MyPeeps", "Groups Object: " + result);
        JSONArray rawList = result.getJSONArray("groups");
        for (int i = 0 ; i < rawList.length() ; i++) {
            JSONObject rawGrp = rawList.getJSONObject(i);
            groups.add(new Group(rawGrp));
        }
        Log.d("MyPeeps", "Groups Count: " + groups.size());
        return groups;
    }

    public List<Post> getPostsForGroup(int id) throws Exception {
        JSONObject result = this.makeRequest(AUTH_BASE_URL, "groups/" + id + "/posts/get", "", auth_token, GET_REQUEST.rawValue, null);
        ArrayList<Post> posts = new ArrayList<>();
        JSONArray rawList = result.getJSONArray("posts");
        for (int i = 0 ; i < rawList.length() ; i++) {
            JSONObject rawPost = rawList.getJSONObject(i);
            posts.add(new Post(rawPost));
        }

        return posts;
    }

    public boolean createGroup(String groupName) throws Exception {
        JSONObject postData = new JSONObject();
        postData.put("groupName", groupName);
        JSONObject result = this.makeRequest(AUTH_BASE_URL, "groups/create", "", auth_token, POST_REQUEST.rawValue, postData);
        return result.getBoolean("success");
    }

    public boolean createPost(String text, int gId) throws Exception {
        JSONObject postData = new JSONObject();
        postData.put("text", text);
        JSONObject result = this.makeRequest(AUTH_BASE_URL, "groups/" + gId + "/posts/new", "", auth_token, POST_REQUEST.rawValue, postData);
        return result.getBoolean("success");
    }

    public void logout() {
        auth_token = null;
        currentUser = null;
    }

    protected JSONObject makeRequest(String baseUrl, String path, String parameters, String auth_token, String reqType, JSONObject postData) {
        byte[] mPostData = null;

        int mstat = 0;
        try {
            URL url = new URL(baseUrl + "/" + path + "?" + parameters);
            System.out.println("Connect to: " + url);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(reqType);
            urlConnection.setRequestProperty("Accept", "application/json");
            if (auth_token != null) {
                urlConnection.setRequestProperty("x-access-token", auth_token);
            }
            if (postData != null) {
                System.out.println("Post Data: " + postData);
                mPostData = postData.toString().getBytes();
                urlConnection.setRequestProperty("Content-Length", Integer.toString(mPostData.length));
                urlConnection.setRequestProperty("Content-Type", "application/json");
                OutputStream out = urlConnection.getOutputStream();
                out.write(mPostData);
                out.close();
            }

            mstat = urlConnection.getResponseCode();
            InputStream in;
            if (mstat >= 200 && mstat < 300) {
                in = new BufferedInputStream((urlConnection.getInputStream()));
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
                String jsonString = bo.toString();
                return new JSONObject(jsonString);
            } catch (IOException ioe) {
                return null;
            } finally {
                in.close();
            }
        } catch (ConnectException ce) {
            Log.e("MyPeeps", ce.getLocalizedMessage());
        } catch (Exception e) {
            Log.e("MyPeeps", e.getLocalizedMessage());
        }
        return null;
    }

    protected JSONObject makeRequest(String baseUrl, String path, String parameters, String reqType, JSONObject postData) {
        return this.makeRequest(baseUrl, path, parameters, null, reqType, postData);
    }
}
