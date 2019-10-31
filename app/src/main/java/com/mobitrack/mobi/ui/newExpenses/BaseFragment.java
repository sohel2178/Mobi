package com.mobitrack.mobi.ui.newExpenses;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;


import com.mobitrack.mobi.model.Head;
import com.mobitrack.mobi.model.Tran;
import com.mobitrack.mobi.model.Vehicle;
import com.mobitrack.mobi.ui.newExpenses.helper.transaction.TranUtil;

import java.util.List;

public class BaseFragment extends Fragment {

    private NewExpensesActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getActivity() instanceof NewExpensesActivity){
            activity = (NewExpensesActivity) getActivity();
        }
    }

    public List<Tran> getTransactions(){
        return activity.getTranList();
    }


    public List<Head> getHeadList(){
        return activity.getHeadList();
    }

    public List<Vehicle> getVehicleList(){
        return activity.getVehicleList();
    }

    public void addHead(Head head){
        activity.getHeadList().add(head);
    }

    public void addTransaction(Tran tran){
        activity.getTranList().add(tran);
    }

    public void updateTransaction(Tran tran){
        int pos = TranUtil.getTransactionPosition(activity.getTranList(),tran);
        activity.getTranList().set(pos,tran);
    }

    public void removeTransaction(Tran tran){
        int pos = TranUtil.getTransactionPosition(activity.getTranList(),tran);
        activity.getTranList().remove(pos);
    }



}
