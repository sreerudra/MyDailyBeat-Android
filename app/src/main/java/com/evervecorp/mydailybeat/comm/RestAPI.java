package com.evervecorp.mydailybeat.comm;

import android.content.Context;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.auth.AUTH;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.*;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.evervecorp.mydailybeat.MimeTypeConstants;
import com.evervecorp.mydailybeat.enumeration.InviteSendingMethod;
import com.evervecorp.mydailybeat.enumeration.UserVerified;
import com.evervecorp.mydailybeat.object.FlingProfile;
import com.evervecorp.mydailybeat.object.Group;
import com.evervecorp.mydailybeat.object.HobbiesPreferences;
import com.evervecorp.mydailybeat.object.MatchingPreferences;
import com.evervecorp.mydailybeat.object.Post;
import com.evervecorp.mydailybeat.object.User;
import com.evervecorp.mydailybeat.object.UserPreferences;

import org.apache.http.*;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.evervecorp.mydailybeat.Utils.JSON;

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
    private final OkHttpClient client = new OkHttpClient();
    private static RestAPI instance = null;
    private String auth_token = null;
    private User currentUser = null;
    private RestAPI() {

    }

    public static RestAPI getInstance() {
        if (instance == null) {
            instance = new RestAPI();
        }

        return instance;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public boolean hasConnectivity(Context c) {
        ConnectivityManager connectivityManager = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public boolean validateToken() throws Exception {
        if (auth_token != null) {
            JSONObject json = new JSONObject();
            json.put("token", auth_token);
            JSONObject result = this.makeRequest(PUBLIC_BASE_URL, "token/validate", "", POST_REQUEST.rawValue, json);
            return result.getBoolean("success");
        } else {
            return true;
        }
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
        Log.d("MyDailyBeat", result.toString());
        if (result != null) {
            this.currentUser = new User(result);
            auth_token = result.getString("auth_token");
            return true;
        }
        return false;
    }

    public boolean doesUserExistWithName(String name) throws Exception {
        JSONObject result = this.makeRequest(PUBLIC_BASE_URL, "users/exists", "name=" + name, GET_REQUEST.rawValue, null);
        return result.getBoolean("success");
    }

    public boolean doesUserExistWithEmail(String email) throws Exception {
        JSONObject result = this.makeRequest(PUBLIC_BASE_URL, "users/exists", "email=" + email, GET_REQUEST.rawValue, null);
        return result.getBoolean("success");
    }

    public boolean doesUserExistWithScreenName(String screenName) throws Exception {
        JSONObject result = this.makeRequest(PUBLIC_BASE_URL, "users/exists", "screenName=" + screenName, GET_REQUEST.rawValue, null);
        return result.getBoolean("success");
    }

    public boolean doesUserExistWithMobile(String mobile) throws Exception {
        JSONObject result = this.makeRequest(PUBLIC_BASE_URL, "users/exists", "mobile=" + mobile, GET_REQUEST.rawValue, null);
        return result.getBoolean("success");
    }

    public UserVerified isUserVerified(String screenName, String password) throws Exception {
        JSONObject json = new JSONObject();
        json.put("screenName", screenName);
        json.put("password", password);
        JSONObject result = this.makeRequest(PUBLIC_BASE_URL, "user/verified/check", "", POST_REQUEST.rawValue, json);
        boolean success = result.getBoolean("success");
        boolean verified = result.getBoolean("verified");
        boolean error = result.getBoolean("error");
        if (!success && !verified && error) {
            return UserVerified.ERROR;
        } else if (!success && !verified) {
            return UserVerified.USER_DOESNT_EXIST;
        } else if (!verified) {
            return UserVerified.USER_NOT_VERIFIED;
        } else {
            return UserVerified.USER_VERIFIED;
        }
    }

    public void resendEmail(String screenName, String password) throws Exception {
        JSONObject json = new JSONObject();
        json.put("screenName", screenName);
        json.put("password", password);
        JSONObject result = this.makeRequest(PUBLIC_BASE_URL, "user/verify/resend", "", POST_REQUEST.rawValue, json);
    }

    public List<Group> getGroupsForCurrentUser() throws Exception {
        JSONObject result = this.makeRequest(AUTH_BASE_URL, "groups/get", "", auth_token, GET_REQUEST.rawValue, null);
        ArrayList<Group> groups = new ArrayList<>();
        Log.d("MyDailyBeat", "Groups Object: " + result);
        JSONArray rawList = result.getJSONArray("groups");
        for (int i = 0 ; i < rawList.length() ; i++) {
            JSONObject rawGrp = rawList.getJSONObject(i);
            groups.add(new Group(rawGrp));
        }
        Log.d("MyDailyBeat", "Groups Count: " + groups.size());
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

    public boolean deletePost(Post p) throws Exception {
        String path = "groups/posts/" + p.getPOST_ID() + "/delete";
        JSONObject result = this.makeRequest(AUTH_BASE_URL, path, "", auth_token, GET_REQUEST.rawValue, null);
        return result.getBoolean("success");
    }

    public boolean deleteGroup(Group g) throws Exception {
        String path = "groups/" + g.getGROUP_ID() + "/delete";
        JSONObject result = this.makeRequest(AUTH_BASE_URL, path, "", auth_token, GET_REQUEST.rawValue, null);
        return result.getBoolean("success");
    }

    public boolean createPost(String text, int gId) throws Exception {
        return createPost(text, gId, null);
    }

    public boolean createPost(String text, int gId, File mediaToUpload) throws Exception {
        JSONObject postData = new JSONObject();
        postData.put("text", text);
        if (mediaToUpload == null) {
            JSONObject result = this.makeRequest(AUTH_BASE_URL, "groups/" + gId + "/posts/new", "", auth_token, POST_REQUEST.rawValue, postData);
            return result.getBoolean("success");
        } else {
            String filename = mediaToUpload.getName();
            String key = filename.substring(filename.indexOf(".") + 1);
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", filename,
                            RequestBody.create(MediaType.parse(key), mediaToUpload))
                    .build();

            RequestBody postBody = RequestBody.create(JSON, postData.toString());

            Request request = new Request.Builder()
                    .header("x-access-token", auth_token)
                    .url("groups/" + gId + "/posts/new")
                    .post(postBody)
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new Exception("Unexpected code " + response);
            }

            JSONObject object = new JSONObject(response.body().string());
            return object.getBoolean("success");
        }
    }

    public void logout() {
        JSONObject result = this.makeRequest(AUTH_BASE_URL, "logout", "", auth_token, POST_REQUEST.rawValue, null);
        auth_token = null;
        currentUser = null;
    }

    public void refreshUserData() throws Exception {
        JSONObject result = this.makeRequest(AUTH_BASE_URL, "users/get", "", auth_token, POST_REQUEST.rawValue, null);
        if (result.getString("screenName") != null) {
            currentUser = new User(result);
        }
    }

    public JSONObject getOpportunitiesInLocation(String zipcode, int page) throws Exception {
        String path = "volunteering/search/" + zipcode + "/" + page;
        JSONObject result = this.makeRequest(AUTH_BASE_URL, path, "", auth_token, GET_REQUEST.rawValue, null);
        String json = result.getString("jsonString");
        json = URLDecoder.decode(json);
        json.replace("\\", "");

        int index = json.indexOf("{");
        if (index != -1) {
            json = json.substring(index);
        } else {
            Log.e("MyDailyBeat", "Malformed result");
        }

        return new JSONObject(json);

    }

    public boolean sendReferral(User from, String toName, String toEmail) throws Exception {
        JSONObject input = new JSONObject();
        input.put("name", toName);
        input.put("email", toEmail);
        JSONObject result = this.makeRequest(AUTH_BASE_URL, "refer/send", "", auth_token, POST_REQUEST.rawValue, input);
        return result.getBoolean("success");
    }

    public boolean uploadProfilePicture(File mediaToUpload) throws Exception {
        boolean returnVal = false;
        String uploadURL = AUTH_BASE_URL + "/profile/upload";

        String filename = mediaToUpload.getName();
        String key = filename.substring(filename.indexOf(".") + 1);

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", filename,
                        RequestBody.create(MediaType.parse(key), mediaToUpload))
                .build();

        Request request = new Request.Builder()
                .header("x-access-token", auth_token)
                .url(uploadURL)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new Exception("Unexpected code " + response);
        }

        JSONObject object = new JSONObject(response.body().string());
        returnVal = object.getBoolean("success");

        return returnVal;
    }

    public String getProfilePictureForCurrentUser() throws Exception {
        return this.getProfilePictureForUser(this.currentUser.getScreenName());
    }

    public String getProfilePictureForUser(String screenName) throws Exception {
        String path = "profile/" + screenName + "/get";
        JSONObject result = this.makeRequest(PUBLIC_BASE_URL, path, "", GET_REQUEST.rawValue, null);
        return result.getString("profilePicUrl");
    }

    public UserPreferences getUserPreferences() throws Exception {
        JSONObject result = this.makeRequest(AUTH_BASE_URL, "preferences/user/get", "", auth_token, GET_REQUEST.rawValue, null);
        return new UserPreferences(result);
    }

    public MatchingPreferences getMatchingPreferences() throws Exception {
        JSONObject result = this.makeRequest(AUTH_BASE_URL, "preferences/matching/get", "", auth_token, GET_REQUEST.rawValue, null);
        return new MatchingPreferences(result);
    }

    public boolean saveUserPreferences(UserPreferences prefs) throws Exception {
        JSONObject postData = new JSONObject();
        postData.put("prefs", prefs.toJSON());
        JSONObject result = this.makeRequest(AUTH_BASE_URL, "preferences/user/save", "", auth_token, POST_REQUEST.rawValue, postData);
        return result.getBoolean("success");
    }

    public boolean saveMatchingPreferences(MatchingPreferences prefs) throws Exception {
        JSONObject postData = new JSONObject();
        postData.put("prefs", prefs.toJSON());
        JSONObject result = this.makeRequest(AUTH_BASE_URL, "preferences/matching/save", "", auth_token, POST_REQUEST.rawValue, postData);
        return result.getBoolean("success");
    }

    public boolean userPreferencesExistForUser() throws Exception {
        JSONObject result = this.makeRequest(AUTH_BASE_URL, "preferences/user/exists", "", auth_token, GET_REQUEST.rawValue, null);
        return result.getBoolean("success");
    }

    public boolean matchingPreferencesExistForUser() throws Exception {
        JSONObject result = this.makeRequest(AUTH_BASE_URL, "preferences/matching/exists", "", auth_token, GET_REQUEST.rawValue, null);
        return result.getBoolean("success");
    }
    public JSONArray getHobbiesRefList() throws Exception {
        JSONObject object = this.makeRequest(PUBLIC_BASE_URL, "hobbies", "", GET_REQUEST.rawValue, null);
        return object.getJSONArray("hobbies");
    }

    public HobbiesPreferences getHobbiesPreferencesForUser() throws Exception {
        JSONObject result = this.makeRequest(AUTH_BASE_URL, "preferences/hobbies/get", "", auth_token, GET_REQUEST.rawValue, null);
        return new HobbiesPreferences(result.getJSONArray("items"));
    }

    public boolean hobbiesPreferencesExistForUser() throws Exception{
        JSONObject result = this.makeRequest(AUTH_BASE_URL, "preferences/hobbies/exists", "", auth_token, GET_REQUEST.rawValue, null);
        return result.getBoolean("success");
    }

    public boolean saveHobbiesPreferences(HobbiesPreferences prefs) throws Exception {
        JSONArray arr = prefs.toJSON();
        JSONObject postData = new JSONObject();
        postData.put("hobbies", arr);
        JSONObject result = this.makeRequest(AUTH_BASE_URL, "preferences/hobbies/save", "", auth_token, POST_REQUEST.rawValue, postData);
        return result.getBoolean("success");
    }

    public List<FlingProfile> getHobbiesMatchesForUser() throws Exception {
        String resultString = this.makeRequestWithStringResult(AUTH_BASE_URL, "friends/match", "", auth_token, GET_REQUEST.rawValue, null);
        JSONArray result = new JSONArray(resultString);
        List<FlingProfile> retVal = new ArrayList<>();
        for (int i = 0 ; i < result.length() ; i++) {
            FlingProfile profile = new FlingProfile();
            JSONObject item = result.getJSONObject(i);
            profile.id = item.getInt("user_id");
            profile.aboutMe = item.getString("abt_me");
            retVal.add(profile);
        }
        return retVal;
    }

    public boolean inviteUserToJoinGroup(User user, Group group, InviteSendingMethod method, String inviteMessage) throws Exception {
        String path = "groups/" + group.getGROUP_ID() + "/invite";
        switch (method) {
            case SEND_BY_EMAIL:
                path += "/email";
                break;
            case SEND_BY_MOBILE:
                path += "/mobile";
                break;
        }

        JSONObject postData = new JSONObject();
        postData.put("other", user.getScreenName());
        postData.put("message", inviteMessage);
        JSONObject result = this.makeRequest(AUTH_BASE_URL, path, "", auth_token, POST_REQUEST.rawValue, postData);
        return result.getBoolean("success");
    }

    public User getUserData(int id) {
        
    }

    protected String makeRequestWithStringResult(String baseUrl, String path, String parameters, String auth_token, String reqType, JSONObject postData) {
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
                return jsonString;
            } catch (IOException ioe) {
                return null;
            } finally {
                in.close();
            }
        } catch (ConnectException ce) {
            Log.e("MyDailyBeat", ce.getLocalizedMessage());
        } catch (Exception e) {
            Log.e("MyDailyBeat", e.getLocalizedMessage());
        }
        return null;
    }


    protected JSONObject makeRequest(String baseUrl, String path, String parameters, String auth_token, String reqType, JSONObject postData) {
        String result = this.makeRequestWithStringResult(baseUrl, path, parameters, auth_token, reqType, postData);
        try {
            return new JSONObject(result);
        } catch (Exception e){
            return null;
        }
    }

    protected JSONObject makeRequest(String baseUrl, String path, String parameters, String reqType, JSONObject postData) {
        return this.makeRequest(baseUrl, path, parameters, null, reqType, postData);
    }
}
