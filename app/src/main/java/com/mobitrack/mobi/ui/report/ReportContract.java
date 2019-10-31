package com.mobitrack.mobi.ui.report;

import com.mobitrack.mobi.api.model.RData;
import com.mobitrack.mobi.api.model.RequestBody;

import java.util.List;

public interface ReportContract {

    interface Presenter{
        void requestForData(RequestBody requestBody);
        void updateTitle();
        void updateFuel();
        void destroy();
        void initialize();
    }

    interface View{
        void showDialog();
        void hideDialog();
        //void updateUI(List<RData> rDataList);
        void updateTitle();
        void updateFuel();
        void updateDistance(double distance);
        void updateFragment();
        void updateTravelTime(String travelTime);
        void visibleBottomNavigationView();
        void showToast(String message);
    }
}
