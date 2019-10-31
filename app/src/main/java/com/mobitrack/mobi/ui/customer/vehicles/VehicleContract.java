package com.mobitrack.mobi.ui.customer.vehicles;

import com.google.firebase.database.Query;
import com.mobitrack.mobi.model.User;
import com.mobitrack.mobi.model.Vehicle;

public interface VehicleContract {

    interface Presenter{

        void getQuery(User user);
        void showAssignDialog();
        void unassign(Vehicle vehicle);

    }
    interface View{
        void updateAdapter(Query query);
        void showAssignDialog();
        void complete();
    }
}
