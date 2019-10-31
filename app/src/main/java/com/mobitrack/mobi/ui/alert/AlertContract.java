package com.mobitrack.mobi.ui.alert;

import com.mobitrack.mobi.api.model.Fence;

import java.util.List;

public interface AlertContract {

    interface Presenter{
        void requestForAllAlert();
        void callForChangeTitle();
        void deleteFenceAlart(String id,int position);
    }

    interface View{
        void changeTitle();
        void showToast(String message);
        void updateAdapter(List<Fence> fenceList);
        void deleteFenceAlart(int position);
    }
}
