package com.example.yan.myapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.yan.myapplication.Config;

import tools.SharePreferencesUtil;

public class PreActivity extends AppCompatActivity {
    private SharedPreferences mSharedPreferences;
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SharePreferencesUtil sharePreferencesUtil = new SharePreferencesUtil();
        if (SharePreferencesUtil.getData(this, Config.SHARE_USER_CONFIG, Config.SHARE_USER_IS_FIRST, true)) {
            SharePreferencesUtil.putData(false, this, Config.SHARE_USER_CONFIG, Config.SHARE_USER_IS_FIRST);
            i = new Intent(PreActivity.this, RegisterActivity.class);
        } else {
            if (SharePreferencesUtil.getData(this, Config.SHARE_USER_CONFIG, Config.SHARE_TOKEN, false)) {
                i = new Intent(PreActivity.this, ShowActivity.class);
            } else {
                i = new Intent(PreActivity.this, NewLoginActivity.class);
            }
        }
        startActivity(i);
        finish();
    }
}
