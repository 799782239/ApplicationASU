package com.example.yan.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import com.example.yan.myapplication.service.MyService;
import com.example.yan.myapplication.R;

public class SettingFragment extends PreferenceFragment {
    private CheckBoxPreference checkBoxPreference;
    private ListPreference listPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferenc);
    }

    //它的触发规则如下：
//    1 先调用onPreferenceClick()方法，如果该方法返回true，则不再调用onPreferenceTreeClick方法 ；
//    如果onPreferenceClick方法返回false，则继续调用onPreferenceTreeClick方法。
//            2 onPreferenceChange的方法独立与其他两种方法的运行。也就是说，它总是会运行。

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        System.out.println("----------change---->");
        switch (preference.getKey()) {
            case "switch_connect":
                checkBoxPreference = (CheckBoxPreference) findPreference("switch_connect");
                listPreference = (ListPreference) findPreference("time");
                if (checkBoxPreference.isChecked()) {
                    listPreference.setEnabled(checkBoxPreference.isChecked());
                    Intent i = new Intent("android.upfresh");
                    getActivity().sendBroadcast(i);
                    Intent intent = new Intent(getActivity(), MyService.class);
                    getActivity().startService(intent);
                } else {
                    Intent i = new Intent("android.stop");
                    getActivity().sendBroadcast(i);
                    listPreference.setEnabled(checkBoxPreference.isChecked());
                }

                break;
            case "time":
//                Intent i = new Intent("android.stop");
//                getActivity().sendBroadcast(i);
//                Intent intent = new Intent(getActivity(), MyService.class);
//                getActivity().startService(intent);
                break;
            default:
                break;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
