package com.mobitrack.mobi.ui.customer.vehicles.assign;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobitrack.mobi.R;
import com.mobitrack.mobi.model.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class AssignAdapter extends RecyclerView.Adapter<AssignAdapter.AdminVehicleHolder> {

    private AssignFragment fragment;
    private List<Vehicle> vehicleList;
    private LayoutInflater inflater;

    public AssignAdapter(AssignFragment fragment) {
        this.fragment = fragment;
        this.vehicleList = new ArrayList<>();
        this.inflater = LayoutInflater.from(fragment.getContext());
    }

    @NonNull
    @Override
    public AdminVehicleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_vehicle_two,parent,false);
        return new AdminVehicleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminVehicleHolder holder, int position) {
        Vehicle vehicle = vehicleList.get(position);
        holder.bind(vehicle);
    }

    public void add(Vehicle vehicle){
        vehicleList.add(vehicle);
        int position = vehicleList.indexOf(vehicle);
        notifyItemInserted(position);
    }

    public void update(Vehicle vehicle){
        int position = getPosition(vehicle);
        if(position!=-1){
            vehicleList.set(position,vehicle);
            notifyItemChanged(position);
        }
    }

    public void remove(Vehicle vehicle){
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

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }


    class AdminVehicleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvImei,tvRegistration;
        ImageView fabEdit,fabUnassign;

        public AdminVehicleHolder(@NonNull View itemView) {
            super(itemView);
            tvImei = itemView.findViewById(R.id.imei);
            tvRegistration = itemView.findViewById(R.id.registration_number);
            fabEdit = itemView.findViewById(R.id.edit);
            fabUnassign = itemView.findViewById(R.id.un_assign);

            itemView.setOnClickListener(this);
        }

        public void bind(Vehicle vehicle){
            tvImei.setText(vehicle.getId());
            if(tvRegistration!=null){
                tvRegistration.setText(vehicle.getModel());
            }
        }

        @Override
        public void onClick(View view) {
            fragment.assignDevice(vehicleList.get(getAdapterPosition()));
        }
    }
}
