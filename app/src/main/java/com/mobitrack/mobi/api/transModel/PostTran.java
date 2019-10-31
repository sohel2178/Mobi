package com.mobitrack.mobi.api.transModel;


import com.mobitrack.mobi.model.Tran;

import java.util.Date;

public class PostTran {

    private String _id;
    private Date date;
    private String purpose;
    private String device_id;
    private String customer_id;
    private double amount;
    private String head;

    public PostTran(Tran transaction) {
        this._id = transaction.get_id();
        this.date = transaction.getDate();
        this.purpose = transaction.getPurpose();
        this.device_id = transaction.getDevice_id();
        this.customer_id = transaction.getCustomer_id();
        this.amount = transaction.getAmount();
        this.head = transaction.getHead().get_id();
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }
}
