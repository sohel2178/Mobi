package com.mobitrack.mobi.ui.customer.vehicles;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.firebase.database.Query;
import com.mobitrack.mobi.R;
import com.mobitrack.mobi.model.User;
import com.mobitrack.mobi.model.Vehicle;
import com.mobitrack.mobi.ui.customer.CustomerActivity;
import com.mobitrack.mobi.ui.customer.vehicles.assign.AssignFragment;
import com.mobitrack.mobi.ui.customer.vehicles.edit.EditVehicleFragment;
import com.mobitrack.mobi.ui.map.MapActivity;
import com.mobitrack.mobi.utility.Constant;
import com.mobitrack.mobi.viewholder.AdminVehicleHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class VehiclesFragment extends Fragment implements VehicleContract.View,View.OnClickListener {


    private VehiclePresenter mPresenter;

    private Button btnAssign;
    private RecyclerView mRecyclerView;

    FirebaseRecyclerAdapter<Vehicle,AdminVehicleHolder> adapter;


    private User user;


    public VehiclesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getActivity() instanceof CustomerActivity){
            CustomerActivity ca = (CustomerActivity) getActivity();
            user = ca.getUser();
        }
        mPresenter = new VehiclePresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vehicles, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {
        btnAssign = view.findViewById(R.id.assign);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btnAssign.setOnClickListener(this);

        mPresenter.getQuery(user);
    }

    @Override
    public void updateAdapter(Query query) {
        adapter = new FirebaseRecyclerAdapter<Vehicle, AdminVehicleHolder>(Vehicle.class, R.layout.item_vehicle, AdminVehicleHolder.class,
                query) {
            @Override
            protected void populateViewHolder(AdminVehicleHolder viewHolder, final Vehicle model, int position) {
                viewHolder.bind(model);

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), MapActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constant.DEVICE,model);
                        intent.putExtras(bundle);

                        startActivity(intent);
                    }
                });

                viewHolder.getEdit().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditVehicleFragment editVehicleFragment = new EditVehicleFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constant.VEHICLE,model);
                        editVehicleFragment.setArguments(bundle);
                        editVehicleFragment.show(getChildFragmentManager(),"LLL");

                    }
                });

                viewHolder.getUnAssign().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPresenter.unassign(model);
                    }
                });

            }
        };
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showAssignDialog() {
        AssignFragment assignFragment = new AssignFragment();
        assignFragment.show(getChildFragmentManager(),"KKKK");
    }

    @Override
    public void complete() {
        Toast.makeText(getContext(), "Un-assign Complete", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        mPresenter.showAssignDialog();
    }
}
