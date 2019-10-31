package com.mobitrack.mobi.ui.sharedUser;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.mobitrack.mobi.R;
import com.mobitrack.mobi.activities.PrebaseActivity;
import com.mobitrack.mobi.model.SharedUser;
import com.mobitrack.mobi.utility.Constant;

import java.util.List;

public class SharedUserActivity extends PrebaseActivity implements SharedUserContract.View, com.mobitrack.mobi.ui.sharedUser.RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private SharedUserPresenter mPresenter;
    
    private String vehicleId;

    private SharedUserAdapter adapter;

    private RecyclerView rvSharedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_user);
        mPresenter = new SharedUserPresenter(this);

        vehicleId = getIntent().getStringExtra(Constant.VEHICLE_ID);

        adapter = new SharedUserAdapter(getApplicationContext(),vehicleId);


        setupToolbar();
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Shared User List");

        initView();

        mPresenter.requestForAllUsers(vehicleId);
    }

    private void initView() {
        rvSharedUser = findViewById(R.id.rv_shared_user);


        rvSharedUser.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvSharedUser.setItemAnimator(new DefaultItemAnimator());
        rvSharedUser.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        rvSharedUser.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvSharedUser);

    }

    @Override
    public void addSharedUser(SharedUser sharedUser) {
        adapter.addSharedUser(sharedUser);
    }

    @Override
    public void userDeleted(int position) {
        adapter.removeItem(position);


        Snackbar snackbar = Snackbar
                .make(rvSharedUser, "Deleted Successfully", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // undo is selected, restore the deleted item
                // adapter.restoreItem(fe, deletedIndex);
            }
        });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        SharedUser sharedUser = adapter.getSharedUser(position);
        mPresenter.deleteSharedUser(sharedUser,vehicleId,position);

    }
}
