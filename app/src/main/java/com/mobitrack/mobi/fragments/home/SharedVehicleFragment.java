package com.mobitrack.mobi.fragments.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mobitrack.mobi.R;
import com.mobitrack.mobi.ui.map.MapActivity;
import com.mobitrack.mobi.model.ShareVehicle;
import com.mobitrack.mobi.model.Vehicle;
import com.mobitrack.mobi.singleton.MyDatabaseRef;
import com.mobitrack.mobi.utility.Constant;
import com.mobitrack.mobi.viewholder.SharedVehicleHolder;


/**
 * A simple {@link Fragment} subclass.
 */
public class SharedVehicleFragment extends Fragment {

    private RecyclerView rvVehicle;
    private Query query;
    FirebaseRecyclerAdapter<ShareVehicle,SharedVehicleHolder> adapter;

    public SharedVehicleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        query = MyDatabaseRef.getInstance().getSharedVehicleRef(uid)
                .orderByChild("status").equalTo(1);


        adapter = new FirebaseRecyclerAdapter<ShareVehicle, SharedVehicleHolder>(ShareVehicle.class,
                R.layout.single_shared_vehicle,SharedVehicleHolder.class,query) {
            @Override
            protected void populateViewHolder(SharedVehicleHolder viewHolder, final ShareVehicle model, int position) {

                viewHolder.bindVehicle(model);

                viewHolder.getRootView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        MyDatabaseRef myDatabaseRef = MyDatabaseRef.getInstance();

                        myDatabaseRef.getDeviceRef().child(model.getVehicleId())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Vehicle vehicle = dataSnapshot.getValue(Vehicle.class);

                                        if(vehicle!=null){
                                            Intent intent = new Intent(getContext(), MapActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable(Constant.DEVICE,vehicle);
                                            bundle.putBoolean(Constant.SHARE,true);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });



                    }
                });

            }
        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shared_vehicle, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        rvVehicle = view.findViewById(R.id.rv_vehicle);
        rvVehicle.setNestedScrollingEnabled(false);
        rvVehicle.setLayoutManager(new LinearLayoutManager(getContext()));
        rvVehicle.setAdapter(adapter);

    }
}
