package com.mobitrack.mobi.ui.newExpenses;


import com.mobitrack.mobi.model.Head;
import com.mobitrack.mobi.model.Tran;
import com.mobitrack.mobi.model.Vehicle;

import java.util.List;

public interface ExpensesContract {

    interface Presenter{
        void setToolBar();
        void getVehicleList();
        void getHeadList();
        void getAllTransactions();
    }

    interface View{
        void setToolBar();
        void setVehicleList(List<Vehicle> vehicleList);
        void setHeadList(List<Head> headList);
        void setTransactions(List<Tran> transactionList);
        void showDialog();
        void hideDialog();
        void showToast(String message);
    }
}
