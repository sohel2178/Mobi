package com.mobitrack.mobi.api.model;

import java.util.List;

public class AlartResponse {

    private boolean success;
    private int count;
    private List<Fence> alerts;

    public AlartResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Fence> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<Fence> alerts) {
        this.alerts = alerts;
    }
}
