package com.mobitrack.mobi.api.model;

public class AlartDeleteRequest {

    private String id;

    public AlartDeleteRequest() {
    }

    public AlartDeleteRequest(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
