package com.mobitrack.mobi.model;

import com.mobitrack.mobi.api.model.RData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IMATPC-12 on 20-Feb-18.
 */

public class Span {

    private int spanNo;
    private int frequency;
    private List<RData> rDataList;


    public Span(int spanNo) {
        this.spanNo=spanNo;
        this.frequency =0;
        this.rDataList = new ArrayList<>();
    }

    public int getSpanNo() {
        return spanNo;
    }

    public void setSpanNo(int spanNo) {
        this.spanNo = spanNo;
    }


    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void addFrequency(){
        this.frequency = this.frequency+1;
    }
    public void removeFrequency(){
        this.frequency = this.frequency-1;
    }

    public List<RData> getRDataList() {
        return rDataList;
    }

    public void addRData(RData rData){
        this.rDataList.add(rData);
    }
}
