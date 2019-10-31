package com.mobitrack.mobi.ui.vehicleShare;

import com.mobitrack.mobi.model.ShareVehicle;

public interface VehicleShareContract {

    interface Presenter{
        void selectClick();
        void shareClick(ShareVehicle shareVehicle, String uid, String projectId);
    }

    interface View{
        void showDialog();
        void startSearchUserActivityForResult();
        void complete();
    }
}
