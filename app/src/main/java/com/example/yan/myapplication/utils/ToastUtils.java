package com.example.yan.myapplication.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by yanqijs on 2016/4/11.
 */
public class ToastUtils {
    public static void MyToast(Context context, String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }
}
