package com.mobitrack.mobi.singleton;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by IMATPC-12 on 23-Apr-18.
 */

public class TripRoadDataSet {

    private static TripRoadDataSet instance;
    private List<LatLng> data = null;

    protected TripRoadDataSet() {

    }

    public static TripRoadDataSet getInstance() {
        if (instance == null) {
            instance = new TripRoadDataSet();
        }
        return instance;
    }


    public List<LatLng> getData() { return data; }
    public void setData(List<LatLng> data) { this.data = data; }
}
