package com.mobitrack.mobi.singleton;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by IMATPC-12 on 06-Mar-18.
 */

public class MyStorageRef {

    private static final String USER_REF="Users";
    private static final String VEHICLE_REF="Vehicles";

    private static MyStorageRef instance;

    private StorageReference mStorageRef;

    private MyStorageRef() {
        this.mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public static MyStorageRef getInstance() {
        if (instance == null) {
            instance = new MyStorageRef();
        }
        return instance;
    }
    public StorageReference getUserStoreRef(){
        return mStorageRef.child(USER_REF);
    }
    public StorageReference getVehicleStoreRef(){
        return mStorageRef.child(VEHICLE_REF);
    }
}
