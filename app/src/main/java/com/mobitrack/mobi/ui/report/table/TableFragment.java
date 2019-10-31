package com.mobitrack.mobi.ui.report.table;


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

import com.mobitrack.mobi.R;
import com.mobitrack.mobi.adapter.DAdapter;
import com.mobitrack.mobi.api.model.RData;
import com.mobitrack.mobi.listener.FragmentListner;
import com.mobitrack.mobi.model.FData;
import com.mobitrack.mobi.model.Span;
import com.mobitrack.mobi.singleton.RawFData;
import com.mobitrack.mobi.ui.report.ReportActivity;
import com.mobitrack.mobi.utility.Constant;
import com.mobitrack.mobi.utility.MyUtil;

import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class TableFragment extends Fragment {

    private RecyclerView rvDistance;

    private DAdapter adapter;

    private FragmentListner listner;

    public TableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getActivity() instanceof ReportActivity){
            ReportActivity reportActivity = (ReportActivity) getActivity();
            int vType = reportActivity.getVehicleType();

            adapter = new DAdapter(getContext(),vType);
        }


    }


    @Override
    public void onResume() {
        super.onResume();

       /* listner = (FragmentListner) getActivity();
        listner.activeFragment(0);*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_table, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        rvDistance = view.findViewById(R.id.rv_travel_distance);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvDistance.setLayoutManager(manager);
        rvDistance.addItemDecoration(new DividerItemDecoration(getContext(),manager.getOrientation()));
        rvDistance.setAdapter(adapter);
    }

    public DAdapter getAdapter() {
        return adapter;
    }


    private List<Span> getSpanList(List<RData> rDataList){
        List<Span> spanList = MyUtil.getSpanList();

        for (int i=0;i<rDataList.size();i++){
            spanList.get(rDataList.get(i).getSpanNo()).addRData(rDataList.get(i));
        }

        return spanList;
    }


    @Override
    public void onStart() {
        super.onStart();
        update();
    }


    public void update(){
        Observable<List<Span>> getSpansObs = Observable.fromCallable(new Callable<List<Span>>() {
            @Override
            public List<Span> call() throws Exception {
                return getSpanList(RawFData.getInstance().getData());
            }
        });

        getSpansObs.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Span>>() {
                    @Override
                    public void call(List<Span> spans) {
                        adapter.clear();

                        for(Span x: spans){
                            adapter.addSpan(x);
                        }
                    }
                });
    }
}
