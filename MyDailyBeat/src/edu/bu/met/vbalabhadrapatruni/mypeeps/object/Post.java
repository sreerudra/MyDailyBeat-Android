package edu.bu.met.vbalabhadrapatruni.mypeeps.object;

import org.json.JSONObject;

/**
 * Created by Virinchi on 4/3/2017.
 */

public class Post {
    private int POST_ID;
    private int USER_ID;
    private String BD_TEXT;
    private String IMG_URL;

    public Post(int POST_ID, int USER_ID, String BD_TEXT, String IMG_URL) {
        this.POST_ID = POST_ID;
        this.USER_ID = USER_ID;
        this.BD_TEXT = BD_TEXT;
        this.IMG_URL = IMG_URL;
    }

    public Post() {
    }

    public Post(JSONObject jsonObject) throws Exception {
        this.POST_ID = jsonObject.getInt("post_id");
        this.USER_ID = jsonObject.getInt("user_id");
        this.BD_TEXT = jsonObject.getString("post_bd");
        this.IMG_URL = jsonObject.getString("pic_url");
    }

    public int getPOST_ID() {
        return POST_ID;
    }

    public void setPOST_ID(int POST_ID) {
        this.POST_ID = POST_ID;
    }

    public int getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(int USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getBD_TEXT() {
        return BD_TEXT;
    }

    public void setBD_TEXT(String BD_TEXT) {
        this.BD_TEXT = BD_TEXT;
    }

    public String getIMG_URL() {
        return IMG_URL;
    }

    public void setIMG_URL(String IMG_URL) {
        this.IMG_URL = IMG_URL;
    }
}
