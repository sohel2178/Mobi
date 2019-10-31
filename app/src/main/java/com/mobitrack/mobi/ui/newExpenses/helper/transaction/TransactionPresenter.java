package com.mobitrack.mobi.ui.newExpenses.helper.transaction;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobitrack.mobi.api.DeviceClient;
import com.mobitrack.mobi.api.ServiceGenerator;
import com.mobitrack.mobi.api.transModel.PostTran;
import com.mobitrack.mobi.api.transModel.TranPostResponse;
import com.mobitrack.mobi.model.Tran;
import com.mobitrack.mobi.singleton.MyDatabaseRef;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionPresenter implements TransactionContract.Presenter {

    private TransactionContract.View mView;
    private FirebaseUser mFirebaseUser;
    private MyDatabaseRef myDatabaseRef;

    public TransactionPresenter(TransactionContract.View mView) {
        this.mView = mView;
        this.mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myDatabaseRef = MyDatabaseRef.getInstance();
    }

    @Override
    public void onCancelClick() {
        mView.dismissDialog();
    }

    @Override
    public boolean validate(Tran transaction) {
        mView.clearPreError();


        if(transaction.getPurpose().equals("")){
            mView.showError("Empty Field is not Allowed",1);
            return false;
        }

        if(transaction.getAmount()<=0){
            mView.showError("Transaction Must be Greater Than 0",2);
            return false;
        }

        if(transaction.getHead()==null){
            mView.showToast("Create Account Before Add Transaction");
            return false;
        }

        if(transaction.getDevice_id()==null){
            mView.showToast("Select Device before Adding Transaction");
            return false;
        }

        return true;
    }

    @Override
    public void saveTransaction(Tran transaction) {
        mView.showProgressBar();

        if(mFirebaseUser!=null){
            transaction.setCustomer_id(mFirebaseUser.getUid());

            DeviceClient client = ServiceGenerator.createService(DeviceClient.class);

            Call<TranPostResponse> call = client.saveTransaction(new PostTran(transaction));

            call.enqueue(new Callback<TranPostResponse>() {
                @Override
                public void onResponse(Call<TranPostResponse> call, Response<TranPostResponse> response) {
                    mView.hideProgressBar();
                    if(response.code()==201){
                        Tran tran = response.body().getTransaction();
                        mView.processTransaction(tran);
                    }else {

                        mView.showToast("Something Happen Wrong");
                    }
                }

                @Override
                public void onFailure(Call<TranPostResponse> call, Throwable t) {
                    mView.hideProgressBar();
                    mView.showToast("Something Happen Wrong "+t.getMessage());
                }
            });
        }

    }

    @Override
    public void updateTransaction(final Tran transaction) {
        mView.showProgressBar();

        if(mFirebaseUser!=null){

            DeviceClient client = ServiceGenerator.createService(DeviceClient.class);

            Call<TranPostResponse> call = client.updateTransaction(transaction.get_id(),new PostTran(transaction));

            call.enqueue(new Callback<TranPostResponse>() {
                @Override
                public void onResponse(Call<TranPostResponse> call, Response<TranPostResponse> response) {
                    mView.hideProgressBar();
                    if(response.code()==201){
                        mView.showToast("Transaction Updated");
                        mView.updateTransactioninView(transaction);
                    }else {
                        Log.d("HHHH",response.code()+" Code");
                        mView.showToast("Something Happen Wrong");
                    }
                }

                @Override
                public void onFailure(Call<TranPostResponse> call, Throwable t) {
                    mView.hideProgressBar();
                    Log.d("HHHH",t.getMessage()+" Message");
                    mView.showToast("Something Happen Wrong "+t.getMessage());
                }
            });
        }

    }

    @Override
    public void controlView(Tran transaction) {
        if(transaction==null){
            mView.setupForTransactionEntry();
        }else {
            mView.setupForTransactionUpdate();
        }
    }
}
