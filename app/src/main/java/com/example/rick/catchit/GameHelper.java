package com.example.rick.catchit;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

public class GameHelper {
    private Activity activity;
    private DisplayMetrics metrics;
    private Typeface myCustomFont;

    public GameHelper(Activity activity){
        this.activity = activity;
    }

    public DisplayMetrics getMetrics(){
        metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    public void setMyCustomFont(TextView tv) {
        myCustomFont = Typeface.createFromAsset(activity.getAssets(),"fonts/myletter.ttf");
        tv.setTypeface(myCustomFont);
    }
}
