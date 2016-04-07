package com.yan.db;

import android.net.Uri;

/**
 * Created by yan on 2015/12/30.
 */
public class DbConfig {
    public static final String TYPE = "type";
    public static final String TABLE_NAME = "asuDb";
    public static final String ID = "_id";
    public static final String ASU = "asu";
    public static final String ALTITUDE = "altitude";
    public static final String LONGITUDE = "longitude";
    public static final String DATE = "date";
    public static final Uri CONTENT_NOTE_DATA_URI = Uri.parse("content://"
            + "com.yan.db.MyContentProvider" + "/asuData");
    public static final Uri CONTENT_ON_DESKTOP_URI = Uri.parse("content://"
            + "com.yan.db.MyContentProvider" + "/noteOnDeskTop");
    public static final Uri CONTENT_AVG_ASU_URI = Uri.parse("content://"
            + "com.yan.db.MyContentProvider" + "/asuAvg");
}
