package com.mobitrack.mobi.model;

import com.mobitrack.mobi.utility.MyUtil;

import java.io.Serializable;

/**
 * Created by sohel on 5/23/2018.
 */

public class Data implements Serializable {
    private long servertime;
    private double lat;
    private double lng;
    private double speed;
    private String status;
    private String value;

    public Data() {
    }

    public long getServertime() {
        return servertime;
    }

    public void setServertime(long servertime) {
        this.servertime = servertime;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSpanNo(){
        String time =MyUtil.getStringDate2(this.servertime);
        return Integer.parseInt(time.split(" ")[1].split(":")[0]);
    }
}
