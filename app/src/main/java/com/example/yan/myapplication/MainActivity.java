package com.example.yan.myapplication;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class MainActivity extends AppCompatActivity {
    private TextView asuText, longitudeText, latitudeText, timeText;
    private MyThread myThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        asuText = (TextView) findViewById(R.id.asuText);
        asuText.setText("暂无数据");
        longitudeText = (TextView) findViewById(R.id.longitudeText);
        longitudeText.setText("暂无数据");
        latitudeText = (TextView) findViewById(R.id.latitudeText);
        latitudeText.setText("暂无数据");
        timeText = (TextView) findViewById(R.id.timeText);
        timeText.setText("暂无数据");
        MyHanler myHanler = new MyHanler();
        myThread = new MyThread(myHanler);
        myThread.start();
    }

    public class MyHanler extends Handler {
        MyHanler() {
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Bundle data = new Bundle();
                    data = msg.getData();
                    asuText.setText(data.getInt("ASU") + "");
                    latitudeText.setText(data.getDouble("Latitude") + "");
                    longitudeText.setText(data.getDouble("Longitude") + "");
                    timeText.setText(data.getString("time") + "");
                    break;
            }
        }
    }
}
