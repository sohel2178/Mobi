package com.mobitrack.mobi.ui.vehicleShare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.mobitrack.mobi.R;
import com.mobitrack.mobi.activities.PrebaseActivity;
import com.mobitrack.mobi.model.ShareVehicle;
import com.mobitrack.mobi.model.SharedUser;
import com.mobitrack.mobi.model.User;
import com.mobitrack.mobi.singleton.MyDatabaseRef;
import com.mobitrack.mobi.ui.searchUser.SearchUserActivity;
import com.mobitrack.mobi.utility.Constant;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class VehicleShareActivity extends PrebaseActivity implements View.OnClickListener,VehicleShareContract.View {
    private static final int REQUEST_CODE=5000;
    
    private String vehicleId;
    private ImageView ivSelect;
    private CircleImageView ivImage;
    private TextView tvName,tvCompanyName;
    private RelativeLayout rlUserContainer;
    private Button btnShare;
    private User selectedUser;

    private VehicleSharePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_share);
        mPresenter = new VehicleSharePresenter(this);

        setupToolbar();
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra(Constant.VEHICLE_NAME));

        vehicleId = getIntent().getStringExtra(Constant.VEHICLE_ID);
        initView();
    }

    private void initView() {

        ivImage = findViewById(R.id.image);
        tvName = findViewById(R.id.name);
        tvCompanyName = findViewById(R.id.tv_company_name);

        rlUserContainer = findViewById(R.id.rl_user_container);

        ivSelect = findViewById(R.id.iv_select);
        btnShare = findViewById(R.id.btn_share);
        ivSelect.setOnClickListener(this);
        btnShare.setOnClickListener(this);

    }


    private void updateUI() {
        if(selectedUser!=null){
            tvName.setText(selectedUser.getName());

            if(!selectedUser.getCompanyName().equals("")){
                tvCompanyName.setText(selectedUser.getCompanyName());
            }else{
                tvCompanyName.setText(R.string.undefined);
            }


            if(selectedUser.getPhotoUri() != null && !selectedUser.getPhotoUri().equals("")){
                Picasso.with(getApplicationContext())
                        .load(selectedUser.getPhotoUri())
                        .into(ivImage);
            }

            btnShare.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_select:
                mPresenter.selectClick();
                break;

            case R.id.btn_share:
                ShareVehicle shareVehicle = new ShareVehicle(1,vehicleId);
                mPresenter.shareClick(shareVehicle,selectedUser.getUid(),vehicleId);
                break;
        }
    }

    @Override
    public void showDialog() {
        showProgressDialog();
    }

    @Override
    public void startSearchUserActivityForResult() {
        Intent intent = new Intent(this, SearchUserActivity.class);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    public void complete() {
        hideProgressDialog();
        Toast.makeText(getApplicationContext(), "Vehicle Shared Successfully", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CODE && resultCode==RESULT_OK){
            selectedUser = (User) data.getSerializableExtra(Constant.USER);
            updateUI();
        }
    }
}
