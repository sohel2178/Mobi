package com.mobitrack.mobi.ui.newExpenses.helper.head;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mobitrack.mobi.R;
import com.mobitrack.mobi.model.Head;


/**
 * A simple {@link Fragment} subclass.
 */
public class HeadDialogFragment extends DialogFragment implements View.OnClickListener,HeadContract.View {

    private HeadListener listener;

    private TextView tvCancel,tvCreate;
    private EditText etHead;
    private TextInputLayout tHead;
    private ProgressBar mProgressBar;

    private HeadPresenter mPresenter;




    public HeadDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mPresenter = new HeadPresenter(this);
        this.listener = (HeadListener) getParentFragment();



    }


   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_head_dialog, container, false);
        initView(view);
        return view;
    }*/

    private void initView(View view) {
        tHead = view.findViewById(R.id.ti_name);
        etHead = view.findViewById(R.id.head);
        tvCancel = view.findViewById(R.id.cancel);
        tvCreate = view.findViewById(R.id.create);

        mProgressBar = view.findViewById(R.id.progressBar);

        tvCancel.setOnClickListener(this);
        tvCreate.setOnClickListener(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_head_dialog, null);
        initView(view);


        AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.MyDialog).create();

        //AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.ThemeOverlay_AppCompat_Dialog);
        alertDialog.setView(view);
        return alertDialog;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel:
                mPresenter.onCancelClick();
                break;

            case R.id.create:
                String head = etHead.getText().toString().trim();
                Head h = new Head();
                h.setName(head);

                boolean valid = mPresenter.validate(h);

                if(!valid){
                    return;
                }

                mPresenter.saveHead(h);
                break;
        }
    }

    @Override
    public void dismissDialog() {
        this.dismiss();
    }

    @Override
    public void clearPreError() {
        tHead.setErrorEnabled(false);
    }

    @Override
    public void showProgressBar() {
        tvCreate.setEnabled(false);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void setHead(Head head) {

        listener.onHeadInserted(head);

       /* if(getActivity() instanceof NewExpensesActivity){
            NewExpensesActivity activity = (NewExpensesActivity) getActivity();
            activity.addHead(head);
        }*/

        dismiss();
    }

    @Override
    public void showError(String message, int field) {
        switch (field){
            case 1:
                etHead.requestFocus();
                tHead.setError(message);
                break;
        }
    }
}
