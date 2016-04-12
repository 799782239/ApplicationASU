package com.example.yan.myapplication.model;

import java.util.Date;

/**
 * Created by yan on 2015/12/4.
 */
public class UserData {
    private int asu;
    private String date;
    private double LaLatitude;
    private double Longitude;
    private String type;
    private String model;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLaLatitude() {
        return LaLatitude;
    }

    public void setLaLatitude(double laLatitude) {
        LaLatitude = laLatitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public int getAsu() {
        return asu;
    }

    public void setAsu(int asu) {
        this.asu = asu;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
