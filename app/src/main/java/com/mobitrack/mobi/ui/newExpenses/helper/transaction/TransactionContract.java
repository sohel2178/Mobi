package com.mobitrack.mobi.ui.newExpenses.helper.transaction;


import com.mobitrack.mobi.model.Tran;

public interface TransactionContract {

    interface Presenter{
        void onCancelClick();

        boolean validate(Tran transaction);
        void saveTransaction(Tran transaction);
        void updateTransaction(Tran transaction);
        void controlView(Tran transaction);
    }

    interface View{
        void dismissDialog();
        void processTransaction(Tran transaction);
        void updateTransactioninView(Tran transaction);
        void showToast(String message);
        void clearPreError();
        void showError(String message, int field);

        void showProgressBar();
        void hideProgressBar();
        void setupForTransactionEntry();
        void setupForTransactionUpdate();
    }
}
