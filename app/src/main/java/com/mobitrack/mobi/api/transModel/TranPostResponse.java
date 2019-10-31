package com.mobitrack.mobi.api.transModel;


import com.mobitrack.mobi.model.Tran;

public class TranPostResponse {
    private String message;
    private Tran transaction;

    public TranPostResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Tran getTransaction() {
        return transaction;
    }

    public void setTransaction(Tran transaction) {
        this.transaction = transaction;
    }
}
