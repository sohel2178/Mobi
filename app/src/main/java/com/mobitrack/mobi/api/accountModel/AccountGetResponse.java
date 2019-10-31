package com.mobitrack.mobi.api.accountModel;


import com.mobitrack.mobi.model.Account;

import java.util.List;

public class AccountGetResponse {

    private int count;
    private List<Account> accounts;


    public AccountGetResponse() {
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
