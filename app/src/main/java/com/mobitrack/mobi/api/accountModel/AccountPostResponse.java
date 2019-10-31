package com.mobitrack.mobi.api.accountModel;


import com.mobitrack.mobi.model.Account;

public class AccountPostResponse {

    private String message;
    private Account account;

    public AccountPostResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
