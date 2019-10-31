package com.mobitrack.mobi.ui.map;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mobitrack.mobi.api.DeviceClient;
import com.mobitrack.mobi.api.ServiceGenerator;
import com.mobitrack.mobi.api.model.Fence;
import com.mobitrack.mobi.api.model.FenceR;
import com.mobitrack.mobi.api.model.FenceReply;
import com.mobitrack.mobi.api.model.ImeiReq;
import com.mobitrack.mobi.model.FireData;
import com.mobitrack.mobi.singleton.MyDatabaseRef;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapPresenter implements MapContract.Presenter {
    private MapContract.View mView;
    private DatabaseReference deviceRef;


    public MapPresenter(MapContract.View mView) {
        this.mView = mView;

    }

    @Override
    public void initView() {
        mView.initView();
    }

    @Override
    public void start() {
        if(deviceRef!=null){
            deviceRef.addChildEventListener(listener);
        }
    }

    @Override
    public void stop() {
        if(deviceRef!=null){
            deviceRef.removeEventListener(listener);
        }
    }

    @Override
    public void startListenFromDevice(String deviceId) {
        this.deviceRef = MyDatabaseRef.getInstance().getDeviceRef().child(deviceId);
        deviceRef.addChildEventListener(listener);
    }

    @Override
    public void requestForFence(String deviceId) {
        ImeiReq imeiReq = new ImeiReq();
        imeiReq.setImei(deviceId);
        DeviceClient deviceClient = ServiceGenerator.createService(DeviceClient.class);

        Call<FenceR> call = deviceClient.getMyFence(imeiReq);

        call.enqueue(new Callback<FenceR>() {
            @Override
            public void onResponse(Call<FenceR> call, Response<FenceR> response) {
                FenceR fenceR = response.body();

                if(fenceR!=null){
                    if(fenceR.isSuccess()){
                        Fence fence = fenceR.getFence();
                        mView.drawFence(fence);
                    }else{
                        //mView.showToast("No Fence Data Found");
                    }
                }


            }

            @Override
            public void onFailure(Call<FenceR> call, Throwable t) {
                mView.showToast("Something Happen Wrong");
            }
        });
    }

    @Override
    public void applyFence(Fence fence) {
        String token = FirebaseInstanceId.getInstance().getToken();
        if(token!=null){
            fence.setUser_token(token);

            DeviceClient deviceClient = ServiceGenerator.createService(DeviceClient.class);

            Call<FenceR> call = deviceClient.applyFence(fence);

            call.enqueue(new Callback<FenceR>() {
                @Override
                public void onResponse(Call<FenceR> call, Response<FenceR> response) {
                    FenceR fenceR = response.body();

                    if(fenceR!=null){
                        if(fenceR.isSuccess()){
                            Fence fence = fenceR.getFence();
                            mView.drawFence(fence);
                        }else{
                            mView.showToast("No Fence Data Found");
                        }
                    }
                }

                @Override
                public void onFailure(Call<FenceR> call, Throwable t) {
                    mView.showToast("Something Happen Wrong");
                }
            });

        }
    }

    @Override
    public void removeFence(String deviceId) {
        ImeiReq imeiReq = new ImeiReq();
        imeiReq.setImei(deviceId);
        DeviceClient deviceClient = ServiceGenerator.createService(DeviceClient.class);

        Call<FenceReply> call = deviceClient.deleteFence(imeiReq);
        call.enqueue(new Callback<FenceReply>() {
            @Override
            public void onResponse(Call<FenceReply> call, Response<FenceReply> response) {
                FenceReply fenceReply = response.body();

                if(fenceReply!=null){
                    if(fenceReply.isSuccess()){
                        mView.removeFence();
                    }
                }
            }

            @Override
            public void onFailure(Call<FenceReply> call, Throwable t) {
                mView.showToast("Something Happen Wrong");
            }
        });
    }

    private ChildEventListener listener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if(dataSnapshot.getValue()!=null){
                if(dataSnapshot.getKey().equals("Data")){
                    FireData fireData;

                    try {
                        fireData = dataSnapshot.getValue(FireData.class);
                    }catch (Exception e){
                        fireData = getManualFireData(dataSnapshot);
                    }
                    mView.setVehicleData(fireData);
                }
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            if(dataSnapshot.getKey().equals("Data")){
                FireData fireData;
                try {
                    fireData = dataSnapshot.getValue(FireData.class);
                }catch (Exception e){
                    fireData = getManualFireData(dataSnapshot);
                }
                mView.updateCurrentVehicle(fireData);

            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    private FireData getManualFireData(DataSnapshot dataSnapshot){
        FireData fireData = new FireData();

        if(dataSnapshot.child("lat").getValue()!=null){
            fireData.setLat(dataSnapshot.child("lat").getValue(String.class));
        }

        if(dataSnapshot.child("lng").getValue()!=null){
            fireData.setLng(dataSnapshot.child("lng").getValue(String.class));
        }

        if(dataSnapshot.child("speed").getValue()!=null){
            fireData.setSpeed(dataSnapshot.child("speed").getValue(String.class));
        }

        Object object = dataSnapshot.child("status").getValue();

        if(object instanceof Long){
            fireData.setStatus(String.valueOf(object));
        }else if(object instanceof String){
            fireData.setStatus(String.valueOf(object));
        }
        return fireData;
    }
}
