package com.mobitrack.mobi.ui.newExpenses.helper.head;


import com.mobitrack.mobi.model.Head;

public interface HeadContract {

    interface Presenter{
        void onCancelClick();
        boolean validate(Head head);
        void saveHead(Head head);
    }

    interface View{
        void dismissDialog();
        void clearPreError();
        void showProgressBar();
        void hideProgressBar();
        void setHead(Head head);
        void showError(String message, int field);
    }
}
