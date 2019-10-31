package com.mobitrack.mobi.ui.newExpenses.helper.transaction;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.mobitrack.mobi.R;
import com.mobitrack.mobi.model.Head;
import com.mobitrack.mobi.model.Tran;
import com.mobitrack.mobi.model.Vehicle;
import com.mobitrack.mobi.ui.newExpenses.NewExpensesActivity;
import com.mobitrack.mobi.ui.newExpenses.transactionHistory.TransactionHistoryFragment;
import com.mobitrack.mobi.utility.Constant;
import com.mobitrack.mobi.utility.MyUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class TransactionDialogFragment extends DialogFragment implements TransactionContract.View,View.OnClickListener {


    private TransactionPresenter mPresenter;

    //View Element
    private AppCompatSpinner spDevice,spHead;
    private EditText etDate,etAmount,etPurpose;
    private TextInputLayout tDate,tAmount,tPurpose;
    private TextView tvCancel,tvSave,tvTitle;

    private ArrayAdapter<Head> headAdapter;
    private ArrayAdapter<Vehicle> vehicleAdapter;

    private ProgressBar mProgressBar;

    private long datetime;

    private Tran tran;

    private TransactionHistoryFragment parent;

    private boolean isUpdate = false;



    public TransactionDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPresenter = new TransactionPresenter(this);

        tran = (Tran) getArguments().getSerializable(Constant.TRANSACTION);

        if(tran!=null){
           isUpdate=true;
        }

        Log.d("HHHHH",(getParentFragment() instanceof TransactionHistoryFragment)+"");



        if(getActivity() instanceof NewExpensesActivity){
            parent = (TransactionHistoryFragment) getParentFragment();
            headAdapter = new ArrayAdapter<Head>(getContext(),android.R.layout.simple_list_item_1,parent.getHeadList());
            vehicleAdapter = new ArrayAdapter<Vehicle>(getContext(),android.R.layout.simple_list_item_1,parent.getVehicleList());

        }

    }

   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction_dialog, container, false);
    }*/


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_transaction_dialog, null);
        initView(view);


        AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.MyDialog).create();

        //AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.ThemeOverlay_AppCompat_Dialog);
        alertDialog.setView(view);
        return alertDialog;
    }

    private void initView(View view) {

        etDate = view.findViewById(R.id.et_date);
        etPurpose = view.findViewById(R.id.purpose);
        etAmount = view.findViewById(R.id.amount);

        mProgressBar = view.findViewById(R.id.progressBar);

        tDate = view.findViewById(R.id.ti_date);
        tPurpose = view.findViewById(R.id.ti_purpose);
        tAmount =view.findViewById(R.id.ti_amount);

        spHead = view.findViewById(R.id.sp_head);
        spDevice = view.findViewById(R.id.sp_vehicle);

        if(headAdapter!=null){
            spHead.setAdapter(headAdapter);
        }

        if(vehicleAdapter!=null){
            spDevice.setAdapter(vehicleAdapter);
        }


        tvCancel = view.findViewById(R.id.cancel);
        tvSave = view.findViewById(R.id.save);
        tvTitle = view.findViewById(R.id.title);

        tvCancel.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        etDate.setOnClickListener(this);

        mPresenter.controlView(tran);

    }



    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.et_date:
                DatePickerBuilder builder = new DatePickerBuilder(getContext(), new OnSelectDateListener() {
                    @Override
                    public void onSelect(List<Calendar> calendar) {
                        datetime = calendar.get(0).getTimeInMillis();
                        etDate.setText(MyUtil.getStringDate(new Date(datetime)));

                    }
                }).pickerType(CalendarView.ONE_DAY_PICKER).date(Calendar.getInstance());

                DatePicker datePicker = builder.build();
                datePicker.show();
                break;
            case R.id.cancel:
                mPresenter.onCancelClick();
                break;

            case R.id.save:
                if(!isUpdate){
                    Log.d("CALLL","Tran Null");
                    tran = new Tran();
                }
                String purpose = etPurpose.getText().toString().trim();
                String amountStr = etAmount.getText().toString().trim();
                double amount =0;

                try{
                    amount = Double.parseDouble(amountStr);
                }catch (Exception e){

                }

                tran.setDate(new Date(datetime));
                tran.setPurpose(purpose);
                tran.setAmount(amount);

                tran.setHead((Head) spHead.getSelectedItem());
                tran.setDevice_id(((Vehicle) spDevice.getSelectedItem()).getId());

                boolean valid = mPresenter.validate(tran);

                if(!valid){
                    return;
                }

                if(isUpdate){
                    Log.d("CALLL","Update Call");
                    mPresenter.updateTransaction(tran);
                }else {
                    Log.d("CALLL","Save Call");
                    Log.d("CALLL","Save Call "+tran.getCustomer_id());
                    mPresenter.saveTransaction(tran);
                }



                break;
        }

    }

    @Override
    public void dismissDialog() {
        dismiss();
    }

    @Override
    public void processTransaction(Tran transaction) {
        parent.transactionAdded(transaction);
        dismiss();
    }

    @Override
    public void updateTransactioninView(Tran transaction) {
        parent.transactionUpdated(transaction);
        dismiss();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void clearPreError() {
        tAmount.setErrorEnabled(false);
        tPurpose.setErrorEnabled(false);
    }

    @Override
    public void showError(String message, int field) {
        switch (field){
            case 1:
                etPurpose.requestFocus();
                tPurpose.setError(message);
                break;

            case 2:
                etAmount.requestFocus();
                tAmount.setError(message);
                break;
        }
    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void setupForTransactionEntry() {
        datetime = new Date().getTime();
        etDate.setText(MyUtil.getStringDate(new Date()));
    }

    @Override
    public void setupForTransactionUpdate() {
        tvTitle.setText(R.string.update_transaction);
        datetime = tran.getDate().getTime();
        etDate.setText(MyUtil.getStringDate(new Date(datetime)));
        etPurpose.setText(tran.getPurpose());
        etAmount.setText(String.valueOf(tran.getAmount()));

        int headPos = TranUtil.getHeadPosition(parent.getHeadList(),tran.getHead());
        int devicePos = TranUtil.getDevicePosition(parent.getVehicleList(),tran.getDevice_id());

        spHead.setSelection(headPos);
        spDevice.setSelection(devicePos);
        tvSave.setText(R.string.update);

    }
}
