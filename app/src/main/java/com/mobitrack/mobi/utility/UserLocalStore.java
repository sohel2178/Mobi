package com.mobitrack.mobi.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.mobitrack.mobi.singleton.MyDatabaseRef;

/**
 * Created by Genius 03 on 1/21/2018.
 */

public class UserLocalStore {

    private static final String SP_NAME ="userDetails";
    private static final String IS_USER_SYNC ="IS_USER_SYNC";
    private static final String MOBI_SUBSCRIBE ="MOBI_SUBSCRIBE";
    private static final String TOKEN_UPDATE ="TOKEN_UPDATE";
    private static final String COMPANY_DESC="COMPANY_DESC";

    private static final String ADDRESS="ADDRESS";
    private static final String ORGANIZATION_NAME="ORGANIZATION_NAME";

    private SharedPreferences userLocalDatabase;

    private static UserLocalStore instance;

    private UserLocalStore(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_NAME,0);
    }

    public static UserLocalStore getInstance(Context context) {
        if (instance == null) {
            instance = new UserLocalStore(context);
        }
        return instance;
    }

    public boolean isUserSync(){
        return userLocalDatabase.getBoolean(IS_USER_SYNC,false);
    }

    public void setIsUserSync(boolean value){
        userLocalDatabase.edit().putBoolean(IS_USER_SYNC,value).apply();
    }

    public boolean isMobiSubscribe(){
        return userLocalDatabase.getBoolean(MOBI_SUBSCRIBE,false);
    }

    public void setMobiSubscribe(boolean value){
        userLocalDatabase.edit().putBoolean(MOBI_SUBSCRIBE,value).apply();
    }


    public boolean isTokenUpDate(){
        return userLocalDatabase.getBoolean(TOKEN_UPDATE,false);
    }

    public void setTokenUpdate(boolean value){
        userLocalDatabase.edit().putBoolean(TOKEN_UPDATE,value).apply();
    }


    public void setOrganizationName(String name){
        userLocalDatabase.edit().putString(ORGANIZATION_NAME,name).apply();
    }

    public String getOrganizationName(){
        return userLocalDatabase.getString(ORGANIZATION_NAME,null);
    }


    public void setAddress(String name){
        userLocalDatabase.edit().putString(ADDRESS,name).apply();
    }

    public String getAddress(){
        return userLocalDatabase.getString(ADDRESS,null);
    }








}
