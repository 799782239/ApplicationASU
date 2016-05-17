package com.example.yan.myapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.yan.myapplication.R;

/**
 * Created by yanqijs on 2016/5/13.
 */
public class BaseActivity extends AppCompatActivity {
    private Toolbar toolbar;

    public void initTitle() {
        toolbar = (Toolbar) this.findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
