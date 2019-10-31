package com.mobitrack.mobi.ui.admin;

import android.os.Bundle;

import com.mobitrack.mobi.R;
import com.mobitrack.mobi.activities.PrebaseActivity;
import com.mobitrack.mobi.ui.admin.customers.CustomerFragment;

public class AdminActivity extends PrebaseActivity implements AdminContract.View {

    private AdminPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mPresenter = new AdminPresenter(this);

        setupToolbar();

        if(savedInstanceState==null){
            mPresenter.loadCustomerFragment();
        }
    }

    @Override
    public void loadCustomerFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new CustomerFragment())
                .commit();
    }

    public void setTitle(String title){
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
    }
}
