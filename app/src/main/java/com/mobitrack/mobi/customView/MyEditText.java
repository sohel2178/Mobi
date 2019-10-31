package com.mobitrack.mobi.customView;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.View;

import com.mobitrack.mobi.R;


/**
 * Created by Sohel on 9/28/2016.
 */
public class MyEditText extends AppCompatEditText implements View.OnFocusChangeListener {
    private Context context;

    public MyEditText(Context context) {
        super(context);

    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        this.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.edit_text_background_deselect,null));
        this.setOnFocusChangeListener(this);
        this.setPadding(10,5,5,5);
        this.setTextColor(ResourcesCompat.getColor(context.getResources(),R.color.edit_text_text_color,null));

        //this.setHeight(80);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if(view.hasFocus()){
            view.setBackground(ResourcesCompat.getDrawable(context.getResources(),R.drawable.edit_text_background,null));
        }else{
            view.setBackground(ResourcesCompat.getDrawable(context.getResources(),R.drawable.edit_text_background_deselect,null));
        }
    }
}
