package com.mobitrack.mobi.ui.map;

import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.anastr.speedviewlib.TubeSpeedometer;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobitrack.mobi.R;
import com.mobitrack.mobi.activities.PrebaseActivity;
import com.mobitrack.mobi.api.model.Fence;
import com.mobitrack.mobi.markerAnimation.LatLngInterpolator;
import com.mobitrack.mobi.markerAnimation.MarkerAnimation;
import com.mobitrack.mobi.model.FireData;
import com.mobitrack.mobi.model.Vehicle;
import com.mobitrack.mobi.ui.monthlyReport.MonthlyReportActivity;
import com.mobitrack.mobi.ui.report.ReportActivity;
import com.mobitrack.mobi.utility.Constant;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MapActivity extends PrebaseActivity implements OnMapReadyCallback,View.OnClickListener,MapContract.View {

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private Marker mMarker;

    private String deviceID;

    private LatLng currentLocation;
    private Vehicle currentVehicle;
    private Vehicle intentVehicle;

    private  int carIconSize;

    private boolean isVehicleLoad;

    private Circle fenceCircle;


    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            playSound();
            removeFence();
        }
    };






    // Clan
    private FloatingActionMenu fabHam;
    private com.github.clans.fab.FloatingActionButton fabFence,fabReport,fabRemoveFence,fabMonthlyReport;
    private TubeSpeedometer speedometer;

    private TextView tvDriverName;
    private ImageView ivIndicator;
    private CircleImageView ivDriverImage;

    private RelativeLayout rlBottomContainer,rlRoot;
    private int bottomContainerHeight;
    private boolean isOpen =false;

    private MapPresenter mPresenter;


    private void moveVehicle(){

        LatLng latLng = getLatLong(currentVehicle.getData().getLat(),currentVehicle.getData().getLng());
        mMarker.setIcon(BitmapDescriptorFactory.fromBitmap(getCarBitmap(carIconSize,carIconSize)));

        if(getDistance(latLng,currentLocation)>2){ // change 10m to 2
            MarkerAnimation.animateMarkerToGB(mMarker, latLng, new LatLngInterpolator.Spherical());
            mMarker.setTitle(getAddress(latLng));
            currentLocation = latLng;
        }

    }

    private double getDistance(LatLng latLng1,LatLng latLng2){
        Location loc1 = new Location("");
        Location loc2 = new Location("");

        if(latLng1!=null && latLng2!=null){
            loc1.setLatitude(latLng1.latitude);
            loc1.setLongitude(latLng1.longitude);


            loc2.setLatitude(latLng2.latitude);
            loc2.setLongitude(latLng2.longitude);
        }

        float distanceInMeters = loc1.distanceTo(loc2);

        return distanceInMeters;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        mPresenter = new MapPresenter(this);

        intentVehicle = (Vehicle) getIntent().getSerializableExtra(Constant.DEVICE);
        deviceID = intentVehicle.getId();
        currentVehicle = new Vehicle();
        carIconSize = (int) getResources().getDimension(R.dimen.car_icon_size);


        setupToolbar();
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(intentVehicle.getModel());






        //Todo Remove Later

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        //polyLineList = new ArrayList<>();

        mapFragment.getMapAsync(MapActivity.this);

        mPresenter.initView();

        //initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter= new IntentFilter();
        intentFilter.addAction(Constant.ACTION);
        registerReceiver(myReceiver,intentFilter);

    }

    @Override
    protected void onPause() {
        unregisterReceiver(myReceiver);
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mMap!=null){
            mPresenter.start();

        }
    }

    @Override
    protected void onStop() {

        mPresenter.stop();
        super.onStop();
    }

    @Override
    public void initView() {
        fabHam = findViewById(R.id.menu);
        fabFence = findViewById(R.id.fab_fence);
        fabReport = findViewById(R.id.fab_report);
        fabMonthlyReport = findViewById(R.id.fab_monthly_report);
        fabRemoveFence = findViewById(R.id.fab_remove_fence);

        fabFence.setOnClickListener(this);
        fabReport.setOnClickListener(this);
        fabRemoveFence.setOnClickListener(this);
        fabMonthlyReport.setOnClickListener(this);


        if(getIntent().getBooleanExtra(Constant.SHARE,false)){
            fabHam.setVisibility(View.GONE);
        }

        speedometer = findViewById(R.id.tubeSpeedMeter);
        speedometer.setWithEffects3D(true);
        speedometer.setAlpha(0.9f);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/digital_7.ttf");
        speedometer.setSpeedTextTypeface(face);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            speedometer.setBackgroundColor(getResources().getColor(android.R.color.transparent,null));
            speedometer.setSpeedometerColor(R.color.indicator_4);
        }else {
            speedometer.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            speedometer.setSpeedometerColor(R.color.indicator_4);
        }



       // tvModel = findViewById(R.id.model);
        tvDriverName = findViewById(R.id.driver_name);
        ivIndicator = findViewById(R.id.indicator);
        ivDriverImage = findViewById(R.id.driver_image);

        rlBottomContainer = findViewById(R.id.bottom_container);
        rlRoot = findViewById(R.id.root);
        ivIndicator.setOnClickListener(this);


        String driverName = intentVehicle.getDriver_name();
        String image = intentVehicle.getDriver_photo();


        if(driverName !=null && !driverName.equals("")){
            tvDriverName.setText(driverName);
        }

        if(image !=null && !image.equals("")){
            Picasso.with(getApplicationContext())
                    .load(image)
                    .into(ivDriverImage);
        }


        rlRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //get Card Height
                int rootHeight = rlRoot.getHeight();
                rlBottomContainer.setY(rootHeight-getResources().getDimension(R.dimen.indicator_dim));
                bottomContainerHeight = rlBottomContainer.getHeight();
                rlRoot.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });


    }

    @Override
    public void setVehicleData(FireData fireData) {
        currentVehicle.setData(fireData);
        currentLocation= getLatLong(currentVehicle.getData().getLat(),currentVehicle.getData().getLng());
        if(!isVehicleLoad){
            gotoVehicleLocation(currentLocation);
        }
    }

    @Override
    public void updateCurrentVehicle(FireData fireData) {
        currentVehicle.setData(fireData);
        speedometer.speedTo((float) Long.parseLong(fireData.getSpeed(),16),1000);
        moveVehicle();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void drawFence(Fence fence) {
        /*if(fencePolygon!=null){
            fencePolygon.remove();
        }*/

        removeFence();

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(new LatLng(fence.getLat(),fence.getLng()));
        circleOptions.radius(20);
        circleOptions.fillColor(Color.parseColor("#bbbbbbbb"));
        circleOptions.strokeWidth(6);
        circleOptions.strokeColor(Color.parseColor("#bbbbbb"));
        fenceCircle = mMap.addCircle(circleOptions);

    }

    @Override
    public void removeFence() {
        if(fenceCircle!=null){
            fenceCircle.remove();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("JJJJJ","JJJJJJ");

        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(true);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);

        // Listening From Firebase Database
        mPresenter.startListenFromDevice(deviceID);

    }


    private void gotoVehicleLocation(LatLng latLng) {
        isVehicleLoad=true;
        MarkerOptions option;

        if(getAddress(latLng)!=null){
            option = new MarkerOptions().position(latLng).title(getAddress(latLng));
        }else{
            option = new MarkerOptions().position(latLng).title("Vehicle Location");
        }

        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(18).tilt(30).build();
        mMarker = mMap.addMarker(option);
        mMarker.setIcon(BitmapDescriptorFactory.fromBitmap(getCarBitmap(carIconSize,carIconSize)));
        mMarker.setAnchor(0.5f,0.5f);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));



        if(intentVehicle.getVehicle_type()==7){
            speedometer.speedTo((float) ((float) Long.parseLong(currentVehicle.getData().getSpeed(),16)/1.852),1000);
            speedometer.setUnit("NM/H");
        }else{
            speedometer.speedTo((float) Long.parseLong(currentVehicle.getData().getSpeed(),16),1000);
        }

        // Request Fence Data From Our Server
        mPresenter.requestForFence(deviceID);

    }

    private Bitmap getCarBitmap(int newWidth, int newHeight) {

        Bitmap bitmap = null;

        switch (intentVehicle.getVehicle_type()){
            case 1:
                if(currentVehicle.getData().getStatus().equals("1")){
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.ic_green);
                }else{
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.ic_red);
                }
                break;

            case 2:
                if(currentVehicle.getData().getStatus().equals("1")){
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.bike_green);
                }else{
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.bike_red);
                }
                break;

            case 3:
                if(currentVehicle.getData().getStatus().equals("1")){
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.micro_green);
                }else{
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.micro_red);
                }
                break;

            case 4:
                if(currentVehicle.getData().getStatus().equals("1")){
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.bus_green);
                }else{
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.bus_red);
                }
                break;

            case 5:
                if(currentVehicle.getData().getStatus().equals("1")){
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.truck_green);
                }else{
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.truck_red);
                }
                break;

            case 6:
                if(currentVehicle.getData().getStatus().equals("1")){
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.cng_green);
                }else{
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.cng_red);
                }
                break;

            case 7:
                if(currentVehicle.getData().getStatus().equals("1")){
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.ship_green);
                }else{
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.ship_red);
                }
                break;

                default:
                    if(currentVehicle.getData().getStatus().equals("1")){
                        bitmap = BitmapFactory.decodeResource(getResources(),
                                R.drawable.ic_green);
                    }else{
                        bitmap = BitmapFactory.decodeResource(getResources(),
                                R.drawable.ic_red);
                    }

        }






        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bitmap, 0, 0, width, height, matrix, false);
        bitmap.recycle();
        return resizedBitmap;
    }

    private String getAddress(LatLng latLng){
        String address = null;
        Geocoder geocoder  =new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            address = addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }catch (IndexOutOfBoundsException ex){
            address = "Address Not Defined";
        }

        return address;
    }

    public int getPixelFromDp(float dp){
        Resources resources = getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                resources.getDisplayMetrics()
        );

        return px;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_fence:

                Fence fence = new Fence(intentVehicle);

                if(intentVehicle.getDriver_name()==null || intentVehicle.getDriver_name().equals("")){
                    fence.setDriver_name("Unnamed Drive");
                }

                if(intentVehicle.getDriver_photo()==null){
                    fence.setDriver_photo("");
                }

                if(intentVehicle.getModel()==null || intentVehicle.getModel().equals("")){
                    fence.setModel("Un-registered");
                }

                fence.setLat(mMarker.getPosition().latitude);
                fence.setLng(mMarker.getPosition().longitude);

                mPresenter.applyFence(fence);

                break;

            case R.id.fab_report:
                Intent intent = new Intent(getApplicationContext(),ReportActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.DEVICE,intentVehicle);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.fab_monthly_report:
                Intent intent1 = new Intent(getApplicationContext(), MonthlyReportActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable(Constant.DEVICE,intentVehicle);
                intent1.putExtras(bundle1);
                startActivity(intent1);
                break;

            case R.id.fab_remove_fence:
                mPresenter.removeFence(deviceID);
                break;

            case R.id.indicator:
                animate();
                break;
        }
    }


    private void animate(){
        final float animatedHeight = bottomContainerHeight-getResources().getDimension(R.dimen.indicator_dim);
        ValueAnimator animator = ValueAnimator.ofFloat(0,animatedHeight);

        final int currentY = (int) rlBottomContainer.getY();
        final float currentRotation =ivIndicator.getRotation();

        if(!isOpen){
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    ivIndicator.setRotation(currentRotation-animatedValue*180/animatedHeight);
                    rlBottomContainer.setY(currentY-animatedValue);
                    rlBottomContainer.requestLayout();
                }
            });
            isOpen=true;

        }else{
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();

                    ivIndicator.setRotation(currentRotation+animatedValue*180/animatedHeight);
                    rlBottomContainer.setY(currentY+animatedValue);
                    rlBottomContainer.requestLayout();
                }
            });

            isOpen=false;
        }



        animator.start();
    }


    private LatLng getLatLong(String lat,String lng){

        Log.d("KKKKKK",lat);
        Log.d("KKKKKK",lng);

        double lati = (double)Long.parseLong(lat,16)/1800000;
        double longi = (double)Long.parseLong(lng,16)/1800000;

        return new LatLng(lati,longi);
    }
}
