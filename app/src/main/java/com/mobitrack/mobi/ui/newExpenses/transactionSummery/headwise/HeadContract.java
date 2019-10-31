package com.mobitrack.mobi.ui.newExpenses.transactionSummery.headwise;


import com.mobitrack.mobi.model.Tran;
import com.mobitrack.mobi.model.Vehicle;

import java.util.List;

public interface HeadContract {

    interface Presenter{
        void processData(List<Tran> tranList);
        void filterData(List<Tran> tranList, Vehicle vehicle);
    }

    interface View{
        void clearAdapter();
        void addItem(MyHead myHead);
    }
}
