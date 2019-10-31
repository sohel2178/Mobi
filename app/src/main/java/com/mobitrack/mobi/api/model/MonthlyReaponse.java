package com.mobitrack.mobi.api.model;

import java.util.List;

public class MonthlyReaponse {

    private int count;
    private List<MonthlyData> data;

    public MonthlyReaponse() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<MonthlyData> getData() {
        return data;
    }

    public void setData(List<MonthlyData> data) {
        this.data = data;
    }
}
