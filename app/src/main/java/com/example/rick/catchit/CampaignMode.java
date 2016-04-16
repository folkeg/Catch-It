package com.example.rick.catchit;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class CampaignMode extends Activity {
    private int hiScore = 0;
    private SensorManager mgr;
    private MyMotionSensorListener myMotionSensorListener;
    private CampaignModeThread campaignModeThread;
    private RelativeLayout restartGameLayout;
    private RelativeLayout nextStageLayout;
    private RelativeLayout lifeModeLayout;
    private RelativeLayout deathModeLayout;
    private RelativeLayout timeAttackModeLayout;
    private RelativeLayout layout;
    private ImageView piranha;
    private int myScore = 0;
    private final int FIRSTSTAGE = 0;
    private TextView hiScoreTitle;
    private TextView scoreTitle;
    private TextView foodTitle;
    private TextView baddiesTitle;
    private TextView timer;
    private TextView scores;
    private TextView foodAmount;
    private TextView baddiesAmount;
    private TextView btnRestartGame;
    private TextView btnRestartStage;
    private TextView btnQuit;
    private TextView btnNextStage;
    private TextView stageSign;
    private TextView startSign;
    private TextView stageTask;
    private GameHelper gameHelper;
    private boolean isMotionMode;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_mode);
        gameHelper = new GameHelper(this);
        findMyViewById();
        setFont();
        //readFile();
        isMotionMode = getIntent().getExtras().getBoolean("mode");
        setPlayMode(isMotionMode);
        StageSetup.setStage(FIRSTSTAGE);
        campaignModeThread = new CampaignModeThread(
                this,
                layout,
                restartGameLayout,
                nextStageLayout,
                lifeModeLayout,
                deathModeLayout,
                timeAttackModeLayout,
                scores,
                foodAmount,
                baddiesAmount,
                timer,
                stageSign,
                startSign,
                stageTask,
                piranha,
                gameHelper.getMetrics());
        campaignModeThread.start();

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
                    (PrimitiveImages.GameMode.TIMEATTACK, CampaignMode.this, piranha));
        }
    }

    public void findMyViewById(){
        layout = (RelativeLayout)findViewById(R.id.layout);
        restartGameLayout = (RelativeLayout)findViewById(R.id.restartGameLayout);
        nextStageLayout = (RelativeLayout)findViewById(R.id.nextStageLayout);
        lifeModeLayout = (RelativeLayout)findViewById(R.id.lifeStageLayout);
        deathModeLayout = (RelativeLayout)findViewById(R.id.deathModeLayout);
        timeAttackModeLayout = (RelativeLayout)findViewById(R.id.timeAttackModeLayout);
        timer = (TextView) findViewById(R.id.timer);
        hiScoreTitle = (TextView) findViewById(R.id.btnHiscore);
        scoreTitle = (TextView) findViewById(R.id.scoreTitle);
        foodTitle = (TextView) findViewById(R.id.foodTitle);
        baddiesTitle = (TextView) findViewById(R.id.baddiesTitle);
        btnRestartGame = (TextView) restartGameLayout.findViewById(R.id.btnRestartGame);
        btnRestartStage = (TextView) restartGameLayout.findViewById(R.id.btnRestartStage);
        btnNextStage = (TextView)findViewById(R.id.btnNextStage);
        btnQuit = (TextView) restartGameLayout.findViewById(R.id.btnQuit);
        scores = (TextView) findViewById(R.id.scoresText);
        foodAmount = (TextView)findViewById(R.id.foodAmount);
        baddiesAmount = (TextView)findViewById(R.id.baddiesAmount);
        stageSign = (TextView)findViewById(R.id.stageSign);
        startSign = (TextView)findViewById(R.id.startSign);
        stageTask = (TextView)findViewById(R.id.stageTask);
        piranha = (ImageView)findViewById(R.id.piranha);
    }

    public void setFont(){
        gameHelper.setMyCustomFont(hiScoreTitle);
        gameHelper.setMyCustomFont(scoreTitle);
        gameHelper.setMyCustomFont(foodTitle);
        gameHelper.setMyCustomFont(baddiesTitle);
        gameHelper.setMyCustomFont(scores);
        gameHelper.setMyCustomFont(foodAmount);
        gameHelper.setMyCustomFont(baddiesAmount);
        gameHelper.setMyCustomFont(timer);
        gameHelper.setMyCustomFont(btnRestartGame);
        gameHelper.setMyCustomFont(btnRestartStage);
        gameHelper.setMyCustomFont(btnNextStage);
        gameHelper.setMyCustomFont(btnQuit);
        gameHelper.setMyCustomFont(stageSign);
        gameHelper.setMyCustomFont(startSign);
        gameHelper.setMyCustomFont(stageTask);
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
        StageSetup.setStage(FIRSTSTAGE);
        this.finish();
    }

    public void onRestartGame(View view) {
        restartGameLayout.setVisibility(View.INVISIBLE);
        scores.setText("");
        StageSetup.setStage(FIRSTSTAGE);
        CampaignModeThread.setStage(FIRSTSTAGE);
        campaignModeThread = new CampaignModeThread(
                this,
                layout,
                restartGameLayout,
                nextStageLayout,
                lifeModeLayout,
                deathModeLayout,
                timeAttackModeLayout,
                scores,
                foodAmount,
                baddiesAmount,
                timer,
                stageSign,
                startSign,
                stageTask,
                piranha,
                gameHelper.getMetrics());
        campaignModeThread.start();
        //new CountTimer(60000,1).start();

    }
    public void onRestartStage(View view) {
        restartGameLayout.setVisibility(View.INVISIBLE);
        scores.setText("");
        StageSetup.setStage(CampaignModeThread.getStage());
        campaignModeThread = new CampaignModeThread(
                this,
                layout,
                restartGameLayout,
                nextStageLayout,
                lifeModeLayout,
                deathModeLayout,
                timeAttackModeLayout,
                scores,
                foodAmount,
                baddiesAmount,
                timer,
                stageSign,
                startSign,
                stageTask,
                piranha,
                gameHelper.getMetrics());
        campaignModeThread.start();
    }

    public void onNextStage(View view){
        nextStageLayout.setVisibility(View.INVISIBLE);
        scores.setText("");
        StageSetup.setStage(CampaignModeThread.getStage());
        campaignModeThread = new CampaignModeThread(
                this,
                layout,
                restartGameLayout,
                nextStageLayout,
                lifeModeLayout,
                deathModeLayout,
                timeAttackModeLayout,
                scores,
                foodAmount,
                baddiesAmount,
                timer,
                stageSign,
                startSign,
                stageTask,
                piranha,
                gameHelper.getMetrics());
        campaignModeThread.start();
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
            String tempScore = readIn.readLine();
            hiScoreTitle.setText("Hi Score: "+tempScore);
            hiScore = Integer.parseInt(tempScore);
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


