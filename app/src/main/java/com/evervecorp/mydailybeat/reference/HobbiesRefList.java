package com.evervecorp.mydailybeat.reference;

import com.evervecorp.mydailybeat.comm.RestAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by Virinchi on 8/27/2017.
 */

public class HobbiesRefList {
    public Map<Integer, String> list = new HashMap<>();
    private boolean loaded = false;
    public static HobbiesRefList s_list = new HobbiesRefList();

    private HobbiesRefList() {
        super();
    }

    public static HobbiesRefList getInstance() {
        if (!s_list.loaded) {
            s_list.load();
        }

        return s_list;
    }

    public void load() {
        try {
            JSONArray arr = RestAPI.getInstance().getHobbiesRefList();
            int length = arr.length();
            for (int i = 0 ; i < length ; i++) {
                JSONObject value = arr.getJSONObject(i);
                int id = value.getInt("hby_ref_id");
                String desc = value.getString("hby_dsc");
                this.list.put(id, desc);
            }
            this.loaded = true;
        } catch (Exception e) {
            this.loaded = false;
        }
    }
}
