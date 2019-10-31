package com.mobitrack.mobi.api.model;

import com.mobitrack.mobi.model.Vehicle;
import com.mobitrack.mobi.utility.MyUtil;

import java.util.Date;

public class Fence {
    private String _id;
    private String imei;
    private String uid;
    private String driver_name;
    private String driver_photo;
    private String fence_time;
    private String user_token ;
    private String model;
    private double lat;
    private double lng;
    private double new_lat;
    private double new_lng;

    public Fence() {
    }

    public Fence(Vehicle vehicle){
        this.imei=vehicle.getId();
        this.uid = vehicle.getUid();
        this.driver_name=vehicle.getDriver_name();
        this.driver_photo=vehicle.getDriver_photo();
        this.model=vehicle.getModel();
        this.lat=(double)Long.parseLong(vehicle.getData().getLat(),16)/1800000;
        this.lng=(double)Long.parseLong(vehicle.getData().getLng(),16)/1800000;
        //this.lng=vehicle.getData().getLng();
        this.fence_time= MyUtil.getStringDate3(new Date());
        this.user_token = "Sohel Token";
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getDriver_photo() {
        return driver_photo;
    }

    public void setDriver_photo(String driver_photo) {
        this.driver_photo = driver_photo;
    }

    public String getFence_time() {
        return fence_time;
    }

    public void setFence_time(String fence_time) {
        this.fence_time = fence_time;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public double getNew_lat() {
        return new_lat;
    }

    public void setNew_lat(double new_lat) {
        this.new_lat = new_lat;
    }

    public double getNew_lng() {
        return new_lng;
    }

    public void setNew_lng(double new_lng) {
        this.new_lng = new_lng;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
