package edu.bu.met.vbalabhadrapatruni.mypeeps.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.bu.met.vbalabhadrapatruni.mypeeps.object.Post;

/**
 * Created by Virinchi on 4/3/2017.
 */

public class DBManager {
    private MyPeepsDBHelper dbHelper;
    private static DBManager manager = null;

    private DBManager(Context context) {
        dbHelper = new MyPeepsDBHelper(context);
    }

    public static DBManager currentManager(Context context) {
        if (manager == null) {
            manager = new DBManager(context);
        }

        return manager;
    }

    public long insertPost(Post newPost) {
        ContentValues values = new ContentValues();
        values.put("POST_ID", newPost.getPOST_ID());
        values.put("USER_ID", newPost.getUSER_ID());
        values.put("BD_TEXT", newPost.getBD_TEXT());
        values.put("IMG_URL", newPost.getIMG_URL());
        return dbHelper.getWritableDatabase().insert("POST", null, values);
    }

    public List<Post> getPosts() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {"POST_ID", "USER_ID", "BD_TEXT", "IMG_URL"};
        Cursor c = db.query("POST", projection, null, null, null, null, null);
        c.moveToFirst();
        List<Post> posts = new ArrayList<>();
        for (int i = 0 ; i < c.getCount() ; i++) {
            int id = c.getInt(c.getColumnIndexOrThrow("POST_ID"));
            int uid = c.getInt(c.getColumnIndexOrThrow("USER_ID"));
            String body = c.getString(c.getColumnIndexOrThrow("BD_TEXT"));
            String imgUrl = c.getString(c.getColumnIndexOrThrow("IMG_URL"));
            Post p = new Post(id, uid, body, imgUrl);
            posts.add(p);
            c.moveToNext();
        }
        return posts;
    }


}
