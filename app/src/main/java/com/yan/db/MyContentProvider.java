package com.yan.db;

/**
 * Created by yan on 2016/1/14.
 */

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;


/**
 * Created by yan on 2015/12/30.
 */
public class MyContentProvider extends ContentProvider {
    private DBHelper mDbHelper;
    public static final int URI_ASU = 1;
    public static final int URI_AVG = 2;
    private static final String QUERY_ASU_PATH = "asuData";
    private static final String QUERY_ASU_AVG_PATH = "asuAvg";
    private static final UriMatcher mURI_MATCHER;

    static {
        mURI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        mURI_MATCHER.addURI("com.yan.db.MyContentProvider", QUERY_ASU_PATH, URI_ASU);
        mURI_MATCHER.addURI("com.yan.db.MyContentProvider", QUERY_ASU_AVG_PATH, URI_AVG);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = DBHelper.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor c = null;
        switch (mURI_MATCHER.match(uri)) {
            case URI_ASU:
                c = db.rawQuery("SELECT * FROM " + DbConfig.TABLE_NAME + " WHERE date " + sortOrder, null);
                break;
            case URI_AVG:
                c = db.rawQuery(sortOrder, null);
            default:
                break;
        }
        if (c != null) {
            c.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return c;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long id = db.insert(DbConfig.TABLE_NAME, null, values);
        if (id > 0) {
            getContext().getContentResolver().notifyChange(
                    ContentUris.withAppendedId(DbConfig.CONTENT_NOTE_DATA_URI,
                            id), null);
        }
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
