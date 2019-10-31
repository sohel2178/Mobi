package com.mobitrack.mobi.fragments.reportNav;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mobitrack.mobi.R;
import com.mobitrack.mobi.api.model.RData;
import com.mobitrack.mobi.model.FData;
import com.mobitrack.mobi.model.Span;
import com.mobitrack.mobi.singleton.FrequencyDistribution;
import com.mobitrack.mobi.singleton.RawFData;
import com.mobitrack.mobi.utility.Constant;
import com.mobitrack.mobi.utility.MyUtil;

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
public class ChartFragment extends Fragment {

    private BarChart barChart;



    public ChartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //spanList = FrequencyDistribution.getInstance().getSpanList();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        barChart = view.findViewById(R.id.bar_chart);
        //createChart();

    }

    private void createChart(List<Span> spanList){
        //barChart = new BarChart(getContext());

        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);
        barChart.setPinchZoom(true);

        barChart.setDrawGridBackground(false);
        barChart.getLegend().setEnabled(false);

        //List<String> xAxisLabels = new ArrayList<>();
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (Span x: spanList) {
            yVals1.add(new BarEntry(spanList.indexOf(x), (float) MyUtil.getDistanceFrom(x.getRDataList())));
           // xAxisLabels.add(String.valueOf(x.getSpanNo()));
        }

        //IAxisValueFormatter xAxisFormatter = new DateAxisFormatter(xAxisLabels);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(true);
        //xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
       //xAxis.setLabelCount(7);
        xAxis.setSpaceMax(5f);
        xAxis.setSpaceMin(2f);
        //xAxis.setValueFormatter(xAxisFormatter);

        YAxis leftAxis = barChart.getAxisLeft();
        //leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        //leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f);

        // Disable Right Axis
        barChart.getAxisRight().setEnabled(false);



        BarDataSet dataSet = new BarDataSet(yVals1,"Hourly Travel Distance");
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        dataSet.setDrawValues(false);
        dataSet.setBarBorderWidth(1f);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(dataSet);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        //data.setValueTypeface(mTfLight);
        data.setBarWidth(.9f);
        barChart.setData(data);
        barChart.animateXY(1000,1000);
        barChart.invalidate();

    }

    @Override
    public void onStart() {
        super.onStart();

        update();
    }

    private List<Span> getSpanList(List<RData> rDataList){
        List<Span> spanList = MyUtil.getSpanList();

        for (int i=0;i<rDataList.size();i++){
            //spanList.get(dataList.get(i).getSpanNo()).addFrequency();
            spanList.get(rDataList.get(i).getSpanNo()).addRData(rDataList.get(i));
        }

        return spanList;
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
                        createChart(spans);
                    }
                });
    }
}
