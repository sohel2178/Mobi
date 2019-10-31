package com.mobitrack.mobi.ui.resetRequest;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.mobitrack.mobi.R;

public class ResetRequestActivity extends AppCompatActivity implements ResetRequestContract.View {

    private Button btnSubmit;
    private EditText etEmail;
    private TextInputLayout tiEmail;

    private ProgressBar mProgressBar;

    private ResetPresenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide Status Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_reset_request);
        
        intView();

        mPresenter = new ResetPresenter(this);
    }

    private void intView() {

        tiEmail = findViewById(R.id.ti_email);
        etEmail = findViewById(R.id.email);
        btnSubmit = findViewById(R.id.submit);

        mProgressBar = findViewById(R.id.progressBar);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();

                boolean valid =mPresenter.validate(email);

                if(!valid){
                    return;
                }

                mPresenter.sendResetRequest(email);


            }
        });

    }


    @Override
    public void clearPreErrors() {
        tiEmail.setErrorEnabled(false);
    }

    @Override
    public void hideDialog() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showDialog() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMessageDialog() {
        hideDialog();

        String titleText ="Password Reset Request";

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
        alertDialog.setMessage("Password Reset Email has been sent!!! \nPlease Check Your Email....");
        Drawable drawable = ContextCompat.getDrawable(this,R.drawable.info);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable.setTint(getResources().getColor(R.color.indicator_4));
        }
        alertDialog.setIcon(drawable);

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.indicator_4));
            }
        });



        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ResetRequestActivity.this.finish();
            }
        });

        alertDialog.show();

    }

    @Override
    public void showErrorMessage(int fieldId, String message) {
        tiEmail.setError(message);
    }
}
