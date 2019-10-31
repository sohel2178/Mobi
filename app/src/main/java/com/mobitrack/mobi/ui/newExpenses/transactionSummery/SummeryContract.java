package com.mobitrack.mobi.ui.newExpenses.transactionSummery;


import com.mobitrack.mobi.model.Tran;

import java.util.List;

public interface SummeryContract {

    interface Presenter{
        void processData(List<Tran> tranList);
    }

    interface View{
        void setCount(int number);
        void setTotal(double total);
    }
}
