package com.mobitrack.mobi.ui.customer.vehicles.edit;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobitrack.mobi.R;
import com.mobitrack.mobi.model.Vehicle;
import com.mobitrack.mobi.utility.Constant;


public class EditVehicleFragment extends DialogFragment implements EditVehicleContract.View,View.OnClickListener {

    private EditVehiclePresenter mPresenter;

    private Vehicle vehicle;

    private EditText etDriverName,etDriverPhone,etModel,etMilage,etSimNum;
    private TextInputLayout tiDriverName,tiDriverPhone,tiModel,tiMilage,tiSimNum;
    private Button btnUpdate;

    private AppCompatSpinner spVehicleType;

    private ArrayAdapter<String> vehicleTypeAdapter;


    public EditVehicleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new EditVehiclePresenter(this);

        vehicle = (Vehicle) getArguments().getSerializable(Constant.VEHICLE);
        vehicleTypeAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.vehicle_type_array));
    }

    /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_vehicle, container, false);
    }*/

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_edit_vehicle, null);
        initView(view);


        AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.MyDialog).create();

        //AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.ThemeOverlay_AppCompat_Dialog);
        alertDialog.setView(view);
        return alertDialog;
    }

    private void initView(View view) {

        etDriverName = view.findViewById(R.id.driver_name);
        etDriverPhone = view.findViewById(R.id.driver_phone);
        etModel = view.findViewById(R.id.model);
        etMilage = view.findViewById(R.id.mileage);
        etSimNum = view.findViewById(R.id.device_sim);

        tiDriverName = view.findViewById(R.id.ti_driver_name);
        tiDriverPhone = view.findViewById(R.id.ti_driver_phone);
        tiModel = view.findViewById(R.id.ti_model);
        tiMilage = view.findViewById(R.id.ti_milage);
        tiSimNum = view.findViewById(R.id.ti_device_sim);

        spVehicleType = view.findViewById(R.id.sp_vehicle);
        spVehicleType.setAdapter(vehicleTypeAdapter);


        btnUpdate = view.findViewById(R.id.update);

        btnUpdate.setOnClickListener(this);

        mPresenter.populateView(vehicle);

    }

    @Override
    public void onClick(View view) {
        String driverName = etDriverName.getText().toString().trim();
        String driverPhone = etDriverPhone.getText().toString().trim();
        String model = etModel.getText().toString().trim();
        String mileageStr = etMilage.getText().toString().trim();
        String deviceSimNumber = etSimNum.getText().toString().trim();

        vehicle.setDriver_name(driverName);
        vehicle.setDriver_phone(driverPhone);
        vehicle.setModel(model);
        vehicle.setDevice_sim_number(deviceSimNumber);
        vehicle.setVehicle_type(spVehicleType.getSelectedItemPosition()+1);

        if(!mileageStr.equals("")){
            vehicle.setMileage(Double.parseDouble(mileageStr));
        }

        boolean valid = mPresenter.validate(vehicle);

        if(!valid){
            return;
        }

        mPresenter.updateVehicle(vehicle);

    }

    @Override
    public void populateView(Vehicle vehicle) {
        if(vehicle.getDriver_name()!=null){
            etDriverName.setText(vehicle.getDriver_name());
        }

        if(vehicle.getDriver_phone()!=null){
            etDriverPhone.setText(vehicle.getDriver_phone());
        }

        if(vehicle.getModel()!=null){
            etModel.setText(vehicle.getModel());
        }

        if(vehicle.getDevice_sim_number()!=null){
            etSimNum.setText(vehicle.getDevice_sim_number());
        }

        etMilage.setText(String.valueOf(vehicle.getMileage()));

        if(vehicle.getVehicle_type()==0){
            spVehicleType.setSelection(0);
        }else{
            spVehicleType.setSelection(vehicle.getVehicle_type()-1);
        }


    }

    @Override
    public void clearPreError() {
        tiDriverName.setErrorEnabled(false);
        tiDriverPhone.setErrorEnabled(false);
        tiModel.setErrorEnabled(false);
        tiMilage.setErrorEnabled(false);
        tiSimNum.setErrorEnabled(false);
    }

    @Override
    public void setError(String message, int field) {
        switch (field){
            case 1:
                etDriverName.requestFocus();
                tiDriverName.setError(message);
                break;

            case 2:
                etDriverPhone.requestFocus();
                tiDriverPhone.setError(message);
                break;

            case 3:
                etModel.requestFocus();
                tiModel.setError(message);
                break;

            case 4:
                etSimNum.requestFocus();
                tiSimNum.setError(message);
                break;
        }
    }

    @Override
    public void complete() {
        dismiss();
        Toast.makeText(getContext(), "Vehicle Updated", Toast.LENGTH_SHORT).show();
    }
}
