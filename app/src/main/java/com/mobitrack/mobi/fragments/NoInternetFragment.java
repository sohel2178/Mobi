package com.mobitrack.mobi.fragments;


import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobitrack.mobi.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoInternetFragment extends Fragment {


    public NoInternetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_no_internet, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        MaterialButton btnReconnect= view.findViewById(R.id.re_connect);
        btnReconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().recreate();
            }
        });
    }

}
