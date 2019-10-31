package com.mobitrack.mobi.ui.newExpenses.transactionHistory;

import android.util.Log;


import com.mobitrack.mobi.api.DeviceClient;
import com.mobitrack.mobi.api.ServiceGenerator;
import com.mobitrack.mobi.api.transModel.TranPostResponse;
import com.mobitrack.mobi.model.Tran;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryPresenter implements HistoryContract.Presenter {

    private HistoryContract.View mView;

    public HistoryPresenter(HistoryContract.View mView) {
        this.mView = mView;
    }


    @Override
    public void hideTransactionButton() {
        mView.hideTransactionButton();
    }

    @Override
    public void showTransactionButton() {
        mView.showTransactionButton();
    }

    @Override
    public void hideDeleteContainer() {
        mView.hideDeleteContainer();
    }

    @Override
    public void deleteTransaction(final Tran tran) {

        mView.showProgressBar();

        DeviceClient client = ServiceGenerator.createService(DeviceClient.class);
        Call<TranPostResponse> call = client.deleteTransaction(tran.get_id());

        call.enqueue(new Callback<TranPostResponse>() {
            @Override
            public void onResponse(Call<TranPostResponse> call, Response<TranPostResponse> response) {
                mView.hideProgressBar();

                if(response.code()==200){
                    mView.deletedTransaction(tran);
                    Log.d("HHHHHH",response.code()+" Code");
                }else {
                    Log.d("HHHHHH",response.code()+" Code");
                }


            }

            @Override
            public void onFailure(Call<TranPostResponse> call, Throwable t) {
                mView.hideProgressBar();
                Log.d("HHHHHH",t.getMessage()+" Message");

            }
        });


    }

    @Override
    public void initializeData() {
        mView.initData();
    }
}
