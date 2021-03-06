package com.example.yan.myapplication;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.baidu.mapapi.BMapManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import tools.MyFragmentViewPager;

public class ShowActivity extends AppCompatActivity {
    private ViewPagerAdapter mViewPagerAdapter;
    private TabLayout tableLayout;
    private MyFragmentViewPager viewPager;
    private List<Fragment> myFragments = new ArrayList<>();
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        MyThread.threadIsTrue = true;
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        tableLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (MyFragmentViewPager) findViewById(R.id.viewPager);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        drawerLayout = (DrawerLayout) findViewById(R.id.showDrawer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.hello_blank_fragment);
        drawerToggle.syncState();
        drawerLayout.setDrawerListener(drawerToggle);
        Fragment main = new MainFragment();
        Fragment chart = new ChartFragment();
        Fragment map = new SortFragment();
        myFragments.add(main);
        myFragments.add(chart);
        myFragments.add(map);
        mViewPagerAdapter.setMyFragment(myFragments);
        viewPager.setAdapter(mViewPagerAdapter);
        tableLayout.setTabsFromPagerAdapter(mViewPagerAdapter);
        tableLayout.setupWithViewPager(viewPager);
        //开启服务
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.yan.myapplication_preferences", MODE_PRIVATE);
        Boolean upData = sharedPreferences.getBoolean("switch_connect", true);
        if (upData) {
            Intent intent = new Intent(ShowActivity.this, MyService.class);
            startService(intent);
        }
        //NavigationView的设置
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_share:
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("image/*");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(Intent.EXTRA_SUBJECT, "share");
                        intent.putExtra(Intent.EXTRA_TEXT, "当前的地址" + "，信号强度" + SaveData.data.getAsu());
                        startActivity(Intent.createChooser(intent, "share"));
                        break;
                    case R.id.nav_manage:
                        Intent i = new Intent(ShowActivity.this, SettingActivity.class);
                        startActivity(i);
                        break;
                    case R.id.login_out:
                        MyThread.threadIsTrue = false;
                        Config.isFresh = true;
                        SaveData.data = null;
                        Intent stopIntent = new Intent("android.stop");
                        sendBroadcast(stopIntent);
                        Intent outIntent = new Intent(ShowActivity.this, NewLoginActivity.class);
                        startActivity(outIntent);
                        finish();
                        break;
                    case R.id.nav_map:
                        Intent mapIntent = new Intent(ShowActivity.this, MapActivity.class);
                        startActivity(mapIntent);
                        break;
                    default:
                        drawerLayout.closeDrawers();

                        break;
                }

                return true;
            }
        });
        tableLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
