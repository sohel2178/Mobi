package com.mobitrack.mobi.api.model;

public class AccountReq {

    private String customer_id;

    public AccountReq(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }
}
