package com.mobitrack.mobi.api.model;

public class RequestBody {

    private String imei;
    private String device_time;

    public RequestBody() {
    }

    public RequestBody(String imei, String device_time) {
        this.imei = imei;
        this.device_time = device_time;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getDevice_time() {
        return device_time;
    }

    public void setDevice_time(String device_time) {
        this.device_time = device_time;
    }
}
