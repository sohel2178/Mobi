package com.mobitrack.mobi.singleton;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by IMATPC-12 on 23-Apr-18.
 */

public class RoadDataSet {

    private static RoadDataSet instance;
    private List<LatLng> data = null;

    protected RoadDataSet() {

    }

    public static RoadDataSet getInstance() {
        if (instance == null) {
            instance = new RoadDataSet();
        }
        return instance;
    }


    public List<LatLng> getData() { return data; }
    public void setData(List<LatLng> data) { this.data = data; }
}
