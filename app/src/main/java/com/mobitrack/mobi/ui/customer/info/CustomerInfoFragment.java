package com.mobitrack.mobi.ui.customer.info;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mobitrack.mobi.R;
import com.mobitrack.mobi.model.User;
import com.mobitrack.mobi.ui.customer.CustomerActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerInfoFragment extends Fragment implements View.OnClickListener,CustomerInfoContract.View {

    private EditText etName,etPhone,etAddress,etCompanyName;
    private TextInputLayout tiName,tiPhone,tiAddress,tiCompanyName;

    private RadioGroup rgActive;
    private RadioButton rbActive,rbInactive;

    private Button btnUpdate;

    private CustomerInfoPresenter mPresenter;

    private User user;


    public CustomerInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new CustomerInfoPresenter(this);

        if(getActivity() instanceof CustomerActivity){
            CustomerActivity ca = (CustomerActivity) getActivity();
            user = ca.getUser();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_info, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {

        tiName = view.findViewById(R.id.ti_name);
        tiPhone = view.findViewById(R.id.ti_phone);
        tiAddress = view.findViewById(R.id.ti_address);
        tiCompanyName = view.findViewById(R.id.ti_company);

        etName = view.findViewById(R.id.et_name);
        etPhone = view.findViewById(R.id.et_phone);
        etAddress = view.findViewById(R.id.et_address);
        etCompanyName = view.findViewById(R.id.et_company_name);

        rgActive = view.findViewById(R.id.radio_gr_active);
        rbActive = view.findViewById(R.id.radio_active);
        rbInactive = view.findViewById(R.id.radio_inactive);

        rgActive.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radio_active:
                        user.setIsActive(1);
                        Log.d("KKKK","active");
                        break;

                    case R.id.radio_inactive:
                        user.setIsActive(0);
                        Log.d("KKKK","Inactive");
                        break;
                }
            }
        });

        btnUpdate = view.findViewById(R.id.btn_upload);
        btnUpdate.setOnClickListener(this);

        if(user!=null){
            mPresenter.updateUI(user);
        }
    }

    @Override
    public void onClick(View view) {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String company = etCompanyName.getText().toString().trim();

        user.setName(name);
        user.setPhone(phone);
        user.setAddress(address);
        user.setCompanyName(company);

        boolean valid = mPresenter.validate(user);

        if(!valid){
            return;
        }

        mPresenter.updateUser(user);
    }

    @Override
    public void updateUI(User user) {
        etName.setText(user.getName());
        etPhone.setText(user.getPhone());
        etAddress.setText(user.getAddress());
        etCompanyName.setText(user.getCompanyName());

        if(user.getIsActive()==1){
            rbActive.setChecked(true);
        }else{
            rbInactive.setChecked(true);
        }
    }

    @Override
    public void showDialog() {
        if(getActivity() instanceof CustomerActivity){
            CustomerActivity ca = (CustomerActivity) getActivity();
            ca.showProgressDialog();
        }
    }

    @Override
    public void hideDialog() {
        if(getActivity() instanceof CustomerActivity){
            CustomerActivity ca = (CustomerActivity) getActivity();
            ca.hideProgressDialog();
        }
    }

    @Override
    public void complete() {
        hideDialog();
        Toast.makeText(getContext(), "Info Updated", Toast.LENGTH_SHORT).show();
    }

    /*private void updateUser(User user){
        if(getActivity() instanceof CustomerActivity){
            CustomerActivity ca = (CustomerActivity) getActivity();
            ca.updateUser(user);
        }
    }*/

    @Override
    public void showErrorMessage(int fieldId, String message) {
        switch (fieldId){
            case 1:
                tiName.setError(message);
                etName.requestFocus();
                break;

            case 2:
                tiPhone.setError(message);
                etPhone.requestFocus();
                break;

            case 3:
                tiAddress.setError(message);
                etAddress.requestFocus();
                break;

            case 4:
                tiCompanyName.setError(message);
                etCompanyName.requestFocus();
                break;

            case 5:
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void clearPreErrors() {
        tiName.setErrorEnabled(false);
        tiPhone.setErrorEnabled(false);
        tiAddress.setErrorEnabled(false);
        tiCompanyName.setErrorEnabled(false);
    }
}
