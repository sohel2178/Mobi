package com.mobitrack.mobi.ui.newExpenses.transactionSummery.headwise;

public class MyHead {

    private String name;
    private double total;

    public MyHead(String name) {
        this.name = name;
    }

    public MyHead() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
