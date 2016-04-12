package com.example.yan.myapplication.operate;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yan.myapplication.Imp.OperateCallBack;

import java.util.Map;

/**
 * Created by yanqijs on 2016/4/8.
 */
public class BaseOperate {

    public void requestServer(int method, String url, final OperateCallBack callBack, Context context) {
        StringRequest request = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                callBack.successCallBack(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("volley", volleyError.toString() + "");
                callBack.failCallBack();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return callBack.setMap();
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }
}
