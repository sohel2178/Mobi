package com.mobitrack.mobi.ui.monthlyReport.info;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mobitrack.mobi.R;
import com.mobitrack.mobi.ui.monthlyReport.MonthlyReportActivity;
import com.mobitrack.mobi.utility.UserLocalStore;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddressFragment extends DialogFragment implements View.OnClickListener {

    private TextView tvCancel,tvSave;
    private TextInputLayout tiOrganizationName,tiAddress;
    private EditText etOrganizationName,etAddress;


    public AddressFragment() {
        // Required empty public constructor
    }


    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_address, container, false);
    }*/

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_address, null);
        initView(view);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.MyDialog).create();

        alertDialog.setView(view);
        return alertDialog;
    }

    private void initView(View view) {
        tvCancel = view.findViewById(R.id.cancel);
        tvSave = view.findViewById(R.id.save);
        tiOrganizationName = view.findViewById(R.id.ti_organization_name);
        tiAddress = view.findViewById(R.id.ti_address);
        etOrganizationName = view.findViewById(R.id.et_organization_name);
        etAddress = view.findViewById(R.id.et_address);

        tvCancel.setOnClickListener(this);
        tvSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel:
                dismiss();
                break;

            case R.id.save:
                String organizationName = etOrganizationName.getText().toString().trim();
                String address = etAddress.getText().toString().trim();

                if(organizationName.equals("")){
                    clearPreError();
                    tiOrganizationName.setError("Organization Name Empty");
                    return;
                }

                if(address.equals("")){
                    clearPreError();
                    tiAddress.setError("Address Field Empty");
                    return;
                }

                UserLocalStore.getInstance(getContext()).setAddress(address);
                UserLocalStore.getInstance(getContext()).setOrganizationName(organizationName);

                if(getActivity() instanceof MonthlyReportActivity){
                    MonthlyReportActivity activity = (MonthlyReportActivity) getActivity();
                    activity.requestFile();
                }

                dismiss();


                break;
        }
    }

    private void clearPreError(){
        tiAddress.setErrorEnabled(false);
        tiOrganizationName.setErrorEnabled(false);
    }
}
