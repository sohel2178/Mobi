package com.mobitrack.mobi.ui.customer;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;


import com.mobitrack.mobi.R;
import com.mobitrack.mobi.activities.PrebaseActivity;
import com.mobitrack.mobi.model.User;
import com.mobitrack.mobi.ui.customer.info.CustomerInfoFragment;
import com.mobitrack.mobi.ui.customer.vehicles.VehiclesFragment;
import com.mobitrack.mobi.utility.Constant;

import java.util.ArrayList;
import java.util.List;

public class CustomerActivity extends PrebaseActivity {

    private User user;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        user = (User) getIntent().getSerializableExtra(Constant.USER);

        if(user!=null){
            setupToolbar();
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(user.getName());
        }

        initView();
    }

    private void initView() {
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


       /* viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // Todo Dome Cod Goes Here

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
    }


    private void setupViewPager(ViewPager viewPager) {
        if(adapter==null){
            adapter = new ViewPagerAdapter(getSupportFragmentManager());
        }

        adapter.addFragment(new CustomerInfoFragment(), "Info");
        adapter.addFragment(new VehiclesFragment(), "Vehicles");
        viewPager.setAdapter(adapter);
    }


    public User getUser() {
        return user;
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
