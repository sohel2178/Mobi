package com.mobitrack.mobi.ui.customer.vehicles.assign;

import com.mobitrack.mobi.model.Vehicle;

public interface AssignContract {

    interface Presenter{
        void requestForVehicles();
        void assignDevice(Vehicle vehicle);
    }

    interface View{
        void hideSelf();
        void addVehicle(Vehicle vehicle);
        void updateVehicle(Vehicle vehicle);
        void removeVehicle(Vehicle vehicle);
        void assignDevice(Vehicle vehicle);
    }
}
