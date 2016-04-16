package com.example.rick.catchit;

import android.app.Activity;
import android.media.MediaPlayer;

public class MySoundPlay {
    private Activity activity;

    public MySoundPlay(Activity activity){
        this.activity = activity;
    }

    public void playDropSound(){
        MediaPlayer currentSound = MediaPlayer.create(activity, R.raw.dropsound);
        currentSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mp.release();
            }
        });
        currentSound.start();
    }

    public void playBiteSound(){
        MediaPlayer biteSound = MediaPlayer.create(activity, R.raw.bitessound);
        biteSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mp.release();
            }
        });
        biteSound.start();
    }

    public MediaPlayer playBackGroundMusic(){
        MediaPlayer backgroundMusic = MediaPlayer.create(activity, R.raw.gamemusic);
        backgroundMusic.start();
        backgroundMusic.setLooping(true);
        return backgroundMusic;
    }
}
