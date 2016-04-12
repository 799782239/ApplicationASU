package com.example.yan.myapplication.utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by yanqijs on 2016/4/8.
 */
public class RequestQ {

    private static RequestQueue mRequestQueen;

    public static RequestQueue getRequestQueen(Context context) {
        if (mRequestQueen != null) {
            return mRequestQueen;
        } else {
            mRequestQueen = Volley.newRequestQueue(context);
            return mRequestQueen;
        }

    }
}
