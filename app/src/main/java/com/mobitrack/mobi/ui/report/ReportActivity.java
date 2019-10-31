package com.mobitrack.mobi.ui.report;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.mobitrack.mobi.R;
import com.mobitrack.mobi.activities.PrebaseActivity;
import com.mobitrack.mobi.activities.RawAnim;
import com.mobitrack.mobi.api.model.RData;
import com.mobitrack.mobi.api.model.RequestBody;
import com.mobitrack.mobi.dialogfragment.UpdateMileageFragment;
import com.mobitrack.mobi.fragments.reportNav.ChartFragment;
import com.mobitrack.mobi.fragments.reportNav.StatusFragment;
import com.mobitrack.mobi.ui.report.table.TableFragment;
import com.mobitrack.mobi.listener.FragmentListner;
import com.mobitrack.mobi.listener.UpdateMilageListener;
import com.mobitrack.mobi.model.Vehicle;
import com.mobitrack.mobi.singleton.MyDatabaseRef;
import com.mobitrack.mobi.singleton.RawFData;
import com.mobitrack.mobi.utility.Constant;
import com.mobitrack.mobi.utility.MyUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReportActivity extends PrebaseActivity implements View.OnClickListener,ReportContract.View {

    private static final int THRESHOLD =60;
    private ProgressDialog mProgressDialog;

    private Vehicle vehicle;
    private ReportPresenter mPresenter;
    private RequestBody requestBody;


    private Date selectedDate;

    private BottomNavigationView bottomNavigationView;
    private TextView tvDistance,tvTravelTime,tvFuel;

    private double totalDistance;
    private CardView cAnim,cFuel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        mPresenter = new ReportPresenter(this);

        setupToolbar();
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        vehicle = (Vehicle) getIntent().getSerializableExtra(Constant.DEVICE);
        selectedDate = new Date();

        initView();


        //firestoreReq2();

        requestBody = new RequestBody();
        requestBody.setDevice_time(MyUtil.getReqDate(selectedDate));
        requestBody.setImei(vehicle.getId());

        mPresenter.requestForData(requestBody);

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
    public void updateTitle() {
        getSupportActionBar().setTitle("Report On "+MyUtil.getStringDate(selectedDate));
    }


    @Override
    public void updateFuel(){
        if(vehicle.getMileage()==0){
            tvFuel.setText("Update Mileage");
        }else{
            double fuel = totalDistance/vehicle.getMileage()/1000;
            tvFuel.setText(MyUtil.getTwoDecimalFormat(fuel)+" Lit");
        }
    }

    @Override
    public void updateDistance(double distance) {
        totalDistance = distance;

        if(vehicle.getVehicle_type()==7){
            tvDistance.setText(MyUtil.getTwoDecimalFormat(totalDistance/1852)+" NM");
        }else{
            tvDistance.setText(MyUtil.getTwoDecimalFormat(totalDistance/1000)+" km");
        }


    }

    @Override
    public void updateFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);

        if(fragment==null){
            fragment = new TableFragment();
            loadFragment(fragment);
        }

        if(fragment instanceof TableFragment){
            final TableFragment tableFragment = (TableFragment) fragment;
            tableFragment.update();

        }else if(fragment instanceof ChartFragment){
            ChartFragment cf = (ChartFragment) fragment;
            cf.update();
        }else if(fragment instanceof StatusFragment){
            StatusFragment sf = (StatusFragment) fragment;
            sf.update();
        }
    }



    @Override
    public void updateTravelTime(String travelTime) {
        if(travelTime==null){
            tvTravelTime.setText("0 hr 0 min");
        }else{
            tvTravelTime.setText(travelTime);
        }
    }

    public int getVehicleType(){
       return vehicle.getVehicle_type();
    }

    @Override
    public void visibleBottomNavigationView() {
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        mPresenter.destroy();
        super.onDestroy();


    }




    public void showProgressDialog() {
        if (mProgressDialog == null) {

            mProgressDialog = new ProgressDialog(this, R.style.MyTheme);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void initView() {
        cAnim = findViewById(R.id.anim);
        cFuel = findViewById(R.id.fuel);

        cAnim.setOnClickListener(this);
        cFuel.setOnClickListener(this);

        tvDistance = findViewById(R.id.tvDistance);
        tvTravelTime = findViewById(R.id.tv_running_time);
        tvFuel = findViewById(R.id.tv_fuel_consumption);

        if(vehicle.getVehicle_type()==7){
            tvDistance.setText("0.00 NM");
        }

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setVisibility(View.GONE);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()){

                    case R.id.action_item1:
                        loadFragment(new TableFragment());
                        break;

                    case R.id.action_item2:
                        loadFragment(new ChartFragment());

                        //selectedFragment = new WorkDoneChartFragment();
                        break;

                    case R.id.action_item3:
                        loadFragment(new StatusFragment());
                        //selectedFragment = new WorkDoneChartFragment();
                        break;

                }

                return true;
            }
        });

        mPresenter.initialize();
        mPresenter.updateTitle();
        mPresenter.updateFuel();

    }


    private void loadFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,fragment).commit();

    }






    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.anim:
                if(totalDistance>1000){

                    Intent intent = new Intent(getApplicationContext(),RawAnim.class);
                    intent.putExtra(Constant.V_TYPE,vehicle.getVehicle_type());
                    startActivity(intent);
                }

                break;

            case R.id.fuel:

                UpdateMileageFragment updateMileageFragment = new UpdateMileageFragment();
                Bundle bundle = new Bundle();
                bundle.putDouble(Constant.MILEAGE,vehicle.getMileage());
                updateMileageFragment.setArguments(bundle);
                updateMileageFragment.setListener(new UpdateMilageListener() {
                    @Override
                    public void update(final double value) {
                        Toast.makeText(ReportActivity.this, ""+value, Toast.LENGTH_SHORT).show();

                        MyDatabaseRef.getInstance()
                                .getDeviceRef().child(vehicle.getId())
                                .child("mileage")
                                .setValue(value, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        vehicle.setMileage(value);
                                        updateFuel();
                                    }
                                });
                    }
                });

                updateMileageFragment.show(getFragmentManager(),"UPDATE");

                break;

        }



    }

    private void openCalender() {
        DatePickerBuilder builder = new DatePickerBuilder(this, new OnSelectDateListener() {
            @Override
            public void onSelect(List<Calendar> calendar) {
                selectedDate = calendar.get(0).getTime();
                mPresenter.updateTitle();

                requestBody.setDevice_time(MyUtil.getReqDate(selectedDate));
                mPresenter.requestForData(requestBody);
                //updateTitle();
                //firestoreReq2();
            }
        }).pickerType(CalendarView.ONE_DAY_PICKER).date(Calendar.getInstance())
                ;

        DatePicker datePicker = builder.build();
        datePicker.show();
    }

    @Override
    public void onBackPressed() {
        RawFData.getInstance().setData(new ArrayList<RData>());
        super.onBackPressed();
    }

    // Option Menu
    // Initiating Menu XML file (menu.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.report_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId())
        {
            case R.id.menu_calendar:
                openCalender();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
