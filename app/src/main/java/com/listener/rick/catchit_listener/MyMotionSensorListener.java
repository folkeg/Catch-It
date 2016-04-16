package com.listener.rick.catchit_listener;

import android.app.Activity;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rick.catchit.GameHelper;

public class MyMotionSensorListener implements SensorEventListener {
    private SensorManager mgr;
    private Sensor accelerometer;
    private float gravity = 0;
    private ImageView piranha;
    private float move = 0;
    private final static int SPEED = 5;
    private DisplayMetrics metrics;
    public MyMotionSensorListener(ImageView piranha, DisplayMetrics metrics){
        this.piranha = piranha;
        this.metrics = metrics;
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        gravity  = -event.values[0];
        move = SPEED * gravity;
        if ((piranha.getX() + move) > 0 && (piranha.getX() + move) < metrics.widthPixels - piranha.getWidth()) {
            piranha.setX(piranha.getX() + move);
        }
    }
}
