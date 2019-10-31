package com.mobitrack.mobi.ui.editVehicle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.mobitrack.mobi.R;
import com.mobitrack.mobi.activities.PrebaseActivity;
import com.mobitrack.mobi.model.Vehicle;
import com.mobitrack.mobi.utility.Constant;
import com.mobitrack.mobi.utility.MyUtil;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import pub.devrel.easypermissions.EasyPermissions;

public class EditVehicleActivity extends PrebaseActivity implements View.OnClickListener,EditVehicleContract.View {
    private static final int CAMERA_PERM =2000;

    private Vehicle vehicle;

    private EditText etDriverName,etDriverPhone,etModel,etMilage,etCongestionConsumption;
    private TextInputLayout tiDriverName,tiDriverPhone,tiModel,tiMilage,tiCongestionConsumption;
    private ImageView ivDriverImage;
    private Button btnSelect,btnUpdate;

    private byte[] byteArray;

    private EditVehiclePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vehicle);

        mPresenter = new EditVehiclePresenter(this);

        setupToolbar();
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Update Vehicle");

        vehicle = (Vehicle) getIntent().getSerializableExtra(Constant.VEHICLE);

        initView();
    }

    private void initView() {
        etDriverName = findViewById(R.id.driver_name);
        etDriverPhone = findViewById(R.id.driver_phone);
        etModel = findViewById(R.id.model);
        etMilage = findViewById(R.id.mileage);
        etCongestionConsumption = findViewById(R.id.congestion_consumption);

        tiDriverName = findViewById(R.id.ti_driver_name);
        tiDriverPhone = findViewById(R.id.ti_driver_phone);
        tiModel = findViewById(R.id.ti_model);
        tiMilage = findViewById(R.id.ti_milage);
        tiCongestionConsumption = findViewById(R.id.ti_congestion_consumption);

        ivDriverImage = findViewById(R.id.driver_image);

        btnSelect = findViewById(R.id.select);
        btnUpdate = findViewById(R.id.update);

        btnSelect.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);

        if(vehicle.getDriver_name()!=null){
            etDriverName.setText(vehicle.getDriver_name());
        }

        if(vehicle.getDriver_phone()!=null){
            etDriverPhone.setText(vehicle.getDriver_phone());
        }

        if(vehicle.getDriver_photo()!=null && !vehicle.getDriver_photo().equals("")){
            Picasso.with(getApplicationContext())
                    .load(vehicle.getDriver_photo())
                    .into(ivDriverImage);
        }

        if(vehicle.getModel()!=null){
            etModel.setText(vehicle.getModel());
        }

        etMilage.setText(MyUtil.getTwoDecimalFormat(vehicle.getMileage()));
        etCongestionConsumption.setText(MyUtil.getTwoDecimalFormat(vehicle.getCongestion_consumption()));


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.select:
                mPresenter.cropImageRequest();
                //getCameraPermission();
                break;

            case R.id.update:

                String driverName = etDriverName.getText().toString().trim();
                String driverPhone = etDriverPhone.getText().toString().trim();
                String model = etModel.getText().toString().trim();
                String mileageStr = etMilage.getText().toString().trim();
                String conConsump = etCongestionConsumption.getText().toString().trim();

                vehicle.setDriver_name(driverName);
                vehicle.setDriver_phone(driverPhone);
                vehicle.setModel(model);

                if(!mileageStr.equals("")){
                    vehicle.setMileage(Double.parseDouble(mileageStr));
                }

                if(!conConsump.equals("")){
                    vehicle.setCongestion_consumption(Double.parseDouble(conConsump));
                }


                boolean valid = mPresenter.validate(vehicle);

                if(!valid){
                    return;
                }

                if(byteArray==null){
                    mPresenter.saveVehicle(vehicle);
                }else{
                    mPresenter.saveVehicleWithImage(vehicle,byteArray);
                }
                break;
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
//                imageUri = result.getUri();
//
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), result.getUri());

                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,200,200,false);

                    ivDriverImage.setImageBitmap(scaledBitmap);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    scaledBitmap.compress(Bitmap.CompressFormat.PNG, 60, stream);
                    byteArray = stream.toByteArray();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                /*if(imageUri!=null){
                    ivDriverImage.setImageURI(imageUri);
                }*/
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public void clearError() {
        tiDriverName.setErrorEnabled(false);
        tiDriverPhone.setErrorEnabled(false);
        tiModel.setErrorEnabled(false);
        tiMilage.setErrorEnabled(false);
        tiCongestionConsumption.setErrorEnabled(false);
    }

    @Override
    public void showError(int fieldId, String message) {
        switch (fieldId){
            case 1:
                tiDriverName.setError(message);
                break;

            case 2:
                tiDriverPhone.setError(message);
                break;

            case 3:
                tiModel.setError(message);
                break;

            case 4:
                tiMilage.setError(message);
                break;

            case 5:
                tiCongestionConsumption.setError(message);
                break;
        }
    }

    @Override
    public void openCropImageActivity() {
        openCropImageActivity(true);
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
