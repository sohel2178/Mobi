package com.mobitrack.mobi.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobitrack.mobi.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends Fragment {


    public AboutUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        //BannerSlider slider =  view.findViewById(R.id.slider);
        //List<Banner> bannerList=new ArrayList<>();

        String[] urls = getResources().getStringArray(R.array.about_slider);

        for(String x:urls){
            //bannerList.add(new RemoteBanner(x));
        }
        //slider.setBanners(bannerList);
        return view;
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_about_us, container, false);
    }

}
