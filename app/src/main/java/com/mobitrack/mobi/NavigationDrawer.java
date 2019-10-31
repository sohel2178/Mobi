package com.mobitrack.mobi;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mobitrack.mobi.fragments.HomeFragment;
import com.mobitrack.mobi.model.User;
import com.mobitrack.mobi.singleton.MyDatabaseRef;
import com.mobitrack.mobi.ui.admin.AdminActivity;
import com.mobitrack.mobi.ui.alert.AlertFragment;
import com.mobitrack.mobi.ui.login.LoginActivity;
import com.mobitrack.mobi.ui.main.MainActivity;
import com.mobitrack.mobi.ui.main.payment.PaymentFragment;
import com.mobitrack.mobi.ui.main.profile.ProfileFragment;
import com.mobitrack.mobi.ui.newExpenses.NewExpensesActivity;
import com.mobitrack.mobi.utility.Constant;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawer extends Fragment implements View.OnClickListener {

    public static final String PREF_NAME ="mypref";
    public static final String KEY_USER_LEARNED_DRAWERR="user_learned_drawer";

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;


    // View Initialize Here
    private CircleImageView ivProfile;
    private TextView tvName,tvEmail;

    private FirebaseUser mUser;
    private User user;

    private LinearLayout rvHome,rvAdmin,rvProfile, rvContactUs, rvLogOut,rvNotifications,rvAlart,rvPayment,rvExpenses;


    public NavigationDrawer() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(),KEY_USER_LEARNED_DRAWERR,"false"));

        // if saveInstanceState is not null its coming back from rotation
        if(savedInstanceState!=null){
            mFromSavedInstanceState=true;
        }

        mUser = FirebaseAuth.getInstance().getCurrentUser();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_navigation_drawer, container, false);


        //Initialize View

        initView(view);

        return view;
    }

    private void initView(View view) {

        ivProfile = view.findViewById(R.id.iv_profile);
        tvName = view.findViewById(R.id.name);
        tvEmail = view.findViewById(R.id.email);
        rvHome = view.findViewById(R.id.home);
        rvAdmin = view.findViewById(R.id.admin);
        rvProfile = view.findViewById(R.id.profile);
        rvContactUs = view.findViewById(R.id.contact_us);
        rvLogOut = view.findViewById(R.id.logout);
        rvNotifications = view.findViewById(R.id.notification);
        rvAlart = view.findViewById(R.id.alert);
        rvPayment = view.findViewById(R.id.payment);
        rvExpenses = view.findViewById(R.id.expenses);

        rvHome.setOnClickListener(this);
        rvAdmin.setOnClickListener(this);
        rvContactUs.setOnClickListener(this);
        rvLogOut.setOnClickListener(this);
        rvNotifications.setOnClickListener(this);
        rvAlart.setOnClickListener(this);
        rvProfile.setOnClickListener(this);
        rvPayment.setOnClickListener(this);
        rvExpenses.setOnClickListener(this);


        if(mUser!=null){
            MyDatabaseRef.getInstance().getUserRef()
                    .child(mUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue()!=null){
                                user = dataSnapshot.getValue(User.class);

                                if(!user.getPhotoUri().equals("")){
                                    Picasso.with(getContext())
                                            .load(user.getPhotoUri())
                                            .into(ivProfile);
                                }

                                if(user.getName()!=null){
                                    tvName.setText(user.getName());
                                }

                                if(user.getEmail()!=null){
                                    tvEmail.setText(user.getEmail());
                                }

                                if(user.getIsAdmin()==1){
                                    rvAdmin.setVisibility(View.VISIBLE);
                                }

                                if(user.getIsActive()==0){

                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }





    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }



    public void setUp(int fragmentId, DrawerLayout layout, final Toolbar toolbar) {

        mDrawerLayout = layout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),mDrawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                //if user gonna not seen the drawer before thats mean the drawer is open for the first time

                if(!mUserLearnedDrawer){
                    mUserLearnedDrawer=true;
                    // save it in sharedpreferences
                    saveToPreferences(getActivity(),KEY_USER_LEARNED_DRAWERR,mUserLearnedDrawer+"");

                    getActivity().invalidateOptionsMenu();
                }

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                super.onDrawerSlide(drawerView, slideOffset);
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }


    public static void saveToPreferences(Context context, String key, String prefValue){
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key,prefValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String key, String defaultValue){
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        return pref.getString(key,defaultValue);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.home:
                mDrawerLayout.closeDrawer(Gravity.START);
                if(!(getFragmentManager().findFragmentById(R.id.main_container) instanceof HomeFragment)){
                    getFragmentManager().beginTransaction().replace(R.id.main_container,new HomeFragment()).commit();
                }
                break;

            case R.id.admin:
                if(mDrawerLayout!=null){
                    mDrawerLayout.closeDrawer(Gravity.START);
                }

                Intent intent = new Intent(getContext(), AdminActivity.class);
                startActivity(intent);

                break;
            case R.id.profile:

                mDrawerLayout.closeDrawer(Gravity.START);
                if(!(getFragmentManager().findFragmentById(R.id.main_container) instanceof ProfileFragment)){
                    getFragmentManager().beginTransaction().replace(R.id.main_container,new ProfileFragment()).commit();
                }
            break;
            /*case R.id.about_us:
                mDrawerLayout.closeDrawer(Gravity.START);
                if(!(getFragmentManager().findFragmentById(R.id.main_container) instanceof AboutUsFragment)){
                    getFragmentManager().beginTransaction().replace(R.id.main_container,new AboutUsFragment()).commit();
                }
                break;*/
            case R.id.contact_us:
                Intent intent2 = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", getString(R.string.mobi_phone), null));
                startActivity(intent2);
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                /*mDrawerLayout.closeDrawer(Gravity.START);
                if(!(getFragmentManager().findFragmentById(R.id.main_container) instanceof ContactUsFragment)){
                    getFragmentManager().beginTransaction().replace(R.id.main_container,new ContactUsFragment()).commit();
                }*/
                break;

            case R.id.notification:
                mDrawerLayout.closeDrawer(Gravity.START);
               /* if(!(getFragmentManager().findFragmentById(R.id.main_container) instanceof TaxFragment)){
                    getFragmentManager().beginTransaction().replace(R.id.main_container,new TaxFragment()).commit();
                }*/
                break;

            case R.id.alert:
                mDrawerLayout.closeDrawer(Gravity.START);
                if(!(getFragmentManager().findFragmentById(R.id.main_container) instanceof AlertFragment)){
                    getFragmentManager().beginTransaction().replace(R.id.main_container,new AlertFragment()).commit();
                }
                break;

            case R.id.payment:
                mDrawerLayout.closeDrawer(Gravity.START);
                if(!(getFragmentManager().findFragmentById(R.id.main_container) instanceof PaymentFragment)){
                    getFragmentManager().beginTransaction().replace(R.id.main_container,new PaymentFragment()).commit();
                }
                break;

            case R.id.expenses:
                mDrawerLayout.closeDrawer(Gravity.START);

                Intent exIntent = new Intent(getContext(), NewExpensesActivity.class);
                if(user!=null){
                    exIntent.putExtra(Constant.NAME,user.getName());
                }

                startActivity(exIntent);
                break;

            case R.id.logout:
                mDrawerLayout.closeDrawer(Gravity.START);
                /*if(getActivity() instanceof MainActivity){
                    MainActivity activity = (MainActivity) getActivity();
                    activity.signOut();
                    activity.finish();
                    activity.getLocalDatabase().setIsUserSync(false);
                }*/

                if(getActivity() instanceof MainActivity){
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.logout();

                    getActivity().finish();

                    startActivity(new Intent(getContext(),LoginActivity.class));
                }

                break;

        }
    }

}
