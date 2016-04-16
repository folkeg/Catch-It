package com.example.rick.catchit;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.listener.rick.catchit_listener.MyMotionSensorListener;
import com.listener.rick.catchit_listener.MyViewOnTouchListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TimeAttackMode extends ActionBarActivity  {
    private long startTime, estimatedTime;
    private SensorManager mgr;
    private MyMotionSensorListener myMotionSensorListener;
    private Thread gameThread;
    private final boolean isFinished = false;
    private RelativeLayout innerLayout, layout;
    private ImageView piranha;
    private int hiScore = 0, myScore = 0;
    private TextView hiScoreTitle;
    private TextView scoreTitle;
    private TextView timer;
    private TextView scores;
    private Button btnRestart;
    private Button btnQuit;
    private GameHelper gameHelper;
    private boolean isMotionMode;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_attack_mode);
        gameHelper = new GameHelper(this);
        findMyViewById();
        setFont();
        readFile();
        isMotionMode = getIntent().getExtras().getBoolean("mode");
        setPlayMode(isMotionMode);

        new CountTimer(60000,1).start();
        gameThread = new GameThread(
                PrimitiveImages.GameMode.TIMEATTACK,
                this,
                layout,
                isFinished,
                scores,
                piranha,
                gameHelper.getMetrics());
        gameThread.start();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void setPlayMode(boolean isMotionMode){
        if(isMotionMode==true) {
            mgr = (SensorManager) this.getSystemService(SENSOR_SERVICE);
            myMotionSensorListener = new MyMotionSensorListener(piranha, gameHelper.getMetrics());
        }
        else{
            piranha.setOnTouchListener(new MyViewOnTouchListener
                    (PrimitiveImages.GameMode.TIMEATTACK, TimeAttackMode.this, piranha));
        }
    }

    public void findMyViewById(){
        layout = (RelativeLayout)findViewById(R.id.layout);
        innerLayout = (RelativeLayout)findViewById(R.id.innerLayout);
        hiScoreTitle = (TextView) findViewById(R.id.btnHiscore);
        timer = (TextView) findViewById(R.id.timer);
        scoreTitle = (TextView) findViewById(R.id.scoreTitle);
        btnRestart = (Button)innerLayout.findViewById(R.id.btnRestart);
        btnQuit = (Button)innerLayout.findViewById(R.id.btnQuit);
        scores = (TextView) findViewById(R.id.scoresText);
        piranha = (ImageView)findViewById(R.id.piranha);
    }

    public void setFont(){
        gameHelper.setMyCustomFont(scoreTitle);
        gameHelper.setMyCustomFont(scores);
        gameHelper.setMyCustomFont(timer);
        gameHelper.setMyCustomFont(hiScoreTitle);
        gameHelper.setMyCustomFont(btnRestart);
        gameHelper.setMyCustomFont(btnQuit);
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

    public void onFinishGame(View view){
        this.finish();
    }
    public void onRestartGame(View view) {
        innerLayout.setVisibility(View.INVISIBLE);
        myScore = 0;
        scores.setText("");
        gameThread = new GameThread(
                PrimitiveImages.GameMode.TIMEATTACK,
                this,
                layout,
                isFinished,
                scores,
                piranha,
                gameHelper.getMetrics());
        gameThread.start();
        new CountTimer(60000,1).start();

    }
    private class CountTimer extends CountDownTimer{

        public CountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long time = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
            String hms = String.format("%02d:%02d:%01d", time, TimeUnit.MILLISECONDS.
                            toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(time),
                    (TimeUnit.MILLISECONDS.toMillis(millisUntilFinished) -
                            TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished))) / 10);
            timer.setText(hms);
        }

        @Override
        public void onFinish() {
            GameThread.endThread();
            timer.setText("00:00:00");
            compareScore();
            hiScoreTitle.setText("Hi Score: " + hiScore);
            writeFile();
            innerLayout.setVisibility(View.VISIBLE);
        }
    }
    public void compareScore(){
        if(myScore>hiScore){
            hiScore = myScore;
        }
    }
    public void readFile(){
        BufferedReader readIn;
        File file=new File(getFilesDir(),"scores.txt");
        try {
            readIn = new BufferedReader(new FileReader(file));
            String tempScore = readIn.readLine().trim();
            try {
                hiScore = Integer.parseInt(tempScore);
            }catch (NumberFormatException e){
                hiScore = 0;
            }
            hiScoreTitle.setText("Hi Score: "+ hiScore);
            readIn.close();
        }catch (IOException e){
            System.out.println("i");
        }
    }

    public void writeFile(){
        File file=new File(getFilesDir(),"scores.txt");
        try {
            FileWriter outputStream = new FileWriter(file, false);
            outputStream.write(String.valueOf(hiScore));
            outputStream.close();
        }
        catch (IOException e){
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.rick.coordinationtest/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.rick.coordinationtest/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}

