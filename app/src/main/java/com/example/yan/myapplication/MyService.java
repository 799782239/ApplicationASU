package com.example.yan.myapplication;

import android.app.Service;
import android.content.AsyncQueryHandler;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.yan.db.DbConfig;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yan on 2015/12/8.
 */
public class MyService extends Service {
    private TelephonyManager telephonyManager;
    private SharedPreferences mSharedPreferences;
    private Boolean isFirst = true;
    private Boolean up = true;
    public LocationClient mLocationClient = null;
    public BDLocationListener mBdLocationListener = new MyLocation();
    private double mLatitude = -100000;
    private double mLongitude = -100000;
    private MyReciver myReciver;
    private MyPhoneState myPhoneStateListener;
    private ContentResolver mContentResolver;
    private BackgroundQueryHandler mBackgroundQueryHandler;
    private Date date;
    private int asu;
    private String tempType;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("------destory---->");
        unregisterReceiver(myReciver);
        // 停止监听-To unregister a listener, pass the listener object and set the events argument to LISTEN_NONE (0).
        telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        mLocationClient.stop();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isFirst = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        boolean upData = true;
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.yan.myapplication_preferences", MODE_PRIVATE);
        upData = sharedPreferences.getBoolean("switch_connect", true);
        myReciver = new MyReciver(this);
        registerReceiver(myReciver, new IntentFilter("android.stop"));
        //int age=sharePreferences.getInt("Age",20);
        // 这句话的意思是先从sharePreferences里面找key 为“Age”的数据，如果有，说明你事先保存过，那就取“Age”对应的值，也就是你事先保存过的值，如果没找到key 为“
        // Age”的，最后的 int age 将被赋予你给的默认值20，也就是说那仅仅是一个默认值，只有在从sp对象里取值失败的时候才会使用。
//        mSharedPreferences = getSharedPreferences("First", MODE_PRIVATE);
        if (isFirst) {
//            SharedPreferences.Editor editor = mSharedPreferences.edit();
//            editor.putBoolean("isFirst", false);
//            editor.commit();
            //baiduMap
            x.Ext.init(getApplication());
            if (upData) {
                mLocationClient = new LocationClient(getApplicationContext());
                mLocationClient.registerLocationListener(mBdLocationListener);
                initLocation();
                mLocationClient.start();
                //
                telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                myPhoneStateListener = new MyPhoneState(this, telephonyManager.getNetworkType(), new PhoneStateCallBack() {
                    @Override
                    public void phoneStateSuccess(int result, final String type) {
                        if (mLatitude != -100000 && up) {
                            //写入Json
                            UserData userData = new UserData();
                            asu = result;
                            userData.setAsu(asu);
                            date = new Date(System.currentTimeMillis());
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            userData.setDate(sdf.format(date));
                            userData.setLaLatitude(mLatitude);
                            userData.setLongitude(mLongitude);
                            tempType = type;
                            userData.setType(tempType);
                            SaveData.data = userData;
//                            System.out.println(SaveData.data.get(SaveData.data.size() - 1).getAsu() + ":" + SaveData.data.get(SaveData.data.size() - 1).getDate());
                            Gson gson = new Gson();
                            String str = gson.toJson(userData);

                            //开启上传线程
                            UpThread upThread = new UpThread();
                            upThread.start();
                            RequestParams params = new RequestParams(Config.URL_HOST);
                            params.addBodyParameter("Json", str);
                            mContentResolver = getContentResolver();
                            x.http().post(params, new Callback.CommonCallback<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    if (result.equals("success")) {
                                        System.out.println("------success--->");
                                        mBackgroundQueryHandler = new BackgroundQueryHandler(mContentResolver);
                                        ContentValues c = new ContentValues();
                                        c.put(DbConfig.ASU, asu);
                                        c.put(DbConfig.ALTITUDE, mLatitude);
                                        c.put(DbConfig.LONGITUDE, mLongitude);
                                        c.put(DbConfig.TYPE, tempType);
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        c.put(DbConfig.DATE, sdf.format(date));
                                        mBackgroundQueryHandler.startInsert(0, null, DbConfig.CONTENT_NOTE_DATA_URI, c);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "未能成功上传", Toast.LENGTH_SHORT).show();
                                    }
                                    System.out.println("-----result-->" + result);
                                }

                                @Override
                                public void onError(Throwable ex, boolean isOnCallback) {
                                    Toast.makeText(x.app(), ex.getMessage() + "", Toast.LENGTH_SHORT);
                                    System.out.println("------error----->" + ex.getMessage());
                                }

                                @Override
                                public void onCancelled(CancelledException cex) {

                                }

                                @Override
                                public void onFinished() {

                                }

                            });
                            System.out.println(str);

                        }
                    }
                });
                telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
                System.out.println("---------------------------->>>>>>");
            } else {
                stopSelf();
            }
            isFirst = false;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 10000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    public class MyLocation implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            mLatitude = bdLocation.getLatitude();
            mLongitude = bdLocation.getLongitude();
            StringBuffer sb = new StringBuffer(256);
            if (bdLocation.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (bdLocation.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            System.out.println(sb);
            System.out.println("----------->" + mLongitude + "-------->" + mLatitude);
        }
    }

    public class UpThread extends Thread {


        public UpThread() {
        }

        @Override
        public void run() {
            super.run();
            up = false;
            try {
                SharedPreferences sharedPreferences = getSharedPreferences("com.example.yan.myapplication_preferences", MODE_PRIVATE);
                String time = sharedPreferences.getString("time", "default");
                int tempTime = 0;
                switch (time) {
                    case "default":
                        tempTime = 0;
                        break;
                    case "1minutes":
                        tempTime = 60;
                        break;
                    case "30minutes":
                        tempTime = 1800;
                        break;
                    default:
                        tempTime = 0;
                        break;
                }
                System.out.println(tempTime + "");
                Thread.sleep(1000 * tempTime);
                up = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class MyReciver extends BroadcastReceiver {
        private MyService myService;

        public MyReciver(MyService myService) {
            this.myService = myService;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            myService.stopSelf();
        }
    }

    private final class BackgroundQueryHandler extends AsyncQueryHandler {
        public BackgroundQueryHandler(ContentResolver contentResolver) {
            super(contentResolver);
        }

        @Override
        protected void onInsertComplete(int token, Object cookie, Uri uri) {
            // Toast.makeText(getActivity(), "完成写入",
            // Toast.LENGTH_SHORT).show();
            System.out.println("pause");
            super.onInsertComplete(token, cookie, uri);
        }
    }

}
