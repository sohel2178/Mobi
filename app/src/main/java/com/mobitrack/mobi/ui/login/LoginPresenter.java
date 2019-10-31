package com.mobitrack.mobi.ui.login;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mobitrack.mobi.model.User;
import com.mobitrack.mobi.singleton.MyDatabaseRef;
import com.mobitrack.mobi.ui.main.MainActivity;
import com.mobitrack.mobi.utility.MyUtil;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mView;
    private FirebaseAuth mAuth;
    private MyDatabaseRef myDatabaseRef;

    public LoginPresenter(LoginContract.View mView) {
        this.mView = mView;
        this.mAuth = FirebaseAuth.getInstance();
        this.myDatabaseRef = MyDatabaseRef.getInstance();
    }

    @Override
    public boolean validate(String email, String password) {
        if(email.equals("")){
            mView.showToastMessage("Empty Value Not Allowed",1);
            return false;
        }

        if(!MyUtil.isValidEmail(email)){
            mView.showToastMessage("Email is not Valid",1);
            return false;
        }

        if(password.equals("")){
            mView.showToastMessage("Empty Value Not Allowed",2);
            return false;
        }
        return true;
    }

    @Override
    public void signIn(String email, String password) {
        mView.showProDialog();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) mView, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            final FirebaseUser user = mAuth.getCurrentUser();

                            if(user.isEmailVerified()){
                                //hideProgressDialog();

                                MyDatabaseRef.getInstance().getUserRef()
                                        .child(user.getUid())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.getValue()==null){
                                                    MyDatabaseRef.getInstance().getUserRef()
                                                            .child(user.getUid()).setValue(new User(user.getUid(),user.getEmail()), new DatabaseReference.CompletionListener() {
                                                        @Override
                                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                            mView.complete();
                                                        }
                                                    });

                                                }else{
                                                   mView.complete();
                                                }

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                            }else {
                                mAuth.signOut();
                                mView.showVarificationDialog();
                            }


                        } else {
                            mView.hideProDialog();
                            mView.showToastMessage("Authentication Failed",5);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void google_click() {
        mView.googleSignIn();
    }

    public void startAutentication(GoogleSignInResult result) {
        mView.showDialog();
        if (result.isSuccess()) {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthWithGoogle(account);
        } else {
            mView.hideDialog();
            mView.showAutheticationFailureToast();
        }

    }

    @Override
    public void facebookClick() {
        mView.openFacebookPage();
    }

    @Override
    public void twitterClick() {
        mView.openTwitterPage();
    }

    @Override
    public void linkedinClick() {
        mView.openLinkedInPage();
    }

    @Override
    public void phoneClick() {
        mView.callCusmonerCare();
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user!=null){

                        myDatabaseRef.getUserRef().child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getValue()==null){

                                    myDatabaseRef.getUserRef().child(user.getUid()).setValue(new User(user), new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            mView.hideDialogandFinish();
                                        }
                                    });

                                }else{
                                    mView.hideDialogandFinish();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                mView.handledatabaseError();
                            }
                        });

                    }

                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mView.handledatabaseError();
            }
        });
    }
}
