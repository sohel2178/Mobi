package com.mobitrack.mobi.singleton;

import com.mobitrack.mobi.api.model.RData;

import java.util.List;

/**
 * Created by sohel on 6/7/2018.
 */

public class RawFData {
    private static RawFData instance;
    private List<RData> data = null;


    protected RawFData() {

    }

    public static RawFData getInstance() {
        if (instance == null) {
            instance = new RawFData();
        }
        return instance;
    }


    public List<RData> getData() { return data; }
    public void setData(List<RData> data) { this.data = data; }
}
