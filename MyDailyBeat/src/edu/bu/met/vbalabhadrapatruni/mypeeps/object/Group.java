package edu.bu.met.vbalabhadrapatruni.mypeeps.object;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Virinchi on 4/3/2017.
 */

public class Group implements Serializable {

    private int GROUP_ID;
    private int ADMIN_ID;
    private String name;

    public Group() {
    }

    public Group(JSONObject raw) throws Exception {
        GROUP_ID = raw.getInt("group_id");
        ADMIN_ID = raw.getInt("admin_id");
        name = raw.getString("group_nm");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGROUP_ID() {
        return GROUP_ID;
    }

    public void setGROUP_ID(int GROUP_ID) {
        this.GROUP_ID = GROUP_ID;
    }

    public int getADMIN_ID() {
        return ADMIN_ID;
    }

    public void setADMIN_ID(int ADMIN_ID) {
        this.ADMIN_ID = ADMIN_ID;
    }
}
