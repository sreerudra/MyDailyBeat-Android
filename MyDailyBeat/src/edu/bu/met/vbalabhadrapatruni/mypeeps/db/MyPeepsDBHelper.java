package edu.bu.met.vbalabhadrapatruni.mypeeps.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Virinchi on 4/3/2017.
 */

class MyPeepsDBHelper extends SQLiteOpenHelper {
    protected static final String DATABASE_NAME = "MyPeepsDB.db";
    protected static final int DB_VERSION = 1;
    protected static final String GROUP_TABLE_SPEC = "CREATE TABLE GROUP (GROUP_ID INTEGER, ADMIN_ID INTEGER)";
    protected static final String POST_TABLE_SPEC = "CREATE TABLE POST (POST_ID INTEGER, USER_ID INTEGER, BD_TEXT VARCHAR(255), IMG_URL VARCHAR(50))";

    protected MyPeepsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(GROUP_TABLE_SPEC);
        db.execSQL(POST_TABLE_SPEC);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
