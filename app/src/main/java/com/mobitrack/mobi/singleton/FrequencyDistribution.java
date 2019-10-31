package com.mobitrack.mobi.singleton;


import android.util.Log;

import com.mobitrack.mobi.api.model.RData;
import com.mobitrack.mobi.model.Data;
import com.mobitrack.mobi.model.FData;
import com.mobitrack.mobi.model.Span;
import com.mobitrack.mobi.utility.MyUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by IMATPC-12 on 24-Apr-18.
 */

public class FrequencyDistribution {

    private static FrequencyDistribution instance;
    private List<RData> dataList = null;

    private long startTime;

    private List<Span> spanList;

    private boolean isFinished;

    protected FrequencyDistribution() {

    }

    public static FrequencyDistribution getInstance() {
        if (instance == null) {
            instance = new FrequencyDistribution();
        }
        return instance;
    }


    public List<RData> getData() { return dataList; }
    public void setData(final List<RData> dataList) {
        this.dataList = dataList;
        this.startTime = MyUtil.getBeginingTime(dataList.get(0).getServerTime());

        spanList = MyUtil.getSpanList();

        //Log.d("SohekLALLAL",spanList.size()+"");

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<dataList.size();i++){
                    //spanList.get(dataList.get(i).getSpanNo()).addFrequency();
                    spanList.get(dataList.get(i).getSpanNo()).addRData(dataList.get(i));
                }

                //spanList.get(0).removeFrequency();

                isFinished = true;


            }
        }).start();


    }

    public boolean isFinished() {
        return isFinished;
    }

    public List<Span> getSpanList(){
        return this.spanList;
    }


}
