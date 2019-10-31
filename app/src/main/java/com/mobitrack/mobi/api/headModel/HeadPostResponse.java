package com.mobitrack.mobi.api.headModel;


import com.mobitrack.mobi.model.Head;

public class HeadPostResponse {

    private String message;
    private Head head;

    public HeadPostResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }
}
