package com.mobitrack.mobi.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.UploadTask;
import com.mobitrack.mobi.ui.main.MainActivity;
import com.mobitrack.mobi.R;
import com.mobitrack.mobi.customView.MyEditText;
import com.mobitrack.mobi.singleton.MyDatabaseRef;
import com.mobitrack.mobi.singleton.MyStorageRef;
import com.mobitrack.mobi.utility.UserLocalStore;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class InfoActivity extends PrebaseActivity implements View.OnClickListener {
    private static final int CAMERA_PERM =2000;

    private MyEditText etName,etPhone,etAddress,etCompanyName;

    private CircleImageView mProfileImage;

    private Button btnSelect,btnSubmit;

    private byte[] byteArray;

    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        setupToolbar();
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Basic Info");

        initView();

        //setupSlideAnimations();
    }

    private void initView() {
        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        etAddress = findViewById(R.id.et_address);
        etCompanyName = findViewById(R.id.et_company_name);

        mProfileImage = findViewById(R.id.iv_profile);
        btnSelect = findViewById(R.id.btn_select);
        btnSubmit = findViewById(R.id.btn_upload);

        btnSelect.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_select:
                getCameraPermission();
                break;

            case R.id.btn_upload:
                final String name = etName.getText().toString().trim();
                final String phone = etPhone.getText().toString().trim();
                final String address = etAddress.getText().toString().trim();
                final String companyName = etCompanyName.getText().toString().trim();

                if(TextUtils.isEmpty(name)){
                    etName.requestFocus();
                    Toast.makeText(this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(phone)){
                    etPhone.requestFocus();
                    Toast.makeText(this, "Phone Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(address)){
                    etAddress.requestFocus();
                    Toast.makeText(this, "Address Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(companyName)){
                    etCompanyName.requestFocus();
                    Toast.makeText(this, "Company Name Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(byteArray==null){
                    Toast.makeText(this, "Please Select Profile Image Before Upload", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(mUser!=null){

                    showProgressDialog();

                    MyStorageRef.getInstance().getUserStoreRef().child(mUser.getUid()).putBytes(byteArray)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Get a URL to the uploaded content
                                    String url = taskSnapshot.getDownloadUrl().toString();

                                    Map<String,Object> userMap = new HashMap<>();
                                    userMap.put("name",name);
                                    userMap.put("phone",phone);
                                    userMap.put("address",address);
                                    userMap.put("companyName",companyName);
                                    userMap.put("photoUri",url);

                                    updateDatabase(userMap);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                    // ...
                                }
                            });


                }





                break;
        }

    }

    private void updateDatabase(Map<String, Object> userMap) {
        MyDatabaseRef.getInstance().getUserRef()
                .child(mUser.getUid()).updateChildren(userMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                hideProgressDialog();
                finish();
                transitActivity(new Intent(getApplicationContext(), MainActivity.class));

                UserLocalStore.getInstance(getApplicationContext()).setIsUserSync(true);
            }
        });
    }


    @AfterPermissionGranted(CAMERA_PERM)
    private void getCameraPermission(){
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...

            openCropImageActivity(true);
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "All Permission",
                    CAMERA_PERM, perms);
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
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), result.getUri());
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,200,200,false);

                    mProfileImage.setImageBitmap(scaledBitmap);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    scaledBitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);
                    byteArray = stream.toByteArray();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
