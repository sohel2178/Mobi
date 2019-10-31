package com.mobitrack.mobi.ui.searchUser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;


import com.mobitrack.mobi.R;
import com.mobitrack.mobi.activities.PrebaseActivity;
import com.mobitrack.mobi.model.ShareVehicle;
import com.mobitrack.mobi.model.User;
import com.mobitrack.mobi.utility.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SearchUserActivity extends PrebaseActivity implements SearchUserContract.View{

    private AppCompatEditText etEmail;
    private RecyclerView rvUsers;

    private SearchUserPresenter mPresenter;
    private int before,after;


    private List<User> userList;
    private UserAdapter adapter;

    private String vehicleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        this.vehicleId = getIntent().getStringExtra(Constant.VEHICLE_ID);

        userList = new ArrayList<>();

        mPresenter = new SearchUserPresenter(this);

        adapter = new UserAdapter(this);

        initView();
    }

    private void initView() {
        etEmail = findViewById(R.id.et_email);
        rvUsers = findViewById(R.id.rv_user);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        rvUsers.setAdapter(adapter);

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                before = charSequence.toString().length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                after = charSequence.toString().length();
                String val = charSequence.toString();

                Log.d("UUUUU",before+" ");
                Log.d("UUUUU",after+" ");
                Log.d("UUUUU",val+" ");
                mPresenter.requestForData(val,before,after);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void updateData(User user, String val) {
        this.userList.add(user);
        updateAdapter(val);
    }

    @Override
    public void updateAdapter(String value) {
        if (value.equals("")){
            adapter.setData(userList);
        }else {
            List<User> tempList = new ArrayList<>();

            for (User x: userList){
                if(x.getEmail().toLowerCase().startsWith(value.toLowerCase())){
                    tempList.add(x);
                }
            }

            adapter.setData(tempList);
        }
    }

    @Override
    public void shearClick(User user) {
        ShareVehicle shareVehicle = new ShareVehicle(1,vehicleId);
        mPresenter.shareClick(shareVehicle,user.getUid(),vehicleId);
    }

    @Override
    public void shareDone(String uid) {
        hideProgressDialog();
        int position = adapter.getPosition(uid);
        UserAdapter.UserHolder holder = (UserAdapter.UserHolder) rvUsers.findViewHolderForAdapterPosition(position);
        holder.btnShare.setVisibility(View.GONE);
    }

    @Override
    public void showDialog() {
        showProgressDialog();
    }
}
