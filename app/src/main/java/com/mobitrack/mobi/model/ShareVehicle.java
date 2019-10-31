package com.mobitrack.mobi.model;

/**
 * Created by sohel on 5/27/2018.
 */

public class ShareVehicle {

    private int status;
    private String vehicleId;

    public ShareVehicle() {
    }

    public ShareVehicle(int status, String vehicleId) {
        this.status = status;
        this.vehicleId = vehicleId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }
}
