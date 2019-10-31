package com.mobitrack.mobi.ui.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.mobitrack.mobi.R;
import com.mobitrack.mobi.activities.BaseActivity;
import com.mobitrack.mobi.ui.resetRequest.ResetRequestActivity;
import com.mobitrack.mobi.activities.PrebaseActivity;
import com.mobitrack.mobi.ui.main.MainActivity;
import com.mobitrack.mobi.ui.resetRequest.dialog.DialogClickListener;
import com.mobitrack.mobi.ui.resetRequest.dialog.InfoDialog;
import com.mobitrack.mobi.ui.signUp.SignUpActivity;
import com.mobitrack.mobi.utility.Constant;

public class LoginActivity extends BaseActivity implements View.OnClickListener,LoginContract.View {

    private static final int RC_SIGN_IN = 9001;
    private SignInButton signInButton;

    private FirebaseAuth mAuth;
    private EditText etEmail,etPassword;


    private TextView tvLogin,tvForgetPassword,tvSignUp;
    private ImageView ivFacebook,ivTwitter,ivLinkedIn,ivPhone;

    private LoginPresenter mPresenter;

    private InfoDialog infoDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide Status Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        mPresenter = new LoginPresenter(this);



        mAuth = FirebaseAuth.getInstance();

        initView();
    }

    private void initView() {
        signInButton = findViewById(R.id.google_sign_in);
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        tvForgetPassword = findViewById(R.id.forget_password);

        ivFacebook = findViewById(R.id.facebook);
        ivTwitter = findViewById(R.id.twitter);
        ivLinkedIn = findViewById(R.id.linked_in);
        ivPhone = findViewById(R.id.phone);

        ivFacebook.setOnClickListener(this);
        ivTwitter.setOnClickListener(this);
        ivLinkedIn.setOnClickListener(this);
        ivPhone.setOnClickListener(this);

        tvLogin = findViewById(R.id.login);
        tvSignUp = findViewById(R.id.sign_up);
        tvLogin.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);
        signInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.login:
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                boolean valid = mPresenter.validate(email,password);

                if(!valid){
                    return;
                }

                mPresenter.signIn(email,password);
                break;

            case R.id.google_sign_in:
                mPresenter.google_click();
                break;

            case R.id.sign_up:
                startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
                break;

            case R.id.forget_password:
                startResetRequestActivity();
                break;

            case R.id.facebook:
                mPresenter.facebookClick();
                break;

            case R.id.twitter:
                mPresenter.twitterClick();
                break;

            case R.id.linked_in:
                mPresenter.linkedinClick();
                break;

            case R.id.phone:
                mPresenter.phoneClick();
                break;
        }



    }

    private void startResetRequestActivity(){
        finish();
        Intent intent = new Intent(getApplicationContext(),ResetRequestActivity.class);
        startActivity(intent);
    }


    @Override
    public void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(getGoogleApiClient());
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
    public void handledatabaseError() {
        hideProgressDialog();
        Toast.makeText(this, "Error Happen in Database", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideDialogandFinish() {
        hideProgressDialog();
        finish();
        startActivity(new Intent(this,MainActivity.class));
    }

    @Override
    public void showAutheticationFailureToast() {
        Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProDialog() {
        showProgressDialog();
    }

    @Override
    public void hideProDialog() {
        hideProgressDialog();
    }

    @Override
    public void showToastMessage(String message, int fieldId) {
        switch (fieldId){
            case 1:
                etEmail.requestFocus();
                break;
            case 2:
                etPassword.requestFocus();
                break;
        }

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showVarificationDialog() {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.CONTENT,"We send a verification mail to your Email. Please verify and then Login");

        if(infoDialog == null){
            infoDialog = new InfoDialog();
        }

        infoDialog.setListener(new DialogClickListener() {
            @Override
            public void positiveButtonClick() {
                LoginActivity.this.finish();
            }
        });
        infoDialog.setArguments(bundle);
        infoDialog.show(getSupportFragmentManager(),"VERIFICATION");




        /*String titleText ="Verification";
        // Initialize a new foreground color span instance
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.WHITE);

        // Initialize a new spannable string builder instance
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);

        // Apply the text color span
        ssBuilder.setSpan(
                foregroundColorSpan,
                0,
                titleText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        final AlertDialog alertDialog = new AlertDialog.Builder(this,R.style.CustomDialogTheme).create();
        alertDialog.setTitle(ssBuilder);
        alertDialog.setMessage("Your Email is not Verified!!! \nPlease Check Your Email....");
        Drawable drawable = ContextCompat.getDrawable(this,R.drawable.info);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable.setTint(getResources().getColor(R.color.indicator_4));
        }
        alertDialog.setIcon(drawable);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.indicator_4));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.indicator_4));
            }
        });



        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LoginActivity.this.finish();
            }
        });

        alertDialog.show();*/
    }

    @Override
    public void complete() {
        finish();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }

    @Override
    public void openFacebookPage() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.face_book_link)));
        startActivity(browserIntent);
    }

    @Override
    public void openTwitterPage() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.twitter_link)));
        startActivity(browserIntent);
    }

    @Override
    public void openLinkedInPage() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.linked_in_link)));
        startActivity(browserIntent);
    }

    @Override
    public void callCusmonerCare() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", getString(R.string.mobi_phone), null));
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case RC_SIGN_IN:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                mPresenter.startAutentication(result);
                break;
        }


    }
}
