package com.evervecorp.mydailybeat.object;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Virinchi on 8/26/2017.
 */

public class MatchingPreferences {
    ArrayList<Integer> gender, age, status, ethnicity, beliefs, drinker;
    int smoker, veteran;

    public MatchingPreferences(JSONObject json) throws Exception {
        JSONArray arr = json.getJSONArray("gender");
        Type listType = new TypeToken<ArrayList<Integer>>(){}.getType();
        Gson gson = new Gson();
        if (arr.length() != 0) {
            gender = gson.fromJson(arr.toString(), listType);
        } else {
            gender = new ArrayList<>();
        }

        arr = json.getJSONArray("age");
        if (arr.length() != 0) {
            age = gson.fromJson(arr.toString(), listType);
        } else {
            age = new ArrayList<>();
        }

        arr = json.getJSONArray("mrrtl");
        if (arr.length() != 0) {
            status = gson.fromJson(arr.toString(), listType);
        } else {
            status = new ArrayList<>();
        }

        arr = json.getJSONArray("ethnct");
        if (arr.length() != 0) {
            ethnicity = gson.fromJson(arr.toString(), listType);
        } else {
            ethnicity = new ArrayList<>();
        }

        arr = json.getJSONArray("relgs");
        if (arr.length() != 0) {
            beliefs = gson.fromJson(arr.toString(), listType);
        } else {
            beliefs = new ArrayList<>();
        }

        arr = json.getJSONArray("drnkr");
        if (arr.length() != 0) {
            drinker = gson.fromJson(arr.toString(), listType);
        } else {
            drinker = new ArrayList<>();
        }

        smoker = json.getInt("smkr_threechoice_id");
        veteran = json.getInt("vtrn_threechoice_id");
    }

    public JSONObject toJSON() throws Exception {
        JSONObject result = new JSONObject();
        Gson gson = new Gson();
        Type listType = new TypeToken<JSONArray>(){}.getType();
        String gender = gson.toJson(this.gender, listType);
        JSONArray arr = new JSONArray(gender);
        result.put("gender", arr);
        String age = gson.toJson(this.age, listType);
        arr = new JSONArray(age);
        result.put("age", arr);
        String mrrtl = gson.toJson(this.status, listType);
        arr = new JSONArray(mrrtl);
        result.put("mrrtl", arr);
        String relgs = gson.toJson(this.beliefs, listType);
        arr = new JSONArray(relgs);
        result.put("relgs", arr);
        String ethnct = gson.toJson(this.ethnicity, listType);
        arr = new JSONArray(ethnct);
        result.put("ethnct", arr);
        String drnkr = gson.toJson(this.drinker, listType);
        arr = new JSONArray(drnkr);
        result.put("drnkr", arr);
        result.put("smkr_threechoice_id", smoker);
        result.put("vtrn_threechoice_id", veteran);
        return result;
    }
}
