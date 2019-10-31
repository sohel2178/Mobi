package com.mobitrack.mobi.ui.vehicleShare;

import android.content.Intent;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.mobitrack.mobi.model.ShareVehicle;
import com.mobitrack.mobi.model.SharedUser;
import com.mobitrack.mobi.model.User;
import com.mobitrack.mobi.singleton.MyDatabaseRef;
import com.mobitrack.mobi.utility.Constant;

public class VehicleSharePresenter implements VehicleShareContract.Presenter {

    private VehicleShareContract.View mView;
    private MyDatabaseRef myDatabaseRef;

    public VehicleSharePresenter(VehicleShareContract.View mView) {
        this.mView = mView;
        this.myDatabaseRef = MyDatabaseRef.getInstance();
    }

    @Override
    public void selectClick() {
        mView.startSearchUserActivityForResult();
    }

    @Override
    public void shareClick(ShareVehicle shareVehicle, final String uid,final String projectId) {
        mView.showDialog();
        myDatabaseRef.getSharedVehicleRef(uid)
                .child(projectId)
                .setValue(shareVehicle, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                        SharedUser sharedUser = new SharedUser(uid,1);

                        myDatabaseRef.getSharedUserRef(projectId).child(uid).setValue(sharedUser, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                mView.complete();

                            }
                        });
                    }
                });
    }


}
