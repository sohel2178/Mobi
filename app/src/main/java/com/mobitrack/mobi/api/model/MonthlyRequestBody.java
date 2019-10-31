package com.mobitrack.mobi.api.model;

public class MonthlyRequestBody {

    private String imei;
    private int year;
    private int month;

    public MonthlyRequestBody() {
    }

    public MonthlyRequestBody(String imei, int year, int month) {
        this.imei = imei;
        this.year = year;
        this.month = month;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
