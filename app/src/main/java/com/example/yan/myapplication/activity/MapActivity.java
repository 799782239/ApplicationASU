package com.example.yan.myapplication.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.yan.myapplication.Config;
import com.example.yan.myapplication.R;
import com.example.yan.myapplication.model.SaveData;
import com.example.yan.myapplication.vo.MapVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yan.db.DbConfig;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MapActivity extends BaseActivity {
    private MapView bMapView;
    private Toolbar toolbar;
    private BaiduMap mBaiduMap;
    private BaiduMapOptions mapOptions;
    private CardView cardView;
    private List<OverlayOptions> overlayOptionsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);
        x.Ext.init(getApplication());
        bMapView = (MapView) findViewById(R.id.baidu_map);

        setTitle("周围的人");
        initTitle();
//        toolbar = (Toolbar) findViewById(R.id.toolBar);
        //地图初始化
        mBaiduMap = bMapView.getMap();
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.i("map", "onclick");
                mBaiduMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getApplicationContext(), "dianji", Toast.LENGTH_SHORT).show();
//                LayoutInflater inflater = getLayoutInflater();
//                View view = inflater.inflate(R.layout.map_content, null);
//                CardView cardView = (CardView) view.findViewById(R.id.cardview);
                Button button = new Button(getApplicationContext());
                button.setBackgroundResource(R.mipmap.content);
                //定义用于显示该InfoWindow的坐标点
                LatLng pt = new LatLng(Double.valueOf(marker.getExtraInfo().getString(DbConfig.ALTITUDE))
                        , Double.valueOf(marker.getExtraInfo().getString(DbConfig.LONGITUDE)));
                //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(button);
                InfoWindow mInfoWindow = new InfoWindow(bitmapDescriptor, pt, -47, new InfoWindow.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick() {
                        Log.i("map", "onclick");
                        mBaiduMap.hideInfoWindow();
                    }
                });

                //显示InfoWindow
                mBaiduMap.showInfoWindow(mInfoWindow);
                return true;
            }
        });
        if (SaveData.data != null) {
            LatLng latLng = new LatLng(SaveData.data.getLaLatitude(), SaveData.data.getLongitude());
            MapStatus mapStatus = new MapStatus.Builder().target(latLng).zoom(16).build();
            //更新状态
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
            mBaiduMap.setMapStatus(mapStatusUpdate);
        }
        MapAsy mapAsy = new MapAsy();
        mapAsy.execute();
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
                            .fromResource(R.mipmap.icon_map);
                    //构建MarkerOption，用于在地图上添加Marker
                    OverlayOptions option = new MarkerOptions()
                            .position(point)
                            .icon(bitmap);
                    //在地图上添加Marker，并显示
                    overlayOptionsList.add(option);
                    Marker marker = (Marker) mBaiduMap.addOverlay(option);
                    Bundle bundle = new Bundle();
                    bundle.putString(DbConfig.ALTITUDE, mapVos.get(i).getLatitude());
                    bundle.putString(DbConfig.LONGITUDE, mapVos.get(i).getLongitude());
                    bundle.putString(DbConfig.ASU, mapVos.get(i).getAsu());
                    marker.setExtraInfo(bundle);
                }
            }
            dialog.dismiss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
