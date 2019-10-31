package com.mobitrack.mobi.ui.newExpenses.transactionSummery.monthly;


import com.mobitrack.mobi.model.Tran;

import java.util.Date;
import java.util.List;

public interface MonthlyContract {

    interface Presenter{
        void updateMonthText();
        void processData(List<Tran> tranList, Date start, Date finish);
    }

    interface View{
        void updateMonth();
        void clearAdapter();
        void addItem(Tran tran);
    }
}
