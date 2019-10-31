package com.mobitrack.mobi.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.mobitrack.mobi.R;
import java.util.Date;

/**
 * Created by IMATPC-12 on 29-Jan-18.
 */

public class LocalData {
    private static final String SP_NAME ="KeyDetails";
    private static final String API_KEY ="API_KEY";


    private SharedPreferences sharedPreferences;
    private Context context;

    private long dateTime;


    public LocalData(Context context) {
        this.context = context;
        sharedPreferences= context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
    }


    public int getLastIndex(){
        return sharedPreferences.getInt(API_KEY,0);
    }
    public void setLastIndex(int index){
        sharedPreferences.edit().putInt(API_KEY,index).apply();
    }


}
