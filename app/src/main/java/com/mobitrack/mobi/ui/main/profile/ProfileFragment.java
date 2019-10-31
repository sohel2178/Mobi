package com.mobitrack.mobi.ui.main.profile;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobitrack.mobi.R;
import com.mobitrack.mobi.model.User;
import com.mobitrack.mobi.ui.main.MainActivity;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment implements View.OnClickListener,ProfileContract.View {

    private EditText etName,etPhone,etAddress,etCompanyName;
    private TextInputLayout tiName,tiPhone,tiAddress,tiCompanyName;

    private Button btnBrowse,btnUpdate;
    private CircleImageView ivProfile;

    private ProfilePresenter mPresenter;

    private boolean isImageSelect;
    private User user;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ProfilePresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(view);
        mPresenter.requestForCurrentUser();
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

        btnBrowse = view.findViewById(R.id.btn_select);
        btnUpdate = view.findViewById(R.id.btn_upload);

        ivProfile = view.findViewById(R.id.iv_profile);

        btnBrowse.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(getActivity() instanceof MainActivity){
            MainActivity ma = (MainActivity) getActivity();
            ma.setTitle(getString(R.string.profile));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_select:
                mPresenter.browseClick();
                break;

            case R.id.btn_upload:
                String name = etName.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String address = etAddress.getText().toString().trim();
                String company = etCompanyName.getText().toString().trim();

                if(user!=null){
                    user.setName(name);
                    user.setPhone(phone);
                    user.setAddress(address);
                    user.setCompanyName(company);

                    boolean valid = mPresenter.validate(user,isImageSelect);

                    if(!valid){
                        return;
                    }

                    if(isImageSelect){
                        Bitmap bitmap;

                        byte[] byteArray=null;

                        try {
                            bitmap = ((BitmapDrawable)ivProfile.getDrawable()).getBitmap();
                        }catch (Exception e){
                            bitmap=null;
                        }


                        if(bitmap!=null){
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byteArray = baos.toByteArray();

                            mPresenter.saveUserWithImage(user,byteArray);
                        }

                    }else{
                        mPresenter.saveUser(user);
                    }


                }





                break;
        }
    }

    @Override
    public void updateUI(User user) {
        this.user = user;
        etName.setText(user.getName());
        etPhone.setText(user.getPhone());
        etAddress.setText(user.getAddress());
        etCompanyName.setText(user.getCompanyName());

        if(user.getPhotoUri() !=null && !user.getPhotoUri().equals("")){
            Picasso.with(getContext())
                    .load(user.getPhotoUri())
                    .into(ivProfile);
        }
    }

    @Override
    public void openCropImageActivity() {
        if(getActivity() instanceof MainActivity){
            MainActivity ma = (MainActivity) getActivity();
            ma.openCropImageActivity(true);
        }
    }

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

    @Override
    public void showDialog() {
        if(getActivity() instanceof MainActivity){
            MainActivity ma = (MainActivity) getActivity();
            ma.showProgressDialog();
        }
    }

    @Override
    public void hideDialog() {
        if(getActivity() instanceof MainActivity){
            MainActivity ma = (MainActivity) getActivity();
            ma.hideProgressDialog();
        }
    }

    @Override
    public void complete() {
        if(getActivity() instanceof MainActivity){
            MainActivity ma = (MainActivity) getActivity();
            //ma.hideProgressDialog();
            ma.recreate();
        }
    }

    public void setBitmap(Bitmap bitmap){
        isImageSelect=true;
        ivProfile.setImageBitmap(bitmap);
    }
}
