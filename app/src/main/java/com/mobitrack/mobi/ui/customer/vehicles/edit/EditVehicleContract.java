package com.mobitrack.mobi.ui.customer.vehicles.edit;


import com.mobitrack.mobi.model.Vehicle;

public interface EditVehicleContract {

    interface Presenter{
        void populateView(Vehicle vehicle);
        void updateVehicle(Vehicle vehicle);

        boolean validate(Vehicle vehicle);
    }

    interface View{
        void populateView(Vehicle vehicle);
        void clearPreError();
        void setError(String message, int field);
        void complete();
    }
}
