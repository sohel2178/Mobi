package com.mobitrack.mobi.ui.customer.vehicles.assign;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.mobitrack.mobi.model.FireData;
import com.mobitrack.mobi.model.User;
import com.mobitrack.mobi.model.Vehicle;
import com.mobitrack.mobi.singleton.MyDatabaseRef;

public class AssignPresenter implements AssignContract.Presenter {

    private AssignContract.View mView;
    private User user;
    private Query query;

    public AssignPresenter(AssignContract.View mView,User user) {
        this.mView = mView;
        this.user = user;
        this.query = MyDatabaseRef.getInstance().getDeviceRef().orderByChild("uid").equalTo(null);
    }

    @Override
    public void requestForVehicles() {
        query.addChildEventListener(listener);
    }

    @Override
    public void assignDevice(Vehicle vehicle) {
        MyDatabaseRef.getInstance().getDeviceRef()
                .child(vehicle.getId())
                .child("uid").setValue(user.getUid(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                mView.hideSelf();
            }
        });
    }


    private ChildEventListener listener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            Vehicle vehicle=null;
            try {
                vehicle = dataSnapshot.getValue(Vehicle.class);

            }catch (Exception e){
                vehicle = manuallyParse(dataSnapshot);
            }

            mView.addVehicle(vehicle);

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            Vehicle vehicle=null;
            try {
                vehicle = dataSnapshot.getValue(Vehicle.class);

            }catch (Exception e){
                vehicle = manuallyParse(dataSnapshot);
            }
            mView.updateVehicle(vehicle);
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Vehicle vehicle=null;
            try {
                vehicle = dataSnapshot.getValue(Vehicle.class);

            }catch (Exception e){
                vehicle = manuallyParse(dataSnapshot);
            }

            mView.removeVehicle(vehicle);
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    private Vehicle manuallyParse(DataSnapshot dataSnapshot){
        Vehicle vehicle = new Vehicle();
        vehicle.setData(new FireData());
        vehicle.setId(dataSnapshot.child("id").getValue(String.class));



        if(dataSnapshot.child("driver_name").getValue(String.class)!=null){
            vehicle.setDriver_name(dataSnapshot.child("driver_name").getValue(String.class));
        }

        if(dataSnapshot.child("driver_phone").getValue(String.class)!=null){
            vehicle.setDriver_phone(dataSnapshot.child("driver_phone").getValue(String.class));
        }

        if(dataSnapshot.child("device_sim_number").getValue(String.class)!=null){
            vehicle.setDevice_sim_number(dataSnapshot.child("device_sim_number").getValue(String.class));
        }

        if(dataSnapshot.child("driver_photo").getValue(String.class)!=null){
            vehicle.setDriver_photo(dataSnapshot.child("driver_photo").getValue(String.class));
        }

        if(dataSnapshot.child("model").getValue(String.class)!=null){
            vehicle.setModel(dataSnapshot.child("model").getValue(String.class));
        }

        if(dataSnapshot.child("uid").getValue(String.class)!=null){
            vehicle.setUid(dataSnapshot.child("uid").getValue(String.class));
        }



        if(dataSnapshot.child("Data").child("lat").getValue(String.class)!=null){
            vehicle.getData().setLat(dataSnapshot.child("Data").child("lat").getValue(String.class));
        }

        if(dataSnapshot.child("Data").child("lng").getValue(String.class)!=null){
            vehicle.getData().setLng(dataSnapshot.child("Data").child("lng").getValue(String.class));
        }

        if(dataSnapshot.child("Data").child("speed").getValue(String.class)!=null){
            vehicle.getData().setSpeed(dataSnapshot.child("Data").child("speed").getValue(String.class));
        }

        Object object = dataSnapshot.child("Data").child("status").getValue();

        if(object instanceof Long){
            vehicle.getData().setStatus(String.valueOf(object));
        }else if(object instanceof String){
            vehicle.getData().setStatus(String.valueOf(object));
        }




        if(dataSnapshot.child("vehicle_type").getValue() instanceof Integer){
            vehicle.setVehicle_type(dataSnapshot.child("vehicle_type").getValue(Integer.class));
        }

        if(dataSnapshot.child("mileage").getValue() instanceof Double){
            vehicle.setMileage(dataSnapshot.child("mileage").getValue(Double.class));
        }

        return vehicle;
    }
}
