package com.mobitrack.mobi.ui.main.ownVehicle;

import com.mobitrack.mobi.model.Vehicle;

public interface OwnVehicleContract {

    interface Presenter{
        void listenVehicleFromDatabase();
        void destroy();
    }

    interface View{
        void addToAdapter(Vehicle vehicle);
        void updateVehicle(Vehicle vehicle);
        void removeVehicle(Vehicle vehicle);

        void startMapActivity(Vehicle vehicle);
        void startVehicleEditActivity(Vehicle vehicle);
        void startVehicleShareActivity(Vehicle vehicle);
        void startSharedUserActivity(Vehicle vehicle);
        void callDriver(Vehicle vehicle);
    }
}
