package com.mobitrack.mobi.api.model;

public class AlartRequest {

    private String uid;

    public AlartRequest() {
    }

    public AlartRequest(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
