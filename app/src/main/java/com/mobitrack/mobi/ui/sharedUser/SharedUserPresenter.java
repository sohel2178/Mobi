package com.mobitrack.mobi.ui.sharedUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mobitrack.mobi.model.SharedUser;
import com.mobitrack.mobi.singleton.MyDatabaseRef;

import java.util.List;

public class SharedUserPresenter implements SharedUserContract.Presenter {
    private SharedUserContract.View mView;
    private MyDatabaseRef myDatabaseRef;

    public SharedUserPresenter(SharedUserContract.View mView) {
        this.mView = mView;
        this.myDatabaseRef = MyDatabaseRef.getInstance();
    }

    @Override
    public void requestForAllUsers(String vehicleId) {
        myDatabaseRef.getSharedUserRef(vehicleId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot x: dataSnapshot.getChildren()){
                            SharedUser sharedUser = x.getValue(SharedUser.class);
                            //sharedUserList.add(sharedUser);
                            mView.addSharedUser(sharedUser);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void deleteSharedUser(SharedUser sharedUser, String vehicleId, final int position) {
        myDatabaseRef.getSharedUserRef(vehicleId)
                .child(sharedUser.getId()).setValue(null);

        myDatabaseRef.getSharedVehicleRef(sharedUser.getId())
                .child(vehicleId)
                .setValue(null, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        mView.userDeleted(position);
                    }
                });



    }
}
