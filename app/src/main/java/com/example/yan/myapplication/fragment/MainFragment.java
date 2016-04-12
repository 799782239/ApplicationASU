package com.example.yan.myapplication.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yan.myapplication.Config;
import com.example.yan.myapplication.utils.MyThread;
import com.example.yan.myapplication.R;
import com.example.yan.myapplication.activity.SettingActivity;
import com.example.yan.myapplication.utils.TimerUtils;

import java.util.Timer;
import java.util.TimerTask;

import tools.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BaseFragment implements View.OnClickListener {

    private TextView asuText, longitudeText, latitudeText, timeText;
    private MyThread myThread;
    private boolean isPrepare;
    private LinearLayout upLinearLayout;
    private Button upButton;
    private SharedPreferences sharedPreferences;
    public static Timer mTimer;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, null);
        upLinearLayout = (LinearLayout) view.findViewById(R.id.up_lin);
        upButton = (Button) view.findViewById(R.id.up_btn);
        upButton.setOnClickListener(this);
        asuText = (TextView) view.findViewById(R.id.asuText);
        asuText.setText("暂无数据");
        longitudeText = (TextView) view.findViewById(R.id.longitudeText);
        longitudeText.setText("暂无数据");
        latitudeText = (TextView) view.findViewById(R.id.latitudeText);
        latitudeText.setText("暂无数据");
        timeText = (TextView) view.findViewById(R.id.timeText);
        timeText.setText("暂无数据");
//        if (Config.isFresh) {
        Handler myHanler = new MyHanler();
//            myThread = new MyThread(myHanler);
//            myThread.start();
        TimerTask timerTask = new TimerUtils(myHanler);
        mTimer = new Timer(true);
        mTimer.schedule(timerTask, 0, 5000);
//            Config.isFresh = false;
//        }
        isPrepare = true;
        lazy();
        return view;

    }

    @Override
    public void lazy() {
        if (!isPrepare || !isShow) {

            System.out.println("exit");
            return;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.up_btn:
                Intent i = new Intent(getActivity(), SettingActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }

    public class MyHanler extends Handler {
        MyHanler() {
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        sharedPreferences = getActivity().getSharedPreferences("com.example.yan.myapplication_preferences", Context.MODE_PRIVATE);
                        if (sharedPreferences.getBoolean("switch_connect", true)) {
                            Bundle data = new Bundle();
                            data = msg.getData();
                            asuText.setText(data.getInt("ASU") + "");
                            latitudeText.setText(data.getDouble("Latitude") + "");
                            longitudeText.setText(data.getDouble("Longitude") + "");
                            timeText.setText(data.getString("time") + "");
                            upLinearLayout.setVisibility(View.GONE);
                        } else {
                            asuText.setText("暂无数据" + "");
                            latitudeText.setText("暂无数据" + "");
                            longitudeText.setText("暂无数据" + "");
                            timeText.setText("暂无数据" + "");
                            upLinearLayout.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer=null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (Config.isFresh) {
        Handler myHanler = new MyHanler();
//            myThread = new MyThread(myHanler);
//            myThread.start();
        TimerTask timerTask = new TimerUtils(myHanler);
        if (mTimer == null) {
            mTimer = new Timer(true);
            mTimer.schedule(timerTask, 0, 5000);
        }
        Config.isFresh = false;
    }

}
