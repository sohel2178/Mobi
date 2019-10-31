package com.mobitrack.mobi.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mobitrack.mobi.R;
import com.mobitrack.mobi.model.ShareVehicle;
import com.mobitrack.mobi.model.Vehicle;
import com.mobitrack.mobi.singleton.MyDatabaseRef;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sohel on 5/27/2018.
 */

public class SharedVehicleHolder extends RecyclerView.ViewHolder {
    private View mView;

    public SharedVehicleHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void bindVehicle(ShareVehicle shareVehicle){

        final CircleImageView driverImage = mView.findViewById(R.id.driver_image);
        final TextView tvDriverName = mView.findViewById(R.id.driver_name);
        final TextView tvDriverPhone = mView.findViewById(R.id.driver_phone);
        final TextView tvModel = mView.findViewById(R.id.model);

        MyDatabaseRef.getInstance().getDeviceRef().child(shareVehicle.getVehicleId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    Vehicle vehicle = dataSnapshot.getValue(Vehicle.class);

                    if(vehicle.getDriver_photo()!=null && !vehicle.getDriver_photo().equals("")){
                        Picasso.with(mView.getContext())
                                .load(vehicle.getDriver_photo())
                                .into(driverImage);
                    }

                    tvDriverName.setText(vehicle.getDriver_name());
                    tvDriverPhone.setText(vehicle.getDriver_phone());
                    tvModel.setText(vehicle.getModel());


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






    }

    public View getRootView(){
        return mView;
    }
}
