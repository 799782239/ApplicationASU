package com.yan.db;

/**
 * Created by yan on 2016/1/14.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by yan on 2015/12/11.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper mDbHelper;
    public static final String CREATE_SQL = "CREATE TABLE IF NOT EXISTS " + DbConfig.TABLE_NAME
            + "(" + DbConfig.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DbConfig.ASU + " INTEGER,"
            + DbConfig.ALTITUDE + " DOUBLE,"
            + DbConfig.LONGITUDE + " DOUBLE,"
            + DbConfig.DATE + " TEXT NOT NULL DEFAULT '',"
            + DbConfig.TYPE + " TEXT NOT NULL DEFAULT ''"
            + ")";

    public DBHelper(Context context) {
        super(context, "asuDb.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static synchronized DBHelper getInstance(Context context) {
        if (mDbHelper == null) {
            mDbHelper = new DBHelper(context);
        }
        return mDbHelper;
    }
}