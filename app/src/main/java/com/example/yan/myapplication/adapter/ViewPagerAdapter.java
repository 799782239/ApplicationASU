package com.example.yan.myapplication.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yan on 2016/1/12.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> myFragment = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public List<Fragment> getMyFragment() {
        return myFragment;
    }

    public void setMyFragment(List<Fragment> myFragment) {
        this.myFragment = myFragment;
    }

    private String title[] = {"实时数据", "本地数据", "分享比较"};


    @Override
    public Fragment getItem(int position) {
        return myFragment.get(position);
    }

    @Override
    public int getCount() {
        return myFragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
