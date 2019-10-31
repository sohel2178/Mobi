package com.mobitrack.mobi.model;

import java.io.Serializable;

public class Account implements Serializable{
    private String _id;
    private String name;
    private String customer_id;
    private int type;


    public Account() {
    }

    public Account(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
