package com.example.rick.catchit;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.listener.rick.catchit_listener.MyMotionSensorListener;
import com.listener.rick.catchit_listener.MyViewOnTouchListener;

public class EvadeMode extends ActionBarActivity {
    private SensorManager mgr;
    private MyMotionSensorListener myMotionSensorListener;
    private final boolean isFinished = false;
    private boolean isMotionMode;
    private Thread gameThread;
    private static RelativeLayout innerLayout, layout;
    private ImageView piranha;
    private int hiScore = 0, myScore = 0;
    private TextView hiScoreTitle;
    private TextView scores;
    private TextView scoreTitle;
    private Button btnRestart;
    private Button btnQuit;
    private GameHelper gameHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evade_mode);
        gameHelper = new GameHelper(this);
        findMyViewById();
        setFont();
        isMotionMode = getIntent().getExtras().getBoolean("mode");
        setPlayMode(isMotionMode);
        gameThread = new GameThread(
                PrimitiveImages.GameMode.EVADE,
                this,
                layout,
                isFinished,
                scores,
                piranha,
                gameHelper.getMetrics());
        gameThread.start();
    }

    public void setPlayMode(boolean isMotionMode){
        if(isMotionMode==true) {
            mgr = (SensorManager) this.getSystemService(SENSOR_SERVICE);
            myMotionSensorListener = new MyMotionSensorListener(piranha, gameHelper.getMetrics());
        }
        else{
            piranha.setOnTouchListener(new MyViewOnTouchListener
                    (PrimitiveImages.GameMode.EVADE, EvadeMode.this, piranha));
        }
    }
    @Override
    protected void onResume() {
        if(isMotionMode==true) {
            mgr.registerListener(myMotionSensorListener,
                    mgr.getDefaultSensor(Sensor.TYPE_GRAVITY),
                    SensorManager.SENSOR_DELAY_GAME);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if(isMotionMode==true) {
            mgr.unregisterListener(myMotionSensorListener,
                    mgr.getDefaultSensor(Sensor.TYPE_GRAVITY));
        }
        super.onPause();
    }

    public static void showEndLayout(){
        innerLayout.setVisibility(View.VISIBLE);
    }

    public void onEvadeModeFinishGame(View view){
        this.finish();
    }

    public void onEveadeModeRestartGame(View view) {
        innerLayout.setVisibility(View.INVISIBLE);
        myScore = 0;
        scores.setText("");
        gameThread = new GameThread(
                PrimitiveImages.GameMode.EVADE,
                this,
                layout,
                isFinished,
                scores,
                piranha,
                gameHelper.getMetrics());
        gameThread.start();
    }

    private void findMyViewById(){
        layout = (RelativeLayout)findViewById(R.id.evadeModeLayout);
        innerLayout = (RelativeLayout)findViewById(R.id.evadeModeInnerLayout);
        hiScoreTitle = (TextView) findViewById(R.id.btnHiscore);
        scoreTitle = (TextView) findViewById(R.id.scoreTitle);
        scores = (TextView) findViewById(R.id.scoresText);
        piranha = (ImageView)findViewById(R.id.piranha);
        btnRestart = (Button)innerLayout.findViewById(R.id.btnRestart);
        btnQuit = (Button)innerLayout.findViewById(R.id.btnQuit);
    }

    private void setFont(){
        gameHelper.setMyCustomFont(scoreTitle);
        gameHelper.setMyCustomFont(scores);
        gameHelper.setMyCustomFont(hiScoreTitle);
        gameHelper.setMyCustomFont(btnRestart);
        gameHelper.setMyCustomFont(btnQuit);
    }
}
