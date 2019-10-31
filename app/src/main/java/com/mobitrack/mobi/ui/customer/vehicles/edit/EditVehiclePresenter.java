package com.mobitrack.mobi.ui.customer.vehicles.edit;

import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.mobitrack.mobi.model.Vehicle;
import com.mobitrack.mobi.singleton.MyDatabaseRef;

public class EditVehiclePresenter implements EditVehicleContract.Presenter {

    private EditVehicleContract.View mView;

    public EditVehiclePresenter(EditVehicleContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void populateView(Vehicle vehicle) {
        mView.populateView(vehicle);
    }

    @Override
    public void updateVehicle(final Vehicle vehicle) {
        MyDatabaseRef.getInstance()
                .getDeviceRef()
                .child(vehicle.getId())
                .setValue(vehicle, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        Log.d("HHHHHH",databaseReference.getKey());
                        databaseReference.child("Data").setValue(vehicle.getData());
                        databaseReference.child("data").setValue(null);
                        mView.complete();
                    }
                });
    }

    @Override
    public boolean validate(Vehicle vehicle) {
        mView.clearPreError();

        if(vehicle.getDriver_name().equals("")){
            mView.setError("Empty Field is not Allowed",1);
            return false;
        }

        if(vehicle.getDriver_phone().equals("")){
            mView.setError("Empty Field is not Allowed",2);
            return false;
        }

        if(vehicle.getModel().equals("")){
            mView.setError("Empty Field is not Allowed",3);
            return false;
        }

        if(vehicle.getDevice_sim_number().equals("")){
            mView.setError("Empty Field is not Allowed",4);
            return false;
        }
        return true;
    }
}
