package com.example.yan.myapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PreActivity extends AppCompatActivity {
    private SharedPreferences mSharedPreferences;
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = getSharedPreferences("userConfig", MODE_PRIVATE);
        if (mSharedPreferences.getBoolean("isFirst", true)) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean("isFirst", false);
            editor.commit();
            i = new Intent(PreActivity.this, RegisterActivity.class);
        } else {
            if (mSharedPreferences.getBoolean("Token", false)) {
                i = new Intent(PreActivity.this, ShowActivity.class);
            } else {
                i = new Intent(PreActivity.this, NewLoginActivity.class);
            }
        }
        startActivity(i);
        finish();

    }
}
