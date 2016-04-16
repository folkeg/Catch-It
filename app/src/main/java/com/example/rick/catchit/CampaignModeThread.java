package com.example.rick.catchit;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Rect;
import android.opengl.Visibility;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class CampaignModeThread extends Thread {
    private StageSetup stageSetup;
    private static int stage = 0;
    private RelativeLayout.LayoutParams layoutParams;
    private Random random;
    private int questionMarkBonus = 0;
    private int imageId = 0;
    private int timeFlag = 0;
    private int food = 0;
    private int baddies = 0;
    private int myScore = 0;
    private Activity mainActivity;
    private RelativeLayout layout;
    private RelativeLayout restartGameLayout;
    private RelativeLayout nextStageLayout;
    private RelativeLayout lifeModeLayout;
    private RelativeLayout deathModeLayout;
    private RelativeLayout timeAttackModeLayout;
    private Handler myHandler;
    private MySoundPlay soundPlay;
    private TextView scoresText;
    private TextView foodAmount;
    private TextView baddiesAmount;
    private TextView timer;
    private TextView stageSign;
    private TextView startSign;
    private TextView stageTask;
    private final String tempStageSign;
    private ImageView piranha;
    private CopyOnWriteArrayList<ImageView> piranhaLife;
    private CopyOnWriteArrayList<ImageView> tempLife;
    private DisplayMetrics metrics;
    private ImageViewFactory imageViewFactory;
    private static boolean isFinished = true;
    private MyCountDownTimer countDownTimer;

    public CampaignModeThread(
            Activity activity,
            RelativeLayout layout,
            RelativeLayout restartGameLayout,
            RelativeLayout nextStageLayout,
            RelativeLayout lifeModeLayout,
            RelativeLayout deathModeLayout,
            RelativeLayout timeAttackModeLayout,
            TextView scoresText,
            TextView foodAmount,
            TextView baddiesAmount,
            TextView timer,
            TextView stageSign,
            TextView startSign,
            TextView stageTask,
            ImageView piranha,
            DisplayMetrics metrics){

        mainActivity = activity;
        this.layout = layout;
        this.restartGameLayout = restartGameLayout;
        this.nextStageLayout = nextStageLayout;
        this.lifeModeLayout = lifeModeLayout;
        this.timeAttackModeLayout = timeAttackModeLayout;
        this.deathModeLayout = deathModeLayout;
        this.scoresText = scoresText;
        this.foodAmount = foodAmount;
        this.baddiesAmount = baddiesAmount;
        this.timer = timer;
        this.stageSign = stageSign;
        this.startSign = startSign;
        this.stageTask = stageTask;
        this.piranha = piranha;
        this.metrics = metrics;
        tempStageSign = stageSign.getText().toString();
        myHandler = new Handler();
        soundPlay = new MySoundPlay(mainActivity);
        random = new Random();
        timeFlag = 0;
        stageSetup = new StageSetup();
        imageViewFactory = ImageViewFactory.getImageViewFactory();
        if(stageSetup.getMode()==1) {
            countDownTimer = new MyCountDownTimer(stageSetup.getCountDownTime(),1);
        }
        if(stageSetup.getMode()==2) {
            tempLife = new CopyOnWriteArrayList<ImageView>();
            piranhaLife = new CopyOnWriteArrayList<ImageView>();
            addLife();
        }
    }

    public void addLife(){
        piranhaLife.add((ImageView) mainActivity.findViewById(R.id.heartOne));
        piranhaLife.add((ImageView) mainActivity.findViewById(R.id.heartTwo));
        piranhaLife.add((ImageView) mainActivity.findViewById(R.id.heartThree));
        piranhaLife.add((ImageView) mainActivity.findViewById(R.id.heartFour));
        piranhaLife.add((ImageView) mainActivity.findViewById(R.id.heartFive));
        for (ImageView iv : piranhaLife){
            iv.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void run() {
        /**
         * Show Stage Information before start of every stage
         */
        setDropTextView(stageSign);
        myHandler.post(new Runnable() {
            @Override
            public void run() {
                stageSign.setVisibility(View.VISIBLE);
                stageSign.animate().y(layout.getHeight() / 2).setDuration(1000);
            }
        });
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stageSign.setVisibility(View.INVISIBLE);
                stageTask.setText(stageSetup.getTaskForStage());
                stageTask.setVisibility(View.VISIBLE);
                stageSign.animate().y(0.0f).setDuration(200);
            }
        }, 2000);
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stageTask.setVisibility(View.INVISIBLE);
                startSign.setVisibility(View.VISIBLE);
            }
        }, 4000);
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startSign.setVisibility(View.INVISIBLE);
                stageSign.setText(tempStageSign);
                isFinished = false;
            }
        }, 5000);
        while (isFinished) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        if(stageSetup.getMode()==1){
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    timeAttackModeLayout.setVisibility(View.VISIBLE);
                    lifeModeLayout.setVisibility(View.INVISIBLE);
                    deathModeLayout.setVisibility(View.INVISIBLE);
                }
            });
            countDownTimer.start();
        }
        else if(stageSetup.getMode()==2){
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    timeAttackModeLayout.setVisibility(View.INVISIBLE);
                    lifeModeLayout.setVisibility(View.VISIBLE);
                    deathModeLayout.setVisibility(View.INVISIBLE);
                }
            });
        }
        else if(stageSetup.getMode()==3){
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    timeAttackModeLayout.setVisibility(View.INVISIBLE);
                    lifeModeLayout.setVisibility(View.INVISIBLE);
                    deathModeLayout.setVisibility(View.VISIBLE);
                }
            });
        }
        while(!isFinished){
            try {
                Thread.sleep(stageSetup.getSleepTime());
                } catch (InterruptedException e) {}
            stageSetup.setImageId(questionMarkBonus);
            if(questionMarkBonus > 0){
                questionMarkBonus++;
            }
            imageId = stageSetup.getImageId();
            stageSetup.setLayoutParams();
            layoutParams = stageSetup.getLayoutParams();
            final ImageView currentImage = new ImageView(mainActivity);
            setDropImageView(currentImage, layoutParams);

            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    currentImage.setVisibility(View.VISIBLE);
                    layout.addView(currentImage);
                    currentImage.animate().y(metrics.heightPixels)
                            .setDuration(stageSetup.getDuration())
                            .rotation(360);
                    if(stageSetup.getMode()== 1){
                        currentImage.animate().setUpdateListener(new MyUpdateListener(
                                currentImage));
                    }
                    else if(stageSetup.getMode()== 2) {
                        currentImage.animate().setUpdateListener(new MyUpdateListener(
                                currentImage,
                                piranhaLife));
                        currentImage.animate().setListener(new MyAnimationListener(currentImage));
                    }
                    else if(stageSetup.getMode()==3){
                        currentImage.animate().setUpdateListener(new MyUpdateListener(
                                currentImage));
                        currentImage.animate().setListener(new MyAnimationListener(currentImage));
                    }
                }
            });
        }
    }

    public void setDropImageView(ImageView iv, RelativeLayout.LayoutParams params) {
        iv.setTag(imageId);
        iv.setImageResource(imageId);
        iv.setLayoutParams(params);
        Double width = metrics.widthPixels/1.2;
        iv.setX(random.nextInt(width.intValue()));
        iv.setY(0.0f);
    }

    private class MyUpdateListener implements ValueAnimator.AnimatorUpdateListener {
        private ImageView currentImage;
        private CopyOnWriteArrayList<ImageView> piranhaLife;

        public MyUpdateListener(ImageView iv){
            currentImage = iv;
        }

        public MyUpdateListener(ImageView iv, CopyOnWriteArrayList<ImageView> piranhaLife){
            currentImage = iv;
            this.piranhaLife = piranhaLife;
        }
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            if (currentImage.getY() <= piranha.getY() && !isFinished) {
                final Rect piranhaRect = new Rect();
                final Rect currentImageRect = new Rect();
                piranha.getGlobalVisibleRect(piranhaRect);
                currentImage.getGlobalVisibleRect(currentImageRect);
                if (piranhaRect.intersect(currentImageRect)) {
                    if(stageSetup.getMode()==1){
                        if(questionMarkBonus >= 4){
                            questionMarkBonus = 0;
                        }
                        if (imageViewFactory.getScoreTypeById((Integer) currentImage.getTag()).
                                equals(PrimitiveImages.ImageScore.QUESTIONMARK)) {
                            questionMarkBonus = 1;
                        }
                        soundPlay.playBiteSound();
                        myScore += imageViewFactory.getScoreById(
                                (Integer) currentImage.getTag());
                        scoresText.setText(String.valueOf(myScore));
                        if(myScore>=stageSetup.getTargetScore()){
                            isFinished = true;
                            stage++;
                            timeFlag = 1;
                            countDownTimer.cancel();
                            nextStageLayout.setVisibility(View.VISIBLE);
                            myScore = 0;
                        }
                    }
                    else if(stageSetup.getMode()==3){
                        soundPlay.playBiteSound();
                        food++;
                        foodAmount.setText(String.valueOf(food));
                        if(food==stageSetup.getTargetFood()){
                            isFinished = true;
                            stage++;
                            food = 0;
                            nextStageLayout.setVisibility(View.VISIBLE);
                        }
                    }
                    else if(stageSetup.getMode()==2){
                        if(piranhaLife.size()>0) {
                            if (imageViewFactory.getScoreTypeById((Integer) currentImage.getTag()).
                                    equals(PrimitiveImages.ImageScore.QUESTIONMARK)) {
                                if(tempLife.size() > 0){
                                    final ImageView tempImageView = tempLife.remove(tempLife.size() - 1);
                                    piranhaLife.add(tempImageView);
                                    myHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            tempImageView.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                            }
                            else {
                                ImageView tempImageView = piranhaLife.remove(piranhaLife.size() - 1);
                                tempLife.add(tempImageView);
                                tempImageView.setVisibility(View.INVISIBLE);
                                blinkingAnimation(piranha);
                            }
                        }
                        else if(piranhaLife.size()==0){
                            isFinished = true;
                            baddies = 0;
                            restartGameLayout.setVisibility(View.VISIBLE);
                        }
                    }
                    currentImage.setVisibility(View.GONE);
                    layout.removeView(currentImage);
                }
            }
        }
    }

    private class MyAnimationListener implements Animator.AnimatorListener{
        private ImageView currentImage;

        public MyAnimationListener(ImageView currentImage){
            this.currentImage = currentImage;
        }
        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if(stageSetup.getMode()==3 && currentImage.getVisibility() != View.GONE && !isFinished) {
                isFinished = true;
                food = 0;
                restartGameLayout.setVisibility(View.VISIBLE);
            }
            else if(stageSetup.getMode()==2){
                baddies++;
                myHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        baddiesAmount.setText(String.valueOf(baddies));
                    }
                });
                if(baddies==stageSetup.getTargetEvadeBaddy() && !isFinished){
                    isFinished = true;
                    stage++;
                    baddies = 0;
                    nextStageLayout.setVisibility(View.VISIBLE);
                }
                layout.removeView(currentImage);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    }
    public void blinkingAnimation(ImageView iv){
        Animation blink = new AlphaAnimation(0.0f, 1.0f);
        blink.setDuration(15);
        blink.setStartOffset(20);
        blink.setRepeatMode(Animation.REVERSE);
        blink.setRepeatCount(20);
        iv.startAnimation(blink);
    }

    public void setDropTextView(final TextView tv){
        myHandler.post(new Runnable() {
            @Override
            public void run() {
                tv.setText(tempStageSign + "  " + String.valueOf(stage+1));
            }
        });
    }

    public static int getStage(){
        return stage;
    }

    public static void setStage(int newStage){
        stage = newStage;
    }

    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            long time = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
            String hms = String.format("%02d:%02d:%01d", time, TimeUnit.MILLISECONDS.
                            toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(time),
                            (TimeUnit.MILLISECONDS.toMillis(millisUntilFinished) -
                            TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(
                                    millisUntilFinished))) / 10);
            timer.setText(hms);
        }

        @Override
        public void onFinish() {
            if(timeFlag == 0) {
                timer.setText("00:00:00");
                isFinished = true;
                restartGameLayout.setVisibility(View.VISIBLE);
            }
            //compareScore();
            //hiScoreTitle.setText("Hi Score: " + hiScore);
            //writeFile();
        }
    }
}
