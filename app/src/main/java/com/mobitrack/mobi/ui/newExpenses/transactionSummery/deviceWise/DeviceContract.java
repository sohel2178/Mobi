package com.mobitrack.mobi.ui.newExpenses.transactionSummery.deviceWise;


import com.mobitrack.mobi.model.Head;
import com.mobitrack.mobi.model.Tran;
import com.mobitrack.mobi.model.Vehicle;
import com.mobitrack.mobi.ui.newExpenses.transactionSummery.headwise.MyHead;

import java.util.List;

public interface DeviceContract {

    interface Presenter{
        void processData(List<Tran> tranList);
        void setVehicleList(List<Vehicle> vehicleList);
        void filterData(List<Tran> tranList, Head head);
    }

    interface View{
        void clearAdapter();
        void addItem(MyHead myHead);
    }
}
