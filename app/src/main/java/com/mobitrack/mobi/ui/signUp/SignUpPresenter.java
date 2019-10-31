package com.mobitrack.mobi.ui.signUp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.mobitrack.mobi.api.DeviceClient;
import com.mobitrack.mobi.api.ServiceGenerator;
import com.mobitrack.mobi.api.model.FenceReply;
import com.mobitrack.mobi.api.model.RUser;
import com.mobitrack.mobi.model.User;
import com.mobitrack.mobi.singleton.MyDatabaseRef;
import com.mobitrack.mobi.utility.MyUtil;

import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpPresenter implements SignUpContract.Presenter {

    private SignUpContract.View mView;
    private FirebaseAuth mAuth;
    private MyDatabaseRef myDatabaseRef;

    public SignUpPresenter(SignUpContract.View mView) {
        this.mView = mView;
        this.mAuth = FirebaseAuth.getInstance();
        this.myDatabaseRef = MyDatabaseRef.getInstance();
    }

    @Override
    public boolean isValid(String email, String password) {

        if(email.equals("")){
            mView.showErrorToast("Empty Email Field",1);
            return false;
        }

        if(!MyUtil.isValidEmail(email)){
            mView.showErrorToast("Email is not Valid",1);
            return false;
        }

        if(password.equals("")){
            mView.showErrorToast("Empty Password Field",2);
            return false;
        }

        if(password.length()<6){
            mView.showErrorToast("Password at least 6 character long",2);
            return false;
        }
        return true;
    }

    @Override
    public void signUpWithEmailAndPassword(final String email, final String password) {
        mView.showDialog();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) mView, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //hideProgressDialog();
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d("HHHHH", "createUserWithEmail:success");
                            final FirebaseUser user = mAuth.getCurrentUser();

                            if(user!=null){

                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            RUser rUser = new RUser();
                                            rUser.setEmail(email);
                                            rUser.setPassword(password);
                                            rUser.setUid(user.getUid());

                                            createUser(rUser);

                                            User fUser = new User(user.getUid(),user.getEmail());
                                            myDatabaseRef.getUserRef().child(user.getUid())
                                                    .setValue(fUser, new DatabaseReference.CompletionListener() {
                                                        @Override
                                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                            mView.complete();
                                                        }
                                                    });


                                        }else {
                                            mView.hideDialog();
                                            mView.showErrorToast("Failed to Send Varification Email",3);
                                        }

                                    }
                                });


                            }

                        } else {
                            mView.hideDialog();
                            mView.showErrorToast("Signup Failed",3);
                        }
                    }
                });
    }

    @Override
    public void createUser(final RUser rUser) {
        /*DeviceClient deviceClient = ServiceGenerator.createService(DeviceClient.class);
        Call<FenceReply> call = deviceClient.resigterUser(rUser);

        call.enqueue(new Callback<FenceReply>() {
            @Override
            public void onResponse(Call<FenceReply> call, Response<FenceReply> response) {
                FenceReply reply = response.body();

                if(reply.isSuccess()){


                }
            }

            @Override
            public void onFailure(Call<FenceReply> call, Throwable t) {

            }
        });*/
    }
}
