package com.mobitrack.mobi.ui.customer.info;


import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.mobitrack.mobi.model.User;
import com.mobitrack.mobi.singleton.MyDatabaseRef;

public class CustomerInfoPresenter implements CustomerInfoContract.Presenter {

    private CustomerInfoContract.View mView;

    public CustomerInfoPresenter(CustomerInfoContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void updateUI(User user) {
       mView.updateUI(user);
    }

    @Override
    public boolean validate(User user) {
        mView.clearPreErrors();

        if(user.getName().equals("")){
            mView.showErrorMessage(1,"Empty Field is not Allowed");
            return false;
        }

        if(user.getPhone().equals("")){
            mView.showErrorMessage(2,"Empty Field is not Allowed");
            return false;
        }

        if(user.getAddress().equals("")){
            mView.showErrorMessage(3,"Empty Field is not Allowed");
            return false;
        }

        if(user.getCompanyName().equals("")){
            mView.showErrorMessage(4,"Empty Field is not Allowed");
            return false;
        }

        return true;
    }

    @Override
    public void updateUser(User user) {

        MyDatabaseRef.getInstance().getUserRef()
                .child(user.getUid())
                .setValue(user, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        mView.complete();
                    }
                });

    }
}
