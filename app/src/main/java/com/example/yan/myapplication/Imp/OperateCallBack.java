package com.example.yan.myapplication.Imp;

import java.util.Map;

/**
 * Created by yanqijs on 2016/4/8.
 */
public interface OperateCallBack {
    void successCallBack(String result);

    void failCallBack();
    Map<String, String> setMap();
}
