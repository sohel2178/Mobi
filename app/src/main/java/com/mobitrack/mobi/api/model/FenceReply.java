package com.mobitrack.mobi.api.model;

public class FenceReply {
    private boolean success;
    private String message;

    public FenceReply() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
