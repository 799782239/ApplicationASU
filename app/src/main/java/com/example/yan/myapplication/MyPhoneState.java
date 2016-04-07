package com.example.yan.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by yan on 2015/12/4.
 */
public class MyPhoneState extends PhoneStateListener {
    /**
     * Network type is unknown
     */
    public static final int NETWORK_TYPE_UNKNOWN = 0;
    /**
     * Current network is GPRS
     */
    public static final int NETWORK_TYPE_GPRS = 1;
    /**
     * Current network is EDGE
     */
    public static final int NETWORK_TYPE_EDGE = 2;
    /**
     * Current network is UMTS
     */
    public static final int NETWORK_TYPE_UMTS = 3;
    /**
     * Current network is CDMA: Either IS95A or IS95B
     */
    public static final int NETWORK_TYPE_CDMA = 4;
    /**
     * Current network is EVDO revision 0
     */
    public static final int NETWORK_TYPE_EVDO_0 = 5;
    /**
     * Current network is EVDO revision A
     */
    public static final int NETWORK_TYPE_EVDO_A = 6;
    /**
     * Current network is 1xRTT
     */
    public static final int NETWORK_TYPE_1xRTT = 7;
    /**
     * Current network is HSDPA
     */
    public static final int NETWORK_TYPE_HSDPA = 8;
    /**
     * Current network is HSUPA
     */
    public static final int NETWORK_TYPE_HSUPA = 9;
    /**
     * Current network is HSPA
     */
    public static final int NETWORK_TYPE_HSPA = 10;
    /**
     * Current network is iDen
     */
    public static final int NETWORK_TYPE_IDEN = 11;
    /**
     * Current network is EVDO revision B
     */
    public static final int NETWORK_TYPE_EVDO_B = 12;
    /**
     * Current network is LTE
     */
    public static final int NETWORK_TYPE_LTE = 13;
    private Context context;
    private int type;
    private double latitude;
    private double longitude;
    private double outLatitude;
    private double outLongitude;
    private PhoneStateCallBack phoneStateCallBack;

    public MyPhoneState(Context context, int type, PhoneStateCallBack phoneStateCallBack) {
        this.context = context;
        this.type = type;
        this.phoneStateCallBack = phoneStateCallBack;
    }

    //CU中国联通
    //CM中国移动
    //CT中国电信
    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        String tempType = null;
        if (type == TelephonyManager.NETWORK_TYPE_UMTS
                || type == TelephonyManager.NETWORK_TYPE_HSDPA) {
            tempType = "CU3g";
        } else if (type == TelephonyManager.NETWORK_TYPE_GPRS
                || type == TelephonyManager.NETWORK_TYPE_EDGE) {
            tempType = "2g";
        } else if (type == TelephonyManager.NETWORK_TYPE_CDMA) {
            tempType = "CT2g";
        } else if (type == TelephonyManager.NETWORK_TYPE_EVDO_0
                || type == TelephonyManager.NETWORK_TYPE_EVDO_A) {
            tempType = "CT3g";
        } else if (type == TelephonyManager.NETWORK_TYPE_LTE) {
            tempType = "4G";
        } else {
            tempType = "unknown";
        }
        phoneStateCallBack.phoneStateSuccess(Integer.valueOf(signalStrength
                .getGsmSignalStrength()), tempType);
//        Toast.makeText(context, sb.toString() + "La" + outLatitude + "Lo" + outLongitude, Toast.LENGTH_SHORT).show();
    }
}
