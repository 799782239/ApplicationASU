package com.example.yan.myapplication.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.example.yan.myapplication.model.SaveData;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yanqijs on 2016/4/12.
 */
public class TimerUtils extends TimerTask {
    private Handler mHandler;

    public TimerUtils(Handler mHandler) {
        if (this.mHandler == null) {
            this.mHandler = mHandler;
        }
    }

    @Override
    public void run() {
        if (SaveData.data == null) {
        } else {
            if (SaveData.data.getAsu() != 0) {
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putInt("ASU", SaveData.data.getAsu());
                data.putDouble("Latitude", SaveData.data.getLaLatitude());
                data.putDouble("Longitude", SaveData.data.getLongitude());
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                data.putString("time", sdf.format(System.currentTimeMillis()));
                msg.setData(data);
                msg.what = 1;
                mHandler.sendMessage(msg);
            }
        }

    }
}
