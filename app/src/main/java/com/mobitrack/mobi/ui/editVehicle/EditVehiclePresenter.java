package com.mobitrack.mobi.ui.editVehicle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.UploadTask;
import com.mobitrack.mobi.model.Vehicle;
import com.mobitrack.mobi.singleton.MyDatabaseRef;
import com.mobitrack.mobi.singleton.MyStorageRef;

import java.util.HashMap;
import java.util.Map;

public class EditVehiclePresenter implements EditVehicleContract.Presenter {

    private EditVehicleContract.View mView;
    private MyDatabaseRef myDatabaseRef;
    private MyStorageRef myStorageRef;

    public EditVehiclePresenter(EditVehicleContract.View mView) {
        this.mView = mView;
        this.myDatabaseRef = MyDatabaseRef.getInstance();
        this.myStorageRef = MyStorageRef.getInstance();
    }

    @Override
    public boolean validate(Vehicle vehicle) {
        mView.clearError();

        if(vehicle.getDriver_name().equals("")){
            mView.showError(1,"Empty Field Not Allowed");
            return false;
        }

        if(vehicle.getDriver_phone().equals("")){
            mView.showError(2,"Empty Field Not Allowed");
            return false;
        }

        if(vehicle.getModel().equals("")){
            mView.showError(3,"Empty Field Not Allowed");
            return false;
        }

        if(vehicle.getMileage()==0){
            mView.showError(4,"Please Set a Numeric Value");
            return false;
        }

        return true;
    }

    @Override
    public void saveVehicle(Vehicle vehicle) {
        Map<String,Object> vehicleMap = new HashMap<>();
        vehicleMap.put("driver_name",vehicle.getDriver_name());
        vehicleMap.put("driver_phone",vehicle.getDriver_phone());
        vehicleMap.put("model",vehicle.getModel());
        vehicleMap.put("mileage",vehicle.getMileage());
        vehicleMap.put("congestion_consumption",vehicle.getCongestion_consumption());

        if(vehicle.getDriver_photo()!=null){
            vehicleMap.put("driver_photo",vehicle.getDriver_photo());
        }

        myDatabaseRef.getDeviceRef()
                .child(vehicle.getId())
                .updateChildren(vehicleMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        mView.complete();
                    }
                });
    }

    @Override
    public void saveVehicleWithImage(final Vehicle vehicle, byte[] bytes) {
        mView.showDialog();

        myStorageRef.getVehicleStoreRef().child(vehicle.getId())
                .putBytes(bytes)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String url = taskSnapshot.getDownloadUrl().toString();
                        vehicle.setDriver_photo(url);
                        saveVehicle(vehicle);
                    }
                });
    }

    @Override
    public void cropImageRequest() {
        mView.openCropImageActivity();
    }
}
