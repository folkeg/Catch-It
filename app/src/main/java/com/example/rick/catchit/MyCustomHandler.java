package com.example.rick.catchit;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;

public class MyCustomHandler extends Handler {
    public final static int ADD_IMAGEVIEW_CHASE = 0;
    public final static int SET_VISIBILITY = 1;
    private ImageView iv;
    private AbsoluteLayout layout;
    private Activity activity;
    public MyCustomHandler(Activity activity, AbsoluteLayout layout){
        this.activity = activity;
        this.layout = layout;
    }
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case ADD_IMAGEVIEW_CHASE:
                iv = (ImageView )msg.obj;
                layout.addView(iv);
                iv.setVisibility(View.VISIBLE);
                iv.animate().x(new GameHelper(activity).getMetrics().widthPixels).setDuration(3500);
                break;
            case SET_VISIBILITY:
                iv = (ImageView)msg.obj;
                iv.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}
