package com.evervecorp.mydailybeat.object;

import com.evervecorp.mydailybeat.reference.HobbiesRefList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Virinchi on 8/27/2017.
 */

public class HobbiesPreferences {
    Map<Integer, Boolean> hobbies = new HashMap<>();

    public HobbiesPreferences(JSONArray json) throws Exception {
        Set<Integer> list = HobbiesRefList.getInstance().list.keySet();

        if (json.length() <= 0) {
            for (Integer hobby: list) {
                this.hobbies.put(hobby, false);
            }
        } else {
            ArrayList<Integer> list2 = new ArrayList<>();
            int length = json.length();
            for (int i = 0 ; i < length ; i++) {
                JSONObject value = json.getJSONObject(i);
                int id = value.getInt("hby_ref_id");
                list2.add(id);
            }
            for (Integer hobby: list) {
                this.hobbies.put(hobby, list2.contains(hobby));
            }
        }
    }

    public JSONArray toJSON() {
        Map<Integer, Boolean> filtered = new HashMap<>();
        JSONArray arr = new JSONArray();
        for (Map.Entry<Integer, Boolean> e: hobbies.entrySet()) {
            if (e.getValue()) {
                filtered.put(e.getKey(), e.getValue());
            }
        }
        for (Map.Entry<Integer, Boolean> e: filtered.entrySet()) {
            arr.put(e.getKey());
        }

        return arr;
    }
}
