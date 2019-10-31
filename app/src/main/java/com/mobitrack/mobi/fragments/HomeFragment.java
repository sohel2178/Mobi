package com.mobitrack.mobi.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mobitrack.mobi.R;
import com.mobitrack.mobi.fragments.home.SharedVehicleFragment;
import com.mobitrack.mobi.model.User;
import com.mobitrack.mobi.singleton.MyDatabaseRef;
import com.mobitrack.mobi.ui.main.MainActivity;
import com.mobitrack.mobi.ui.main.ownVehicle.OwnVehicleFragment;

import java.util.ArrayList;
import java.util.List;

import github.chenupt.springindicator.SpringIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements
        AppBarLayout.OnOffsetChangedListener{

    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;

    private int mMaxScrollSize;

    private TextView tvCompanyName,tvCompanyDesc;



    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(getActivity() instanceof MainActivity){
            MainActivity ma = (MainActivity) getActivity();
            ma.setTitle(getString(R.string.app_name));
        }
    }

    private void initView(View view) {

        SpringIndicator springIndicator = (SpringIndicator) view.findViewById(R.id.indicator);
        ViewPager viewPager  =  view.findViewById(R.id.materialup_viewpager);
        AppBarLayout appbarLayout =  view.findViewById(R.id.materialup_appbar);

        tvCompanyName = view.findViewById(R.id.tv_company_name);
        tvCompanyDesc = view.findViewById(R.id.tv_company_desc);


        if(FirebaseAuth.getInstance().getCurrentUser() !=null){

            updateUI();




        }




        appbarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appbarLayout.getTotalScrollRange();

        TabsAdapter adapter = new TabsAdapter(getChildFragmentManager());
        adapter.addFragment(new OwnVehicleFragment());
        adapter.addFragment(new SharedVehicleFragment());


        viewPager.setAdapter(adapter);
        springIndicator.setViewPager(viewPager);


    }

    private void updateUI() {

        MyDatabaseRef.getInstance().getUserRef().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    User user = dataSnapshot.getValue(User.class);


                    tvCompanyName.setText(user.getCompanyName());
                    tvCompanyDesc.setText(user.getAddress());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }





    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (mMaxScrollSize == 0){
            mMaxScrollSize = appBarLayout.getTotalScrollRange();
        }


        int percentage = (Math.abs(verticalOffset)) * 100 / mMaxScrollSize;

        /*if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;

            mProfileImage.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            mProfileImage.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }*/

    }


    private static class TabsAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;

        TabsAdapter(FragmentManager fm) {
            super(fm);
            fragmentList = new ArrayList<>();
        }

        public void addFragment(Fragment fragment){
            fragmentList.add(fragment);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String bal="";
            switch (position){
                case 0:
                    bal="My"+"\nVehicles";
                    break;

                case 1:
                    bal ="Shared"+ "\nVehicles";
                    break;
            }
            return bal;
        }
    }
}
