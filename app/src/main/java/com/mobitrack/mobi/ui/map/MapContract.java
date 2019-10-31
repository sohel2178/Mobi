package com.mobitrack.mobi.ui.map;

import com.mobitrack.mobi.api.model.Fence;
import com.mobitrack.mobi.model.FireData;

public interface MapContract {

    interface Presenter{
        void initView();
        void start();
        void stop();
        void startListenFromDevice(String deviceId);
        void requestForFence(String deviceId);
        void applyFence(Fence fence);
        void removeFence(String deviceId);

    }

    interface View{
        void initView();
        void setVehicleData(FireData fireData);
        void updateCurrentVehicle(FireData fireData);
        void showToast(String message);
        void drawFence(Fence fence);
        void removeFence();
    }
}
