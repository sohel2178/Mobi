package com.mobitrack.mobi.ui.newExpenses.transactionSummery;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.mobitrack.mobi.R;
import com.mobitrack.mobi.ui.newExpenses.BaseFragment;
import com.mobitrack.mobi.ui.newExpenses.transactionSummery.deviceWise.DeviceWiseFragment;
import com.mobitrack.mobi.ui.newExpenses.transactionSummery.headwise.HeadWiseFragment;
import com.mobitrack.mobi.ui.newExpenses.transactionSummery.monthly.MonthlyFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionSummeryFragment extends BaseFragment implements SummeryContract.View {

    private TextView tvTotal,tvCount,tvHeader;

    private SummeryPresenter mPresenter;

    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

   private String[] headings = {"Headwise Summery","Devicewise Summery","Monthly Expenses"};


    public TransactionSummeryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SummeryPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_transaction_summery, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tvCount = view.findViewById(R.id.count);
        tvTotal = view.findViewById(R.id.total);
        tvHeader = view.findViewById(R.id.header);

        tvHeader.setText(headings[0]);
        viewPager = view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        if(adapter==null){
            adapter = new ViewPagerAdapter(getChildFragmentManager()); // Todo Maybe Changed
        }

        adapter.addFragment(new HeadWiseFragment(), "Head");
        adapter.addFragment(new DeviceWiseFragment(), "Device");
        adapter.addFragment(new MonthlyFragment(), "Month");

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateData(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void updateData(int position){
        tvHeader.setText(headings[position]);
        switch (position){
            case 0:
                HeadWiseFragment hwf = (HeadWiseFragment) adapter.getItem(position);
                hwf.initData();

                break;

            case 1:
                DeviceWiseFragment df = (DeviceWiseFragment) adapter.getItem(position);
                df.initData();
                break;

            case 2:
                MonthlyFragment mf = (MonthlyFragment) adapter.getItem(position);
                mf.initData();
                break;
        }
    }

    public void initUpdateData(){
        mPresenter.processData(getTransactions());
        updateData(viewPager.getCurrentItem());
    }

    @Override
    public void setCount(int number) {
        tvCount.setText(String.valueOf(number+" Nos"));
    }

    @Override
    public void setTotal(double total) {
        tvTotal.setText(String.valueOf(total));
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
