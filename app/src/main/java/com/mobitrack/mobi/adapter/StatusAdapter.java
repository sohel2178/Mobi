package com.mobitrack.mobi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobitrack.mobi.R;
import com.mobitrack.mobi.api.model.RData;
import com.mobitrack.mobi.listener.VehicleStatusListener;
import com.mobitrack.mobi.model.FData;
import com.mobitrack.mobi.model.VehicleStatus;
import com.mobitrack.mobi.singleton.RawFData;
import com.mobitrack.mobi.utility.MyUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by sohel on 6/7/2018.
 */

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.Statusholder> {
    private Context context;
    private List<VehicleStatus> vehicleStatusList;
    private LayoutInflater inflater;

    private VehicleStatusListener listener;

    private int vehicleType;


    public StatusAdapter(Context context,int vehicleType,VehicleStatusListener listener) {
        this.context = context;
        this.listener = listener;
        this.vehicleStatusList = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
        this.vehicleType = vehicleType;
    }

    @Override
    public Statusholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_status,parent,false);
        return new Statusholder(view);
    }

    @Override
    public void onBindViewHolder(final Statusholder holder, int position) {
        final VehicleStatus vehicleStatus = vehicleStatusList.get(position);

        String stTime = MyUtil.getStringDate3(new Date(vehicleStatus.getStartTime())).split(" ")[1];
        String endTime = MyUtil.getStringDate3(new Date(vehicleStatus.getEndTime())).split(" ")[1];

        holder.tvStartTime.setText(stTime);
        holder.tvEndTime.setText(endTime);

        int duration =Math.round((vehicleStatus.getEndTime()-vehicleStatus.getStartTime())/1000);

        String val ="";
        if(duration>=60*60){
            int hour = duration/(60*60);
            int min = duration%60;

            //Log.d("KKKK",duration)

            val = hour+" hr "+min+" min";

        }else if(duration<3600 && duration>=60){
            int min = duration/60;
            int sec = (int) (duration-min*60);
            val = min+" min "+sec+" sec";
        }else {
            val ="0 min "+ duration+" sec";
        }

        holder.tvDuration.setText(val);

        if(vehicleStatus.getStatus().equals("0")){
            holder.tvStatus.setText("OFF");
        }else {
            holder.tvStatus.setText("ON");
        }


        Observable<Double> distanceObs = Observable.fromCallable(new Callable<Double>() {
            @Override
            public Double call() throws Exception {
                return getDistance(vehicleStatus);
            }
        });

        distanceObs.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Double>() {
                    @Override
                    public void call(Double aDouble) {
                        if(vehicleType==7){
                            holder.tvDistance.setText(MyUtil.getTwoDecimalFormat(aDouble/1852)+" NM");
                        }else {
                            holder.tvDistance.setText(MyUtil.getTwoDecimalFormat(aDouble/1000)+" km");
                        }

                    }
                });




    }

    public void clear(){
        this.vehicleStatusList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void addVehicleStatus(VehicleStatus vehicleStatus){
        vehicleStatusList.add(vehicleStatus);
        int pos = vehicleStatusList.indexOf(vehicleStatus);
        notifyItemInserted(pos);
    }

    @Override
    public int getItemCount() {
        return vehicleStatusList.size();
    }

    class Statusholder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvStartTime,tvEndTime,tvDuration,tvStatus,tvDistance;

        public Statusholder(View itemView) {
            super(itemView);

            tvStartTime = itemView.findViewById(R.id.start_time);
            tvEndTime = itemView.findViewById(R.id.end_time);
            tvDuration = itemView.findViewById(R.id.duration);
            tvDistance = itemView.findViewById(R.id.distance);
            tvStatus = itemView.findViewById(R.id.status);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onStatusClick(vehicleStatusList.get(getAdapterPosition()));
        }
    }


    private double getDistance(VehicleStatus status){
        double distance =0;
        List<RData> rDataList = new ArrayList<>();

        for (RData x:RawFData.getInstance().getData()){
            if(x.getServerTime()>=status.getStartTime() && x.getServerTime()<=status.getEndTime()){
                rDataList.add(x);
            }
        }

        if(rDataList.size()>=2){
            distance = MyUtil.getDistanceFrom(rDataList);
        }
        return distance;
    }
}
