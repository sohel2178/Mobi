package com.mobitrack.mobi.ui.newExpenses.transactionSummery.monthly;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.mobitrack.mobi.R;
import com.mobitrack.mobi.model.Tran;
import com.mobitrack.mobi.ui.newExpenses.BaseFragment;
import com.mobitrack.mobi.utility.MyUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonthlyFragment extends BaseFragment implements View.OnClickListener,MonthlyContract.View {

    private TextView tvMonth,tvNext,tvPrev;
    private RecyclerView mRecyclerView;


    private int currentMonth,currentYear;

    private MonthlyPresenter mPresenter;

    private MonAdapter adapter;

    public MonthlyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mPresenter = new MonthlyPresenter(this);

        this.currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        this.currentYear = Calendar.getInstance().get(Calendar.YEAR);

        this.adapter= new MonAdapter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monthly, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tvMonth = view.findViewById(R.id.month);
        tvNext = view.findViewById(R.id.next);
        tvPrev = view.findViewById(R.id.prev);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(adapter);

        tvNext.setOnClickListener(this);
        tvPrev.setOnClickListener(this);

        mPresenter.updateMonthText();
        mPresenter.processData(getTransactions(),getStartDate(),getEndDate());

    }

    public void initData(){
        mPresenter.processData(getTransactions(),getStartDate(),getEndDate());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.next:
                increase();
                mPresenter.updateMonthText();
                mPresenter.processData(getTransactions(),getStartDate(),getEndDate());
                break;

            case R.id.prev:
                decrease();
                mPresenter.updateMonthText();
                mPresenter.processData(getTransactions(),getStartDate(),getEndDate());
                break;
        }
    }

    private void increase(){
        currentMonth++;
        if(currentMonth>11){
            currentYear++;
            currentMonth= currentMonth%12;
        }
    }

    private void decrease(){
        currentMonth--;
        if(currentMonth<0){
            currentYear--;
            currentMonth= currentMonth+12;
        }
    }

    private Date getStartDate(){
       Calendar cal = new GregorianCalendar(currentYear,currentMonth,1);
       return  cal.getTime();
    }

    private Date getEndDate(){
        Calendar cal = new GregorianCalendar(currentYear,currentMonth,1);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(currentYear,currentMonth,maxDay);
        return  cal.getTime();
    }

    @Override
    public void updateMonth() {
        tvMonth.setText(MyUtil.getMonthYear(getStartDate()));
    }

    @Override
    public void clearAdapter() {
        adapter.clear();
    }

    @Override
    public void addItem(Tran tran) {
        adapter.addTransaction(tran);
    }
}
