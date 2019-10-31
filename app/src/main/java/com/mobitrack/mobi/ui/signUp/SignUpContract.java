package com.mobitrack.mobi.ui.signUp;

import com.google.firebase.auth.FirebaseAuth;
import com.mobitrack.mobi.api.model.RUser;

public interface SignUpContract {

    interface Presenter{
        boolean isValid(String email,String password);
        void signUpWithEmailAndPassword(String email,String password);
        void createUser(RUser rUser);
    }

    interface View{
        void showErrorToast(String message,int fieldId);
        void showDialog();
        void hideDialog();
        void complete();
    }
}
