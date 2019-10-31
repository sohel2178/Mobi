package com.mobitrack.mobi.ui.alert;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.mobitrack.mobi.R;
import com.mobitrack.mobi.api.model.Fence;
import com.mobitrack.mobi.utility.MyUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AlartAdapter extends RecyclerView.Adapter<AlartAdapter.AlartHolder> {

    private List<Fence> fenceList;
    private LayoutInflater inflater;

    public AlartAdapter(Fragment fragment) {
        fenceList =new ArrayList<>();
        inflater = LayoutInflater.from(fragment.getContext());
    }

    @NonNull
    @Override
    public AlartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_alert,parent,false);
        return new AlartHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlartHolder holder, int position) {

        Fence fence = fenceList.get(position);

        if(fence.getDriver_photo()!=null && !fence.getDriver_photo().equals("")){
            Picasso.with(inflater.getContext())
                    .load(fence.getDriver_photo())
                    .into(holder.ivImage);
        }

        double distance = getDistance(fence);
        String direction = getDirection(fence);

        String text = fence.getDriver_name().concat(", Vehicle Registration Number ")
                .concat(fence.getModel()).concat(" has changed his location ").concat(MyUtil.getTwoDecimalFormat(distance))
                .concat(" m from origin towards ").concat(direction).concat(" direction");

        holder.textView.setText(text);



        //Log.d("HHH","JJJJ"+direction);

    }



    public void addItem(Fence fence){
        fenceList.add(0,fence);
        notifyItemInserted(0);
    }

    public Fence getFence(int position){
        return fenceList.get(position);
    }

    public void removeItem(int position) {
        fenceList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Fence item, int position) {
        fenceList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    @Override
    public int getItemCount() {
        return fenceList.size();
    }

    class AlartHolder extends RecyclerView.ViewHolder{

        CircleImageView ivImage;
        TextView textView;

        public RelativeLayout viewBackground, viewForeground;

        public AlartHolder(@NonNull View itemView) {
            super(itemView);

            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);

            ivImage = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.text);
        }
    }

    private double getDistance(Fence fence){
        Location location = new Location("");
        location.setLatitude(fence.getLat());
        location.setLongitude(fence.getLng());

        Location location2 = new Location("");
        location2.setLatitude(fence.getNew_lat());
        location2.setLongitude(fence.getNew_lng());

        return location.distanceTo(location2);
    }

    private String getDirection(Fence fence) {
        double bearing = getBeating(fence);
        if(bearing==0 || bearing==360){
            return "N";
        }else if(bearing>0 && bearing<90){
            return "NE";
        }else if(bearing==90){
            return "E";
        }else if(bearing>90 && bearing<180){
            return "SE";
        }else if(bearing==180){
            return "S";
        }else if(bearing>180 && bearing<270){
            return "SW";
        }else if(bearing==270){
            return "W";
        }else if(bearing>270 && bearing<360){
            return "NW";
        }
        return "Undefined";
    }

    private double getBeating(Fence fence){
        LatLng begin = new LatLng(fence.getLat(),fence.getLng());
        LatLng end = new LatLng(fence.getNew_lat(),fence.getNew_lng());
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }
}
