package com.listener.rick.catchit_listener;

import android.app.Activity;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.rick.catchit.PrimitiveImages;

public class MyViewOnTouchListener implements View.OnTouchListener{
    private DisplayMetrics metrics;
    private float mouseX = 0;
    private PointF imagePT = new PointF();
    private ImageView dragAndDropObject;
    private Activity activity;
    private PrimitiveImages.GameMode gameMode;

    public MyViewOnTouchListener(PrimitiveImages.GameMode gameMode,Activity activity,ImageView iv){
        this.activity = activity;
        dragAndDropObject = iv;
        metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        this.gameMode = gameMode;
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int eventAction = event.getAction();
        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
                mouseX = event.getX();
                imagePT = new PointF(dragAndDropObject.getX(), dragAndDropObject.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                PointF move = new PointF(event.getX() - mouseX, 0);
                if ((imagePT.x + move.x) > 0 && (imagePT.x + move.x) < metrics.widthPixels - dragAndDropObject.getWidth()) {
                    dragAndDropObject.setX((int) (imagePT.x + move.x));
                    dragAndDropObject.setY((int) (imagePT.y));
                    imagePT = new PointF(dragAndDropObject.getX(), dragAndDropObject.getY());
                }
                break;
            default:
                break;
        }
        return true;
    }
}
