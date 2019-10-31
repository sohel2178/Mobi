package com.mobitrack.mobi.activities;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.mobitrack.mobi.R;
import com.mobitrack.mobi.api.model.RData;
import com.mobitrack.mobi.markerAnimation.LatLngInterpolator;
import com.mobitrack.mobi.markerAnimation.MarkerAnimation;
import com.mobitrack.mobi.singleton.RawFData;

import java.util.ArrayList;
import java.util.List;

public class RawAnimActivity extends AppCompatActivity implements OnMapReadyCallback {

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;

    private int iconSize;
    private Marker mMarker;

    private PolylineOptions polylineOptions,darkPolyLineOption;
    private Polyline grayPolyLine,darkPolyLine;

    private MyThread myThread;

    List<RData> rDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide Status Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_raw_anim);

        rDataList = RawFData.getInstance().getData();

        iconSize = (int) getResources().getDimension(R.dimen.car_icon_size);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        //polyLineList = new ArrayList<>();

        mapFragment.getMapAsync(RawAnimActivity.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);

        //gotoFirstLatLong();

        animate();

    }

    private void animate() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
       /* for (LatLng x : latLongList) {
            builder.include(x);
        }*/

        final List<LatLng> tempList = new ArrayList<>();


        if(rDataList.size()>5){

            for (int i = 0; i< rDataList.size(); i++){

                if(i%2==1){
                    LatLng latLng = new LatLng(rDataList.get(i).getLat(), rDataList.get(i).getLng());
                    builder.include(latLng);
                    tempList.add(latLng);
                }

            }

        }else{
            for (int i = 0; i< rDataList.size(); i++){
                LatLng latLng = new LatLng(rDataList.get(i).getLat(), rDataList.get(i).getLng());
                builder.include(latLng);
                tempList.add(latLng);
            }
        }



        LatLngBounds bounds = builder.build();
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2);
        mMap.animateCamera(mCameraUpdate);

        polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.GRAY);
        polylineOptions.width(4);
        polylineOptions.startCap(new SquareCap());
        polylineOptions.endCap(new SquareCap());
        polylineOptions.jointType(JointType.ROUND);
        polylineOptions.addAll(tempList);

        grayPolyLine = mMap.addPolyline(polylineOptions);

        darkPolyLineOption = new PolylineOptions();
        darkPolyLineOption.width(6);
        darkPolyLineOption.color(Color.RED);
        darkPolyLineOption.startCap(new SquareCap());
        darkPolyLineOption.endCap(new SquareCap());
        darkPolyLineOption.jointType(JointType.ROUND);

        darkPolyLine = mMap.addPolyline(darkPolyLineOption);

        // Add a Marker at the Start of the Path
        mMap.addMarker(new MarkerOptions()
                .position(tempList.get(0)));

        // Poly Line Animator
        ValueAnimator polylineAnimator = ValueAnimator.ofInt(0, 100);
        polylineAnimator.setDuration(10000);
        polylineAnimator.setInterpolator(new LinearInterpolator());
        polylineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                List<LatLng> points = grayPolyLine.getPoints();
                int percentValue = (int) valueAnimator.getAnimatedValue();
                int size = points.size();
                int newPoints = (int) (size * (percentValue / 100.0f));
                List<LatLng> p = points.subList(0, newPoints);
                darkPolyLine.setPoints(p);

                if(valueAnimator.getAnimatedFraction()==1){
                    mMap.addMarker(new MarkerOptions()
                            .position(tempList.get(tempList.size() - 1)));
                }
            }
        });


        polylineAnimator.start();


        mMarker = mMap.addMarker(new MarkerOptions().position(tempList.get(0))
                .flat(true)
                .icon(BitmapDescriptorFactory.fromBitmap(getCarBitmap(iconSize,iconSize))));



        mMap.moveCamera(CameraUpdateFactory
                .newCameraPosition
                        (new CameraPosition.Builder()
                                .target(tempList.get(0))
                                .zoom(15.5f)
                                .build()));

        myThread = new MyThread(tempList); // Update here
    }

    private Bitmap getCarBitmap(int newWidth, int newHeight) {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_car);

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

    class MyThread extends Thread{
        List<LatLng> latLngList;

        int size,counter;
        private Handler handler;

        boolean isRunning;

        public MyThread(List<LatLng> latLngList){
            this.latLngList = latLngList;
            this.size = latLngList.size();
            this.counter=0;
            this.handler = new Handler();
            isRunning =true;
            this.start();
        }

        @Override
        public void run() {

            while (isRunning){
                try {
                    Thread.sleep(500);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            MarkerAnimation.myMapAnim(mMarker, latLngList.get(counter), new LatLngInterpolator.Spherical(),500);

                            /*Log.d("JJJJ","KK");*/
                        }
                    });

                    if(counter==size-2){
                        isRunning=false;
                    }

                    counter++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void stopThread(){
            isRunning=false;
            try {
                this.join();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
