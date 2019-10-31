package com.mobitrack.mobi.ui.resetRequest;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.mobitrack.mobi.utility.MyUtil;

public class ResetPresenter implements ResetRequestContract.Presenter {

    private ResetRequestContract.View mView;

    public ResetPresenter(ResetRequestContract.View mView) {
        this.mView = mView;
    }

    @Override
    public boolean validate(String email) {
        mView.clearPreErrors();
        if(email.equals("")){
            mView.showErrorMessage(0,"Email Field Empty");
            return false;
        }

        if(!MyUtil.isValidEmail(email)){
            mView.showErrorMessage(0,"Email not Valid");
        }
        return true;
    }

    @Override
    public void sendResetRequest(String email) {

        mView.showDialog();

        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mView.showMessageDialog();
                        }
                    }
                });

    }
}
