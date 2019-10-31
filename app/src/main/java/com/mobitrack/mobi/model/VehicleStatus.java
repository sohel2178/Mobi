package com.mobitrack.mobi.model;

/**
 * Created by sohel on 6/7/2018.
 */

public class VehicleStatus {

    private long startTime;
    private long endTime;

    private String status;


    public VehicleStatus() {
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
