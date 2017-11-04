package com.evervecorp.mydailybeat.object;

import org.json.JSONObject;

/**
 * Created by Virinchi on 8/24/2017.
 */

public class UserPreferences {
    public int gender, status, ethnicity, beliefs, drinker;
    public boolean isSmoker, isVeteran, isWillingToConnectAnonymously;

    public UserPreferences(JSONObject json) throws Exception {
        gender = json.getInt("gndr_ref_id");
        status = json.getInt("mrrtl_ref_id");
        ethnicity = json.getInt("ethnct_ref_id");
        beliefs = json.getInt("relgs_ref_id");
        drinker = json.getInt("drnkr_ref_id");
        isSmoker = json.getBoolean("smkr_yn");
        isVeteran = json.getBoolean("vtrn_yn");
        isWillingToConnectAnonymously = json.getBoolean("flng_bl_cnt_anm");
    }

    public JSONObject toJSON() throws Exception {
        JSONObject result = new JSONObject();
        result.put("gndr_ref_id", gender);
        result.put("mrrtl_ref_id", status);
        result.put("ethnct_ref_id", ethnicity);
        result.put("relgs_ref_id", beliefs);
        result.put("drnkr_ref_id", drinker);
        result.put("smkr_yn", isSmoker);
        result.put("vtrn_yn", isVeteran);
        result.put("flng_bl_cnt_anm", isWillingToConnectAnonymously);
        return result;
    }
}
