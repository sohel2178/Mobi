package com.mobitrack.mobi.ui.notiTest;

public class NotiPresenter implements NotiContract.Presenter {

    private NotiContract.View mView;

    public NotiPresenter(NotiContract.View mView) {
        this.mView = mView;
    }
}
