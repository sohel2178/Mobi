package com.mobitrack.mobi.api.transModel;


import com.mobitrack.mobi.model.Tran;

import java.util.List;

public class TranGetResponse {
    private int count;
    private List<Tran> transactions;

    public TranGetResponse() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Tran> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Tran> transactions) {
        this.transactions = transactions;
    }
}
