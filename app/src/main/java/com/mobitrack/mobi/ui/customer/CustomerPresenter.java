package com.mobitrack.mobi.ui.customer;

public class CustomerPresenter implements CustomerContract.Presenter {

    private CustomerContract.View mView;

    public CustomerPresenter(CustomerContract.View mView) {
        this.mView = mView;
    }
}
