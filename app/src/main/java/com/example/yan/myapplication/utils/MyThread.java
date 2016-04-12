package com.example.yan.myapplication.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.yan.myapplication.model.SaveData;

import java.text.SimpleDateFormat;

/**
 * Created by yan on 2015/12/10.
 */

public class MyThread extends Thread {
    private Handler myHanler;

    public MyThread(Handler myHanler) {
        this.myHanler = myHanler;
    }

    public static Boolean threadIsTrue = true;

    @Override
    public void run() {
        try {
            while (threadIsTrue) {
                Message msg = new Message();
                Bundle data = new Bundle();
                if (SaveData.data.getAsu() == 0) {
                } else {
                    data.putInt("ASU", SaveData.data.getAsu());
                    data.putDouble("Latitude", SaveData.data.getLaLatitude());
                    data.putDouble("Longitude", SaveData.data.getLongitude());
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    data.putString("time", sdf.format(System.currentTimeMillis()));
                    msg.setData(data);
                    msg.what = 1;
                    myHanler.sendMessage(msg);
                }
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
