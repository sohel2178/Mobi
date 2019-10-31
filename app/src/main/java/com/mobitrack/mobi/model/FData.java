package com.mobitrack.mobi.model;

import com.mobitrack.mobi.utility.MyUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by sohel on 6/7/2018.
 */

public class FData implements Serializable {

    private Date servertime;
    private double lat;
    private double lng;
    private double speed;
    private String status;

    public FData() {
    }

    public Date getServertime() {
        return servertime;
    }

    public void setServertime(Date servertime) {
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
        String time = MyUtil.getStringDate2(this.servertime.getTime());
        return Integer.parseInt(time.split(" ")[1].split(":")[0]);
    }
}
