package com.example.yan.myapplication.utils;

import android.content.Context;

import com.android.volley.Request;

/**
 * Created by yanqijs on 2016/4/8.
 */
public class VolleyUtils {
    public static void addRequest(Request<?> request, Context context) {
        RequestQ.getRequestQueen(context).add(request);
    }
}
