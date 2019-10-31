package com.mobitrack.mobi.ui.resetRequest.dialog;


import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mobitrack.mobi.R;
import com.mobitrack.mobi.utility.Constant;


/**
 * A simple {@link Fragment} subclass.
 */
public class InfoDialog extends DialogFragment implements View.OnClickListener {

    private TextView tvContent,tvOK;

    String content = "";

    private DialogClickListener listener;


    public InfoDialog() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        content = getArguments().getString(Constant.CONTENT);
    }

    public void setListener(DialogClickListener listener){
        this.listener = listener;
    }


   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_dialog, container, false);
    }*/


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_info_dialog, null);
        initView(view);


        AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.MyDialog).create();

        //AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.ThemeOverlay_AppCompat_Dialog);
        alertDialog.setView(view);
        return alertDialog;
    }


    private void initView(View view) {

        tvContent = view.findViewById(R.id.content);
        tvOK = view.findViewById(R.id.ok);

        tvOK.setOnClickListener(this);

        if(content!=null){
            tvContent.setText(content);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.ok:
                if(listener!=null){
                    listener.positiveButtonClick();
                }
                break;
        }
    }

}
