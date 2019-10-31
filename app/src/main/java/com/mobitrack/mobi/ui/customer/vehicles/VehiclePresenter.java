package com.mobitrack.mobi.ui.customer.vehicles;


import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mobitrack.mobi.model.User;
import com.mobitrack.mobi.model.Vehicle;
import com.mobitrack.mobi.singleton.MyDatabaseRef;

public class VehiclePresenter implements VehicleContract.Presenter {

    private VehicleContract.View mView;

    public VehiclePresenter(VehicleContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void getQuery(User user) {
        Query query = MyDatabaseRef.getInstance().getDeviceRef().orderByChild("uid").equalTo(user.getUid());
        mView.updateAdapter(query);

       /* query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot x: dataSnapshot.getChildren()){
                    Vehicle vehicle = x.getValue(Vehicle.class);

                    Log.d("YYYYYYYYYYYY",vehicle.getDriver_phone());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("YYYYYYYYYYYY",databaseError.getDetails());

            }
        });*/

    }

    @Override
    public void showAssignDialog() {
        mView.showAssignDialog();
    }

    @Override
    public void unassign(Vehicle vehicle) {
        MyDatabaseRef.getInstance().getDeviceRef()
                .child(vehicle.getId())
                .child("uid").setValue(null, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                mView.complete();
            }
        });
    }
}
