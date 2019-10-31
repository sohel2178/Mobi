package com.mobitrack.mobi.ui.alert;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobitrack.mobi.api.DeviceClient;
import com.mobitrack.mobi.api.ServiceGenerator;
import com.mobitrack.mobi.api.model.AlartDeleteRequest;
import com.mobitrack.mobi.api.model.AlartRequest;
import com.mobitrack.mobi.api.model.AlartResponse;
import com.mobitrack.mobi.api.model.FenceReply;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlertPresenter implements AlertContract.Presenter {

    private AlertContract.View mView;
    private FirebaseUser mCurrentUser;

    public AlertPresenter(AlertContract.View mView) {
        this.mView = mView;
        this.mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void requestForAllAlert() {
        DeviceClient client = ServiceGenerator.createService(DeviceClient.class);

        AlartRequest alartRequest = new AlartRequest(mCurrentUser.getUid());

        Call<AlartResponse> call = client.getAllAlart(alartRequest);

        call.enqueue(new Callback<AlartResponse>() {
            @Override
            public void onResponse(Call<AlartResponse> call, Response<AlartResponse> response) {
                AlartResponse alartResponse = response.body();

                if(alartResponse!=null){
                    if(alartResponse.isSuccess()){
                        mView.updateAdapter(alartResponse.getAlerts());
                    }else{
                        mView.showToast("Data Not Found");
                    }
                }
            }

            @Override
            public void onFailure(Call<AlartResponse> call, Throwable t) {
                mView.showToast("Error in Fetching Data");
            }
        });
    }

    @Override
    public void callForChangeTitle() {
        mView.changeTitle();
    }

    @Override
    public void deleteFenceAlart(String id, final int position) {
        DeviceClient client = ServiceGenerator.createService(DeviceClient.class);
        AlartDeleteRequest alartDeleteRequest = new AlartDeleteRequest(id);

        Call<FenceReply> call = client.deleteFenceAlart(alartDeleteRequest);

        call.enqueue(new Callback<FenceReply>() {
            @Override
            public void onResponse(Call<FenceReply> call, Response<FenceReply> response) {
                FenceReply fenceReply = response.body();

                if(fenceReply!=null){
                    if(fenceReply.isSuccess()){
                        mView.deleteFenceAlart(position);
                    }else {
                        mView.showToast("Problem in Deleting Deleting Fence Alart");
                    }
                }
            }

            @Override
            public void onFailure(Call<FenceReply> call, Throwable t) {
                mView.showToast("Error in Deleting Fence Alart");
            }
        });


    }
}
