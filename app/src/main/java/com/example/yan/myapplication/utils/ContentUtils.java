package com.example.yan.myapplication.utils;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

/**
 * Created by yanqijs on 2016/4/11.
 */
public class ContentUtils extends AsyncQueryHandler {
    private Context context;
    private ContentResolver mContentResolver;

    public ContentUtils(ContentResolver cr) {
        super(cr);
        mContentResolver=cr;
    }

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        super.onInsertComplete(token, cookie, uri);
    }
}
