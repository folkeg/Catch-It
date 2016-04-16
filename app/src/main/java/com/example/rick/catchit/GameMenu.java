package com.example.rick.catchit;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GameMenu extends ActionBarActivity {
    private final boolean isFinished = false;
    private GameThread gameThread;
    private MySoundPlay soundPlay;
    private RelativeLayout layout;
    private TextView btnStart;
    private TextView btnOption;
    private TextView btnTitle;
    private TextView btnTimeAttack;
    private TextView btnEvadeMode;
    private TextView btnCampaign;
    private TextView btnBack;
    private TextView btnConfirm;
    private GameHelper gameHelper;
    private CheckBox motionMode;
    private CheckBox classicMode;
    private MediaPlayer backgroundMusic;
    private boolean isMotionMode = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        soundPlay = new MySoundPlay(this);
        backgroundMusic = soundPlay.playBackGroundMusic();
        gameHelper = new GameHelper(this);
        setViewId();
        setTypeFace();
        gameThread = new GameThread(PrimitiveImages.GameMode.MENU,this,layout,isFinished,gameHelper.getMetrics());
        gameThread.start();
    }

    public void onStart(View view){
        setChildStartMenuVisible();
    }

    public void onOption(View view){
        setChildOptionMenuVisible();
    }

    public void onCheck(View view){
        boolean isChecked = ((CheckBox)view).isChecked();
        switch (view.getId()){
            case R.id.motionSensorMode:
                if (isChecked==true ) {
                    classicMode.setChecked(false);
                    isMotionMode = true;
                }
                else {
                    classicMode.setChecked(true);
                    isMotionMode = false;
                }
                break;
            case R.id.classicMode:
                if(isChecked==true){
                    motionMode.setChecked(false);
                    isMotionMode = false;
                }
                else {
                    motionMode.setChecked(true);
                    isMotionMode = true;
                }
                break;
            default:
                break;
            }

    }

    public void onConfirm(View view){
        setMenuVisible();
    }

    public void onCampaignMode(View view){
        gameThread.interrupt();
        Intent gameIntent = new Intent(this,CampaignMode.class);
        gameIntent.putExtra("mode", isMotionMode);
        startActivity(gameIntent);
        setMenuVisible();
    }

    public void onTimeAttack(View view) {
        gameThread.interrupt();
        Intent gameIntent = new Intent(this,TimeAttackMode.class);
        gameIntent.putExtra("mode", isMotionMode);
        startActivity(gameIntent);
        setMenuVisible();
    }

    public void onEvadeMode(View view) {
        gameThread.interrupt();
        Intent gameIntent = new Intent(this,EvadeMode.class);
        gameIntent.putExtra("mode", isMotionMode);
        startActivity(gameIntent);
        setMenuVisible();
    }

    public void onBack(View view) {
        setMenuVisible();
    }

    public void setupBlinkingAnimation(TextView textView) {
        Animation blinkingAnimation = new AlphaAnimation(0.0f, 1.0f);
        blinkingAnimation.setDuration(400);
        blinkingAnimation.setStartOffset(20);
        blinkingAnimation.setRepeatMode(Animation.REVERSE);
        blinkingAnimation.setRepeatCount(Animation.INFINITE);
        textView.startAnimation(blinkingAnimation);
    }

    public void setMenuVisible(){
        btnStart.setVisibility(View.VISIBLE);
        btnOption.setVisibility(View.VISIBLE);
        btnTimeAttack.setVisibility(View.INVISIBLE);
        btnEvadeMode.setVisibility(View.INVISIBLE);
        btnCampaign.setVisibility(View.INVISIBLE);
        motionMode.setVisibility(View.INVISIBLE);
        classicMode.setVisibility(View.INVISIBLE);
        btnConfirm.setVisibility(View.INVISIBLE);
        btnBack.setVisibility(View.INVISIBLE);
    }

    public void setChildStartMenuVisible(){
        btnStart.setVisibility(View.INVISIBLE);
        btnOption.setVisibility(View.INVISIBLE);
        btnTimeAttack.setVisibility(View.VISIBLE);
        btnEvadeMode.setVisibility(View.VISIBLE);
        btnCampaign.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.VISIBLE);
    }

    public void setChildOptionMenuVisible(){
        btnStart.setVisibility(View.INVISIBLE);
        btnOption.setVisibility(View.INVISIBLE);
        motionMode.setVisibility(View.VISIBLE);
        classicMode.setVisibility(View.VISIBLE);
        btnConfirm.setVisibility(View.VISIBLE);
    }

    public void setViewId(){
        btnStart = (TextView) findViewById(R.id.btnstart);
        btnOption = (TextView) findViewById(R.id.btnOption);
        btnTitle = (TextView) findViewById(R.id.btnTitle);
        layout = (RelativeLayout)findViewById(R.id.layout);
        motionMode = (CheckBox)findViewById(R.id.motionSensorMode);
        classicMode = (CheckBox)findViewById(R.id.classicMode);
        btnCampaign = (TextView) findViewById(R.id.btnCampaign);
        btnTimeAttack = (TextView) findViewById(R.id.btnTimeAttack);
        btnEvadeMode = (TextView) findViewById(R.id.btnEvadeMode);
        btnConfirm = (TextView) findViewById(R.id.btnConfirm);
        btnBack = (TextView) findViewById(R.id.btnBack);
    }

    public void setTypeFace(){
        gameHelper.setMyCustomFont(btnCampaign);
        gameHelper.setMyCustomFont(btnStart);
        gameHelper.setMyCustomFont(btnTitle);
        gameHelper.setMyCustomFont(motionMode);
        gameHelper.setMyCustomFont(classicMode);
        gameHelper.setMyCustomFont(btnTimeAttack);
        gameHelper.setMyCustomFont(btnEvadeMode);
        gameHelper.setMyCustomFont(btnOption);
        gameHelper.setMyCustomFont(btnConfirm);
        gameHelper.setMyCustomFont(btnBack);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_HOME ) {
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
