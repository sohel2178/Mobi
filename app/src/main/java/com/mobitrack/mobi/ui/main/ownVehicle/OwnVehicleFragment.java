package com.mobitrack.mobi.ui.main.ownVehicle;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mobitrack.mobi.R;
import com.mobitrack.mobi.model.Vehicle;
import com.mobitrack.mobi.ui.editVehicle.EditVehicleActivity;
import com.mobitrack.mobi.ui.map.MapActivity;
import com.mobitrack.mobi.ui.searchUser.SearchUserActivity;
import com.mobitrack.mobi.ui.sharedUser.SharedUserActivity;
import com.mobitrack.mobi.utility.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class OwnVehicleFragment extends Fragment implements OwnVehicleContract.View {

    private RecyclerView rvVehicle;
    private VehicleAdapter adapter;

    private OwnVehiclePresenter mPresenter;




    public OwnVehicleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new OwnVehiclePresenter(this);
        adapter = new VehicleAdapter(this);
    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_own_vehicle, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        rvVehicle = view.findViewById(R.id.rv_vehicle);
        rvVehicle.setNestedScrollingEnabled(false);
        rvVehicle.setLayoutManager(new GridLayoutManager(getContext(),2));
        //rvVehicle.setLayoutManager(new LinearLayoutManager(getContext()));
        rvVehicle.setAdapter(adapter);

        mPresenter.listenVehicleFromDatabase();

    }

    @Override
    public void onDestroy() {
        mPresenter.destroy();
        super.onDestroy();
    }

    @Override
    public void addToAdapter(Vehicle vehicle) {
        adapter.add(vehicle);
    }

    @Override
    public void updateVehicle(Vehicle vehicle) {
        adapter.updateVehicle(vehicle);
    }

    @Override
    public void removeVehicle(Vehicle vehicle) {
        adapter.removeVehicle(vehicle);
    }

    @Override
    public void startMapActivity(Vehicle vehicle) {
        Intent intent = new Intent(getContext(), MapActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.DEVICE,vehicle);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void startVehicleEditActivity(Vehicle vehicle) {
        Intent intent = new Intent(getContext(), EditVehicleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.VEHICLE,vehicle);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void startVehicleShareActivity(Vehicle vehicle) {
        Intent intent = new Intent(getContext(),SearchUserActivity.class);
        intent.putExtra(Constant.VEHICLE_ID,vehicle.getId());
        intent.putExtra(Constant.VEHICLE_NAME,vehicle.getDriver_name());

        startActivity(intent);
    }

    @Override
    public void startSharedUserActivity(Vehicle vehicle) {
        Intent intent = new Intent(getContext(),SharedUserActivity.class);
        intent.putExtra(Constant.VEHICLE_ID,vehicle.getId());
        startActivity(intent);

    }

    @Override
    public void callDriver(Vehicle vehicle) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", vehicle.getDriver_phone(), null));
        startActivity(intent);
    }
}
