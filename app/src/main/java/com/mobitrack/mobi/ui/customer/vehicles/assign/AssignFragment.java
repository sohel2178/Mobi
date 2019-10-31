package com.mobitrack.mobi.ui.customer.vehicles.assign;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import com.mobitrack.mobi.R;
import com.mobitrack.mobi.model.User;
import com.mobitrack.mobi.model.Vehicle;
import com.mobitrack.mobi.ui.customer.CustomerActivity;


public class AssignFragment extends DialogFragment implements AssignContract.View {

    private RecyclerView mRecyclerView;
    private User user;

    private AssignAdapter adapter;
    private AssignPresenter mPresenter;





    public AssignFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        adapter = new AssignAdapter(this);

        if(getActivity() instanceof CustomerActivity){
            CustomerActivity ca = (CustomerActivity) getActivity();
            user = ca.getUser();
            mPresenter = new AssignPresenter(this,user);
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_assign, null);
        initView(view);


        AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.MyDialog).create();

        //AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.ThemeOverlay_AppCompat_Dialog);
        alertDialog.setView(view);
        return alertDialog;
    }

    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);

        mPresenter.requestForVehicles();
    }

    @Override
    public void hideSelf() {
        dismiss();
    }

    @Override
    public void addVehicle(Vehicle vehicle) {
        adapter.add(vehicle);
    }

    @Override
    public void updateVehicle(Vehicle vehicle) {
        adapter.update(vehicle);
    }

    @Override
    public void removeVehicle(Vehicle vehicle) {
        adapter.remove(vehicle);
    }

    @Override
    public void assignDevice(Vehicle vehicle) {
        mPresenter.assignDevice(vehicle);
    }
}
