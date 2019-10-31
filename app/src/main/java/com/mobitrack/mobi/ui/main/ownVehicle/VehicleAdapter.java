package com.mobitrack.mobi.ui.main.ownVehicle;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.mobitrack.mobi.R;
import com.mobitrack.mobi.model.FireData;
import com.mobitrack.mobi.model.Vehicle;
import com.mobitrack.mobi.utility.MyUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleHolder> {

    private OwnVehicleFragment fragment;
    private List<Vehicle> vehicleList;
    private LayoutInflater inflater;

    public VehicleAdapter(OwnVehicleFragment fragment) {
        this.fragment = fragment;
        this.inflater = LayoutInflater.from(fragment.getContext());
        this.vehicleList = new ArrayList<>();
    }

    @NonNull
    @Override
    public VehicleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_driver,parent,false);
        return new VehicleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleHolder holder, int position) {
        Vehicle vehicle = vehicleList.get(position);
        holder.bind(vehicle);
    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

    public void add(Vehicle vehicle){
        vehicleList.add(vehicle);
        int position = vehicleList.indexOf(vehicle);
        notifyItemInserted(position);
    }

    public void updateVehicle(Vehicle vehicle){
        int position = getPosition(vehicle);
        if(position!=-1){
            vehicleList.set(position,vehicle);
            notifyItemChanged(position);
        }
    }

    public void removeVehicle(Vehicle vehicle){
        int position = getPosition(vehicle);
        vehicleList.remove(position);
        notifyItemRemoved(position);
    }


    private int getPosition(Vehicle vehicle){
        int retVal = -1;
        for (Vehicle x: vehicleList){
            if(x.getId().equals(vehicle.getId())){
                retVal = vehicleList.indexOf(x);
                break;
            }
        }
        return retVal;
    }


    class VehicleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivEdit,ivShare,ivShareUser,ivCall,ivIndicator;

        CircleImageView driverImage;

        TextView tvDriverName,tvModel,tvLocation;

        public VehicleHolder(@NonNull View itemView) {
            super(itemView);

            driverImage = itemView.findViewById(R.id.driver_image);
            tvDriverName = itemView.findViewById(R.id.driver_name);
            tvModel = itemView.findViewById(R.id.model);
            tvLocation = itemView.findViewById(R.id.current_location);

            ivIndicator = itemView.findViewById(R.id.indicator);

            ivEdit = itemView.findViewById(R.id.edit);
            ivShare = itemView.findViewById(R.id.share);
            ivShareUser = itemView.findViewById(R.id.share_user);
            ivCall = itemView.findViewById(R.id.driver_phone);

            itemView.setOnClickListener(this);
            ivEdit.setOnClickListener(this);
            ivShare.setOnClickListener(this);
            ivShareUser.setOnClickListener(this);
            ivCall.setOnClickListener(this);
        }


        public void bind(Vehicle vehicle){


            if(vehicle.getDriver_photo()!=null && !vehicle.getDriver_photo().equals("")){
                Picasso.with(itemView.getContext())
                        .load(vehicle.getDriver_photo())
                        .into(driverImage);
            }else {
                driverImage.setImageResource(R.drawable.ic_profile);
            }

            tvDriverName.setText(vehicle.getDriver_name());
            //tvDriverPhone.setText(vehicle.getDriver_phone());
            tvModel.setText(vehicle.getModel());

            FireData fireData = vehicle.getData();

            if(fireData!=null){

                LatLng latLng = getLatLong(vehicle.getData().getLat(),vehicle.getData().getLng());

                if(MyUtil.getAddress(itemView.getContext(),latLng)==null){
                    tvLocation.setText("Address Not Defined");
                }else {
                    tvLocation.setText(MyUtil.getAddress(itemView.getContext(),latLng));
                }




                if(fireData.getStatus().equals("1")){
                    ivIndicator.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
                }else{
                    ivIndicator.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
                }
            }
        }

        @Override
        public void onClick(View view) {

            if(view==itemView){
                fragment.startMapActivity(vehicleList.get(getAdapterPosition()));
            }else if(view==ivEdit){
                fragment.startVehicleEditActivity(vehicleList.get(getAdapterPosition()));
            }else if(view==ivShare){
                fragment.startVehicleShareActivity(vehicleList.get(getAdapterPosition()));
            }else if(view==ivShareUser){
                fragment.startSharedUserActivity(vehicleList.get(getAdapterPosition()));
            }else if(view==ivCall){
                fragment.callDriver(vehicleList.get(getAdapterPosition()));
            }

        }


        private LatLng getLatLong(String lat,String lng){
            double lati = (double)Long.parseLong(lat,16)/1800000;
            double longi = (double)Long.parseLong(lng,16)/1800000;

            return new LatLng(lati,longi);
        }
    }
}
