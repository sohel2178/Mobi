package com.mobitrack.mobi.ui.newExpenses.transactionSummery.deviceWise;


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
import com.mobitrack.mobi.model.Head;
import com.mobitrack.mobi.ui.newExpenses.BaseFragment;
import com.mobitrack.mobi.ui.newExpenses.transactionSummery.headwise.HeadAdapter;
import com.mobitrack.mobi.ui.newExpenses.transactionSummery.headwise.MyHead;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceWiseFragment extends BaseFragment implements DeviceContract.View{

    private RecyclerView mRecyclerView;

    private DevicePresenter mPresenter;
    private HeadAdapter adapter;

    private AppCompatSpinner spHead;

    private ArrayAdapter<Head> headAdapter;



    public DeviceWiseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPresenter  = new DevicePresenter(this);
        mPresenter.setVehicleList(getVehicleList());

        this.adapter = new HeadAdapter(this);
        this.headAdapter = new ArrayAdapter<Head>(getContext(),android.R.layout.simple_list_item_1);

        Head head = new Head();
        head.set_id("");
        head.setName("All Heads");
        headAdapter.add(head);
        headAdapter.addAll(getHeadList());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device_wise, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(adapter);

        spHead = view.findViewById(R.id.sp_head);
        spHead.setAdapter(headAdapter);

        spHead.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(getTransactions()!=null){
                    mPresenter.filterData(getTransactions(),(Head) (spHead.getSelectedItem()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void initData(){
        mPresenter.processData(getTransactions());
        spHead.setSelection(0);
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
