package com.mobitrack.mobi.singleton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by sohel on 07-02-18.
 */

public class MyDatabaseRef {
    private static final String USER_REF="Users";
    private static final String DEVICE_REF="Devices";
    private static final String DATA_REF="Data";
    private static final String SHARED_VEHICLE="SharedVehicle";
    private static final String SHARED_USER="SharedUser";
    private static final String TOKEN_REF="Tokens";

    private static MyDatabaseRef instance;



    private FirebaseDatabase database;

    private MyDatabaseRef() {
        this.database  = FirebaseDatabase.getInstance();
    }

    public static MyDatabaseRef getInstance() {
        if (instance == null) {
            instance = new MyDatabaseRef();
        }
        return instance;
    }

    public DatabaseReference getUserRef(){
        return database.getReference(USER_REF);
    }
    public DatabaseReference getDeviceRef(){
        return database.getReference(DEVICE_REF);
    }
    public DatabaseReference getDataRef(String ime){
        return database.getReference(DATA_REF).child(ime);
    }

    public DatabaseReference getSharedVehicleRef(String uid){
        return database.getReference(SHARED_VEHICLE).child(uid);
    }

    public DatabaseReference getSharedUserRef(String vehicleId){
        return database.getReference(SHARED_USER).child(vehicleId);
    }

    public DatabaseReference getTokenRef(String vehicleId){
        return database.getReference(TOKEN_REF).child(vehicleId);
    }

    public DatabaseReference getRootDataRef(){
        return database.getReference(DATA_REF);
    }

}
