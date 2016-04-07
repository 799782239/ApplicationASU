package com.example.yan.myapplication;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.yan.myapplication.vo.MapVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MapActivity extends AppCompatActivity implements View.OnClickListener {
    private MapView bMapView;
    private Toolbar toolbar;
    private BaiduMap mBaiduMap;
    private BaiduMapOptions mapOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);
        x.Ext.init(getApplication());
        bMapView = (MapView) findViewById(R.id.baidu_map);
        toolbar = (Toolbar) findViewById(R.id.map_toolbar);
        //地图初始化
        mBaiduMap = bMapView.getMap();
//        System.out.println("asdasdasdasdasdasd" + SaveData.data.getAsu() + "");
//        System.out.println("asdasdasdasdasdasd" + SaveData.data.getDate() + "");
        if (SaveData.data.getDate() != null) {
            LatLng latLng = new LatLng(SaveData.data.getLaLatitude(), SaveData.data.getLongitude());
            MapStatus mapStatus = new MapStatus.Builder().target(latLng).zoom(16).build();
            //更新状态
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
            mBaiduMap.setMapStatus(mapStatusUpdate);
        }
        //设置toolbar
        setSupportActionBar(toolbar);
        //设置toolbar后调用setDisplayHomeAsUpEnabled
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(MapActivity.this, ShowActivity.class);
//                startActivity(i);
                finish();
            }
        });
        MapAsy mapAsy = new MapAsy();
        mapAsy.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.map_toolbar:

        }
    }

    public class MapAsy extends AsyncTask<String, String, List<MapVo>> {
        private String result;
        private Dialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(MapActivity.this, "", "正在努力加载。。。", true, false);
        }

        @Override
        protected List<MapVo> doInBackground(String... params) {
            Date endDate = new Date(System.currentTimeMillis());
            Date startDate = new Date(System.currentTimeMillis() - 86400000);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            RequestParams requestParams = new RequestParams(Config.URL_AROUND);
            System.out.println(sdf.format(startDate) + "");
            System.out.println(sdf.format(endDate) + "");
            requestParams.addBodyParameter("starttime", sdf.format(startDate) + "");
            requestParams.addBodyParameter("endtime", sdf.format(endDate) + "");
            try {
                result = x.http().postSync(requestParams, String.class);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            if (TextUtils.isEmpty(result)) {
                return null;
            }
            System.out.println(result);
            Gson gson = new Gson();
            MapVo mapVo = new MapVo();
            System.out.println(result + "");
            List<MapVo> mapVos = new ArrayList<>();
            Type type = new TypeToken<ArrayList<MapVo>>() {
            }.getType();
            mapVos = gson.fromJson(result, type);
            return mapVos;
        }

        @Override
        protected void onPostExecute(List<MapVo> mapVos) {
            super.onPostExecute(mapVos);
            if (mapVos == null) {
            } else {
                for (int i = 0; i < mapVos.size(); i++) {
                    //定义Maker坐标点
                    LatLng point = new LatLng(Double.valueOf(mapVos.get(i).getLatitude()), Double.valueOf(mapVos.get(i).getLongitude()));
                    //构建Marker图标
                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.icon_gcoding);
                    //构建MarkerOption，用于在地图上添加Marker
                    OverlayOptions option = new MarkerOptions()
                            .position(point)
                            .icon(bitmap);
                    //在地图上添加Marker，并显示
                    mBaiduMap.addOverlay(option);
                }
            }
            dialog.dismiss();
        }
    }
}
