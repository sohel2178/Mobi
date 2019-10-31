package com.mobitrack.mobi.ui.sharedUser;

import com.mobitrack.mobi.model.SharedUser;

import java.util.List;

public interface SharedUserContract {

    interface Presenter{
        void requestForAllUsers(String vehicleId);
        void deleteSharedUser(SharedUser sharedUser,String vehicleId,int position);

    }

    interface View{
        void addSharedUser(SharedUser sharedUser);
        void userDeleted(int position);
    }
}
