package com.mobitrack.mobi.ui.searchUser;


import com.mobitrack.mobi.model.ShareVehicle;
import com.mobitrack.mobi.model.User;

import java.util.List;

public interface SearchUserContract {

    interface Presenter{
        void requestForData(String value, int before, int after);
        void shareClick(ShareVehicle shareVehicle, String uid, String projectId);
    }

    interface View{
        void updateData(User user, String val);
        void updateAdapter(String val);
        void shearClick(User user);
        void shareDone(String uid);
        void showDialog();
    }
}
