package com.mobitrack.mobi.ui.newExpenses.transactionHistory;


import com.mobitrack.mobi.model.Tran;

public interface HistoryContract {

    interface Presenter{
        //void getAllHeads();
        void hideTransactionButton();
        void showTransactionButton();
        void hideDeleteContainer();
        void deleteTransaction(Tran tran);
        void initializeData();



    }

    interface View{
        void initData();
        void hideTransactionButton();
        void showTransactionButton();

        void deletedTransaction(Tran tran);
        void onItemClick(Tran tran);
        void onItemLongClick(Tran tran);

        void hideDeleteContainer();

        void showProgressBar();
        void hideProgressBar();
    }
}
