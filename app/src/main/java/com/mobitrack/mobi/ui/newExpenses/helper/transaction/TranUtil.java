package com.mobitrack.mobi.ui.newExpenses.helper.transaction;


import com.mobitrack.mobi.model.Head;
import com.mobitrack.mobi.model.Tran;
import com.mobitrack.mobi.model.Vehicle;

import java.util.List;

public class TranUtil {

    public static int getHeadPosition(List<Head> headList, Head head){
        int retInt=-1;
        for (Head x:headList){
            if(x.get_id().equals(head.get_id())){
                retInt = headList.indexOf(x);
                break;
            }
        }

        return retInt;
    }

    public static int getDevicePosition(List<Vehicle> vehicleList, String vehicleID){
        int retInt=-1;
        for (Vehicle x:vehicleList){
            if(x.getId().equals(vehicleID)){
                retInt = vehicleList.indexOf(x);
                break;
            }
        }

        return retInt;
    }

    public static int getTransactionPosition(List<Tran> tranList, Tran tran){
        int retInt=-1;
        for (Tran x:tranList){
            if(x.get_id().equals(tran.get_id())){
                retInt = tranList.indexOf(x);
                break;
            }
        }

        return retInt;
    }
}
