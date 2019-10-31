package com.mobitrack.mobi.fragments.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobitrack.mobi.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class InActiveFragment extends Fragment {


    public InActiveFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_in_active, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

       /* final CircleImageView imageView = view.findViewById(R.id.image);
        final TextView tvName= view.findViewById(R.id.name);
        final TextView tvPhone= view.findViewById(R.id.phone);
        final TextView tvEmail= view.findViewById(R.id.email);

        MyDatabaseRef.getInstance().getSuperAdminRef()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue()!=null){
                            SuperAdmin superAdmin = dataSnapshot.getValue(SuperAdmin.class);
                            tvName.setText(superAdmin.getName());
                            tvPhone.setText("Phone : ".concat(superAdmin.getPhone()));
                            tvEmail.setText("Email : ".concat(superAdmin.getEmail()));

                            if(superAdmin.getImageUrl()!=null && !superAdmin.getImageUrl().equals("")){
                                Picasso.with(getContext())
                                        .load(superAdmin.getImageUrl())
                                        .into(imageView);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/

    }

}
