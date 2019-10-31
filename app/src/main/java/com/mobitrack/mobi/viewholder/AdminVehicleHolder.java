package com.mobitrack.mobi.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobitrack.mobi.R;
import com.mobitrack.mobi.model.Vehicle;

public class AdminVehicleHolder extends RecyclerView.ViewHolder {
    TextView tvImei,tvRegistration;
    ImageView fabEdit,fabUnassign;
    public AdminVehicleHolder(@NonNull View itemView) {
        super(itemView);
        tvImei = itemView.findViewById(R.id.imei);
        tvRegistration = itemView.findViewById(R.id.registration_number);
        fabEdit = itemView.findViewById(R.id.edit);
        fabUnassign = itemView.findViewById(R.id.un_assign);
    }

    public void bind(Vehicle vehicle){
        tvImei.setText(vehicle.getId());
        if(tvRegistration!=null){
            tvRegistration.setText(vehicle.getModel());
        }
    }

    public ImageView getEdit() {
        return fabEdit;
    }

    public ImageView getUnAssign() {
        return fabUnassign;
    }
}
