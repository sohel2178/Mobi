package com.mobitrack.mobi.ui.monthlyReport;


import android.util.Log;

import com.mobitrack.mobi.api.DeviceClient;
import com.mobitrack.mobi.api.ServiceGenerator;
import com.mobitrack.mobi.api.model.MonthlyReaponse;
import com.mobitrack.mobi.api.model.MonthlyRequestBody;
import com.mobitrack.mobi.model.MonthlyRBody;
import com.mobitrack.mobi.utility.UserLocalStore;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonthlyReportPresenter implements MonthlyReportContract.Presenter {

    private MonthlyReportContract.View mView;
    private UserLocalStore userLocalStore;

    public MonthlyReportPresenter(MonthlyReportContract.View mView,UserLocalStore userLocalStore) {
        this.mView = mView;
        this.userLocalStore = userLocalStore;
    }

    @Override
    public void requestMonthlyData(String imei, int year, int month) {
        MonthlyRequestBody requestBody = new MonthlyRequestBody(imei,year,month);

        mView.showDialog();

        DeviceClient deviceClient = ServiceGenerator.createService(DeviceClient.class);

        Call<MonthlyReaponse> call = deviceClient.getMonthlyData(requestBody);
        call.enqueue(new Callback<MonthlyReaponse>() {
            @Override
            public void onResponse(Call<MonthlyReaponse> call, Response<MonthlyReaponse> response) {
                if(response.code()==200){
                    MonthlyReaponse mr = response.body();
                    mView.updateUI(mr.getData());

                }
            }

            @Override
            public void onFailure(Call<MonthlyReaponse> call, Throwable t) {
                mView.hideDialog();
                mView.showToast("Faied "+t.getMessage());
            }
        });


    }

    @Override
    public void updateMonthText() {
        mView.updateMonthText();
    }

    @Override
    public void updateFuelAndDistance() {
        mView.updateFuelAndDistance();
    }

    @Override
    public void requestForFile(MonthlyRBody monthlyRBody) {

        mView.showDialog();
        if(userLocalStore.getAddress()==null || userLocalStore.getOrganizationName()==null){
            mView.hideDialog();
            mView.showInfoFragment();
        }else{

            monthlyRBody.setAddress(userLocalStore.getAddress());
            monthlyRBody.setCompany_name(userLocalStore.getOrganizationName());

            DeviceClient deviceClient = ServiceGenerator.createService(DeviceClient.class);
            Call<ResponseBody> call = deviceClient.getFile(monthlyRBody);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    mView.hideDialog();

                    if(response.isSuccessful()){
                        String filePath = mView.saveFile(response.body());
                        if(filePath!=null){
                            mView.showToast("File Saved Successfully");
                            mView.openFile(filePath);
                        }else{
                            mView.showToast("Failed to Save the File");
                        }



                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    mView.hideDialog();
                }
            });



        }

    }
}
