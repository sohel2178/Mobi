package com.mobitrack.mobi.model;

import android.util.Log;

import java.io.Serializable;

public class FireData implements Serializable{

    private String status;
    private String lat;
    private String lng;
    private String speed;




    public FireData() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }


}
