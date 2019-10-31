package com.mobitrack.mobi.ui.signUp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobitrack.mobi.R;
import com.mobitrack.mobi.activities.PrebaseActivity;
import com.mobitrack.mobi.api.model.RUser;

public class SignUpActivity extends PrebaseActivity implements View.OnClickListener,SignUpContract.View {


    private EditText etEmail,etPassword;

    private Button btnSignUp;

    private SignUpPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide Status Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_sign_up);

        mPresenter = new SignUpPresenter(this);

        initView();
    }

    private void initView() {
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        btnSignUp = findViewById(R.id.sign_up);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        boolean valid = mPresenter.isValid(email,password);

        if(!valid){
            return;
        }

        mPresenter.signUpWithEmailAndPassword(email,password);

    }

    /*private void signUpWithEmailAndPassword(String email, String password) {

        showProgressDialog();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //hideProgressDialog();
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d("HHHHH", "createUserWithEmail:success");
                            final FirebaseUser user = mAuth.getCurrentUser();

                            if(user!=null){

                                Log.d("HHHH","USer Found");

                                Log.d("HHHHH",user.isEmailVerified()+"");

                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            hideProgressDialog();
                                            FirebaseAuth.getInstance().signOut();
                                            SignUpActivity.this.finish();
                                        }else {
                                            hideProgressDialog();
                                            Toast.makeText(SignUpActivity.this, "Email Not Send", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });


                            }

                        } else {
                            hideProgressDialog();
                            // If sign in fails, display a message to the user.
                            Log.d("HHHHH", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }*/

    @Override
    public void showErrorToast(String message, int fieldId) {

        switch (fieldId){
            case 1:
                etEmail.requestFocus();
                break;

            case 2:
                etPassword.requestFocus();
                break;
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDialog() {
        showProgressDialog();
    }


    @Override
    public void hideDialog() {
        hideProgressDialog();
    }

    @Override
    public void complete() {
        hideProgressDialog();
        finish();
    }
}
