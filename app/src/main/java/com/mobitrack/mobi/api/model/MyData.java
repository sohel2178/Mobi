package com.mobitrack.mobi.api.model;

import java.util.List;

public class MyData {

    private boolean success;
    private int count;
    private List<RData> locations;

    public MyData() {
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

    public List<RData> getLocations() {
        return locations;
    }

    public void setLocations(List<RData> locations) {
        this.locations = locations;
    }
}
