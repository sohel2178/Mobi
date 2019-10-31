package com.mobitrack.mobi.ui.main.payment;

public class PaymentPresenter implements PaymentContract.Presenter {

    private PaymentContract.View mView;

    public PaymentPresenter(PaymentContract.View mView) {
        this.mView = mView;
    }
}
