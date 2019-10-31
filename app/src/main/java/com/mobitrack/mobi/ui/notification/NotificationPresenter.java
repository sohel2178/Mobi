package com.mobitrack.mobi.ui.notification;

public class NotificationPresenter implements NotificationContract.Presenter {

    private NotificationContract.View mView;

    public NotificationPresenter(NotificationContract.View mView) {
        this.mView = mView;
    }
}
