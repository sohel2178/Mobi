package com.mobitrack.mobi.ui.newExpenses.transactionSummery.headwise;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.mobitrack.mobi.R;
import com.mobitrack.mobi.model.Vehicle;
import com.mobitrack.mobi.ui.newExpenses.BaseFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class HeadWiseFragment extends BaseFragment implements HeadContract.View{

    private RecyclerView mRecyclerView;

    private HeadPresenter mPresenter;
    private HeadAdapter adapter;

    private AppCompatSpinner spVehicle;

    private ArrayAdapter<Vehicle> vehicleAdapter;


    public HeadWiseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPresenter  = new HeadPresenter(this);
        this.adapter = new HeadAdapter(this);
        this.vehicleAdapter = new ArrayAdapter<Vehicle>(getContext(),android.R.layout.simple_list_item_1);

        Vehicle vehicle = new Vehicle();
        vehicle.setId("");
        vehicle.setModel("All Vehicle");
        vehicleAdapter.add(vehicle);
        vehicleAdapter.addAll(getVehicleList());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_head_wise, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(adapter);

        spVehicle = view.findViewById(R.id.sp_vehicle);
        spVehicle.setAdapter(vehicleAdapter);

        spVehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(getTransactions()!=null){
                    mPresenter.filterData(getTransactions(),(Vehicle) (spVehicle.getSelectedItem()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public void initData(){
        mPresenter.processData(getTransactions());
        spVehicle.setSelection(0);
    }

    @Override
    public void clearAdapter() {
        adapter.clearAdapter();
    }

    @Override
    public void addItem(MyHead myHead) {
        adapter.addItem(myHead);
        runLayoutAnimation(mRecyclerView);
    }


    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}
