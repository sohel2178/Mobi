package com.mobitrack.mobi.ui.monthlyReport;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.mobitrack.mobi.BuildConfig;
import com.mobitrack.mobi.R;
import com.mobitrack.mobi.activities.PrebaseActivity;
import com.mobitrack.mobi.api.model.MonthlyData;
import com.mobitrack.mobi.model.MonthlyRBody;
import com.mobitrack.mobi.model.Vehicle;
import com.mobitrack.mobi.ui.monthlyReport.info.AddressFragment;
import com.mobitrack.mobi.utility.Constant;
import com.mobitrack.mobi.utility.MyUtil;
import com.mobitrack.mobi.utility.UserLocalStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import okhttp3.ResponseBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MonthlyReportActivity extends PrebaseActivity implements MonthlyReportContract.View, View.OnClickListener {

    private static final int READ_WRITE_PERMISSION=3000;
    private MonthlyReportPresenter mPresenter;

    private Vehicle vehicle;

    private TextView tvMonth,tvFuel,tvDistance;

    private int currentMonth,currentYear;

    private MonthlyAdapter adapter;

    private List<MonthlyData> monthlyDataList;
    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_report);
        gson = new Gson();

        mPresenter = new MonthlyReportPresenter(this, UserLocalStore.getInstance(this));
        this.vehicle = (Vehicle) getIntent().getSerializableExtra(Constant.DEVICE);

        this.adapter = new MonthlyAdapter(getApplicationContext(),vehicle);
        setupToolbar();
        this.currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        this.currentYear = Calendar.getInstance().get(Calendar.YEAR);

        initView();

    }

    private void initView() {
        TextView tvTitle = findViewById(R.id.title);
        TextView tvNext = findViewById(R.id.next);
        TextView tvPrev = findViewById(R.id.prev);
        tvTitle.setText("Monthly Report");
        tvNext.setOnClickListener(this);
        tvPrev.setOnClickListener(this);

        tvMonth = findViewById(R.id.month);
        tvFuel = findViewById(R.id.fuel);
        tvDistance = findViewById(R.id.distance);

        AppCompatImageView btnDownload = findViewById(R.id.download);
        btnDownload.setOnClickListener(this);

        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(adapter);




        mPresenter.requestMonthlyData(vehicle.getId(),currentYear,currentMonth);
    }

    @AfterPermissionGranted(READ_WRITE_PERMISSION)
    private void requestFileAfterPermission() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getApplicationContext(), perms)) {

            requestFile();

        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "App need to Permission for Location",
                    READ_WRITE_PERMISSION, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    public void requestFile() {
        MonthlyRBody monthlyRBody = new MonthlyRBody();
        monthlyRBody.setModel(vehicle.getModel());
        monthlyRBody.setDriver(vehicle.getDriver_name());
        monthlyRBody.setMilage(vehicle.getMileage());
        monthlyRBody.setFuel_in_congestion(vehicle.getCongestion_consumption());

        String data = gson.toJson(monthlyDataList);
        monthlyRBody.setData(data);
        mPresenter.requestForFile(monthlyRBody);
    }

    @Override
    public void showDialog() {
        showProgressDialog();
    }

    @Override
    public void hideDialog() {
        hideProgressDialog();
    }

    @Override
    public void updateMonthText() {
        tvMonth.setText(MyUtil.getMonthYear(getStartDate()));
    }

    @Override
    public void updateUI(List<MonthlyData> monthlyDataList) {
        this.monthlyDataList = monthlyDataList;
        hideProgressDialog();
        mPresenter.updateMonthText();
        //Update Adapter Here
        adapter.clear();

        for(MonthlyData x: monthlyDataList){
            adapter.addData(x);
        }

        mPresenter.updateFuelAndDistance();
    }

    @Override
    public void updateFuelAndDistance() {
        tvFuel.setText(MyUtil.getTwoDecimalFormat(adapter.getTotalFuel()).concat(" Lit"));
        tvDistance.setText(MyUtil.getTwoDecimalFormat(adapter.getTotalDistance()).concat(" KM"));
    }

    @Override
    public void showInfoFragment() {
        AddressFragment addressFragment = new AddressFragment();
        addressFragment.show(getSupportFragmentManager(),"SHOW");
    }

    @Override
    public String saveFile(ResponseBody body) {
        String file = Environment.getExternalStorageDirectory().getPath()+ File.separator+getString(R.string.app_name);
        File dir = new File(file);

        if(!dir.exists()){
            dir.mkdir();
        }

        String fileName = generateFileName();
        File myFile = new File(file, fileName);


        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            byte[] fileReader = new byte[4096];

            long fileSize = body.contentLength();
            long fileSizeDownloaded = 0;

            inputStream = body.byteStream();
            outputStream = new FileOutputStream(myFile);

            while (true) {
                int read = inputStream.read(fileReader);

                if (read == -1) {
                    break;
                }

                outputStream.write(fileReader, 0, read);

                fileSizeDownloaded += read;
            }

            outputStream.flush();

            Log.d("HHHHH",myFile.getPath());

            return myFile.getPath();
        } catch (IOException e) {
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openFile(String filePath) {
        String filename = "May-2019.pdf";

        File file = new File(filePath);



        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Uri uri = FileProvider.getUriForFile(this,
                BuildConfig.APPLICATION_ID+".fileprovider",file);

        target.setDataAndType(uri,"application/pdf");

        Log.d("IIIIII",uri.getAuthority());
        Log.d("IIIIII",uri.toString());



        Intent intent = Intent.createChooser(target, "Open File");
        try {
            PackageManager pm = getPackageManager();
            if (intent.resolveActivity(pm) != null) {
                startActivity(intent);
            }
            //startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
            showToast("Please Download a PDF Reader");
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.next:
                increase();
                mPresenter.requestMonthlyData(vehicle.getId(),currentYear,currentMonth);
                break;

            case R.id.prev:
                decrease();
                mPresenter.requestMonthlyData(vehicle.getId(),currentYear,currentMonth);
                break;

            case R.id.download:
                requestFileAfterPermission();
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

    private String getMonthText(){
       return getResources().getStringArray(R.array.month_array)[currentMonth];
    }

    private String generateFileName(){
        return getMonthText()+"-"+currentYear+".pdf";
    }



}
