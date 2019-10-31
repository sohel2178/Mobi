package com.mobitrack.mobi.ui.admin;

public class AdminPresenter implements AdminContract.Presenter {

    private AdminContract.View mView;

    public AdminPresenter(AdminContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void loadCustomerFragment() {
        mView.loadCustomerFragment();
    }
}
