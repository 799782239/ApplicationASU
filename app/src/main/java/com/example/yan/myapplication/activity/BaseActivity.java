package com.example.yan.myapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.yan.myapplication.R;

/**
 * Created by yanqijs on 2016/5/13.
 */
public class BaseActivity extends AppCompatActivity {
    private Toolbar toolbar;

    public void initTitle(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolBar);
    }
}
