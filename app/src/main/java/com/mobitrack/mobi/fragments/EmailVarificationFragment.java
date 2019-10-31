package com.mobitrack.mobi.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobitrack.mobi.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmailVarificationFragment extends Fragment {


    public EmailVarificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_email_varification, container, false);
    }

}
