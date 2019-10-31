package com.mobitrack.mobi.fragments.reportNav;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mobitrack.mobi.R;
import com.mobitrack.mobi.api.model.RData;
import com.mobitrack.mobi.ui.report.ReportActivity;
import com.mobitrack.mobi.activities.TripAnimActivity;
import com.mobitrack.mobi.adapter.StatusAdapter;
import com.mobitrack.mobi.listener.VehicleStatusListener;
import com.mobitrack.mobi.model.FData;
import com.mobitrack.mobi.model.VehicleStatus;
import com.mobitrack.mobi.singleton.RawFData;
import com.mobitrack.mobi.utility.Constant;
import com.mobitrack.mobi.utility.MyUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatusFragment extends Fragment implements VehicleStatusListener {

    private RecyclerView rvStatus;


    private StatusAdapter adapter;

    private int vehicleType;



    public StatusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getActivity() instanceof ReportActivity){
            ReportActivity ra = (ReportActivity) getActivity();
            vehicleType = ra.getVehicleType();
        }

        adapter = new StatusAdapter(getContext(),vehicleType,this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        rvStatus = view.findViewById(R.id.rv_status);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvStatus.setLayoutManager(manager);
        rvStatus.addItemDecoration(new DividerItemDecoration(getContext(),manager.getOrientation()));
        rvStatus.setAdapter(adapter);

    }

    @Override
    public void onStart() {
        super.onStart();

        update();
    }

    public void update() {

        Observable<List<VehicleStatus>> getStatusObs = Observable.fromCallable(new Callable<List<VehicleStatus>>() {
            @Override
            public List<VehicleStatus> call() throws Exception {
                return getVehicleStatusList(RawFData.getInstance().getData());
            }
        });

        getStatusObs.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<VehicleStatus>>() {
                    @Override
                    public void call(List<VehicleStatus> vehicleStatuses) {
                        adapter.clear();

                        for (VehicleStatus x:vehicleStatuses){
                            adapter.addVehicleStatus(x);
                        }
                    }
                });
    }

    private List<VehicleStatus> getVehicleStatusList(List<RData> rDataList){

        RData initialRData = new RData();
        initialRData.setServerTime(MyUtil.getBeginingTime(rDataList.get(0).getServerTime()));
        initialRData.setStatus("0");

        //Log.d("KKKK",MyUtil.getStringDate3(initialFData.getServertime()));

        List<VehicleStatus> vehicleStatusList = new ArrayList<>();

        for(int i=0;i<rDataList.size();i++){

            RData rData = rDataList.get(i);

            if(!rData.getStatus().equals(initialRData.getStatus())){
                VehicleStatus vehicleStatus = new VehicleStatus();
                vehicleStatus.setStartTime(initialRData.getServerTime());
                vehicleStatus.setEndTime(rData.getServerTime());
                vehicleStatus.setStatus(initialRData.getStatus());

                vehicleStatusList.add(vehicleStatus);

                initialRData = rData;
            }



        }

        return vehicleStatusList;
    }

    @Override
    public void onStatusClick(final VehicleStatus status) {
        if(status.getStatus().equals("1")){
//
//
            int time = (int) ((status.getEndTime()-status.getStartTime())/1000/60); // Time in Min

            if(time>=3){

                Observable<List<RData>> fDataObs = Observable.fromCallable(new Callable<List<RData>>() {
                    @Override
                    public List<RData> call() throws Exception {
                        return getFData(status);
                    }
                });

                fDataObs.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<List<RData>>() {
                            @Override
                            public void call(List<RData> fDataList) {
                                startTripAnimActivity(fDataList);
                            }
                        });

                //List<FData> fDataList = getFData(status);

               // Log.d("JJJJ",fDataList.size()+"");


            }

        }
    }

    public void startTripAnimActivity(List<RData> rDataList){
        int vehicleType = 1;
        if(getActivity() instanceof ReportActivity){
            ReportActivity ra = (ReportActivity) getActivity();
            vehicleType= ra.getVehicleType();

            Log.d("HHHHH",vehicleType+"");
        }
        if(rDataList.size()>10){
            Intent intent = new Intent(getContext(),TripAnimActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.DATA, (Serializable) rDataList);
            bundle.putInt(Constant.V_TYPE,vehicleType);
            intent.putExtras(bundle);
            startActivity(intent);
        }else{
            Toast.makeText(getContext(), "No Data Found to Animate Car", Toast.LENGTH_SHORT).show();
        }
    }

    private List<RData> getFData(VehicleStatus status){

        List<RData> rDataList = new ArrayList<>();

        for (RData x:RawFData.getInstance().getData()){
            if(x.getServerTime()>=status.getStartTime() && x.getServerTime()<=status.getEndTime()){
                rDataList.add(x);
            }
        }

        return rDataList;

    }

    private void showDialog(){
        if(getActivity() instanceof ReportActivity){
            ReportActivity activity = (ReportActivity) getActivity();
            activity.showProgressDialog();
        }
    }

    private void hideDialog(){
        if(getActivity() instanceof ReportActivity){
            ReportActivity activity = (ReportActivity) getActivity();
            activity.hideProgressDialog();
        }
    }

}
