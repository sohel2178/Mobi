package com.mobitrack.mobi.ui.main;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View mView;
    private FirebaseUser mCurrentUser;


    public MainPresenter(MainContract.View mView) {
        this.mView = mView;
        this.mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void checkAndStart() {
        mView.checkAndStart();
    }

    @Override
    public void checkCurrentUser() {
        if(mCurrentUser==null){
            mView.startLoginActivity();
        }else {
            mView.loadHomeFragment();
        }
    }
}
