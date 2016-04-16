package com.example.rick.catchit;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameThread extends Thread {
    private PrimitiveImages.GameMode mode;
    private RelativeLayout.LayoutParams layoutParams;
    private Random random;
    private int questionMarkBonus = 0;
    private int sameItemEatenBonus = 0;
    private int imageId = 0;
    private Integer myScore = 0;
    private Activity mainActivity;
    private RelativeLayout layout;
    private Handler myHandler;
    private MySoundPlay soundPlay;
    private TextView scoresText;
    private ImageView piranha;
    private CopyOnWriteArrayList<ImageView> piranhaLife;
    private CopyOnWriteArrayList<ImageView> tempLife;
    private DisplayMetrics metrics;
    private ImageViewFactory imageViewFactory;
    private static boolean isFinished = false;
    private ImageView formerImage;

    public GameThread(
            PrimitiveImages.GameMode mode,
            Activity activity,
            RelativeLayout layout,
            boolean isFinished,
            TextView scoresText,
            ImageView piranha,
            DisplayMetrics metrics){

        this.mode = mode;
        mainActivity = activity;
        this.layout = layout;
        this.isFinished = isFinished;
        this.scoresText = scoresText;
        this.piranha = piranha;
        this.metrics = metrics;
        formerImage = new ImageView(mainActivity);
        myHandler = new Handler();
        soundPlay = new MySoundPlay(mainActivity);
        random = new Random();
        imageViewFactory = ImageViewFactory.getImageViewFactory();
        if(mode.equals(PrimitiveImages.GameMode.EVADE)) {
            tempLife = new CopyOnWriteArrayList<ImageView>();
            piranhaLife = new CopyOnWriteArrayList<ImageView>();
            addLife();
        }
    }

    public GameThread(PrimitiveImages.GameMode mode,
                      Activity activity,
                      RelativeLayout layout,
                      boolean isFinished,
                      DisplayMetrics metrics){
        this(mode, activity, layout, isFinished, null, null, metrics);
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
        while(!isFinished){
            try {
                if(mode.equals(PrimitiveImages.GameMode.TIMEATTACK)){
                    Thread.sleep(600);
                }
                else if (mode.equals(PrimitiveImages.GameMode.EVADE)){
                    Thread.sleep(500);
                }
                else if  (mode.equals(PrimitiveImages.GameMode.MENU)){
                    Thread.sleep(300);
                }
            } catch (InterruptedException e) {}

            int randomNumber = random.nextInt(100);
            if(mode.equals(PrimitiveImages.GameMode.EVADE)){
                if(randomNumber < 3){
                    imageId = imageViewFactory.createImageView(PrimitiveImages.ImageScore.BADDY);
                    layoutParams = imageViewFactory.setLayoutParams(ImageViewFactory.ImageSize.HUGE);
                } else if(randomNumber > 95){
                    imageId = imageViewFactory.createImageView(PrimitiveImages.ImageScore.QUESTIONMARK);
                    layoutParams = imageViewFactory.setLayoutParams(ImageViewFactory.ImageSize.REGULAR);
                } else if(questionMarkBonus == 1){
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
                    questionMarkBonus = 0;
                } else {
                    imageId = imageViewFactory.createImageView(PrimitiveImages.ImageScore.BADDY);
                    layoutParams = imageViewFactory.setLayoutParams(ImageViewFactory.ImageSize.REGULAR);
                }
            }
            else if(mode.equals(PrimitiveImages.GameMode.TIMEATTACK) ||
                    mode.equals(PrimitiveImages.GameMode.MENU)) {
                if (randomNumber < 3) {
                    imageId = imageViewFactory.createImageView(PrimitiveImages.ImageScore.BADDY);
                    layoutParams = imageViewFactory.setLayoutParams(ImageViewFactory.ImageSize.HUGE);
                    //soundPlay.playDropSound();
                } else if (sameItemEatenBonus >= 3 && sameItemEatenBonus < 7) {
                    imageId = imageViewFactory.createImageView(PrimitiveImages.ImageScore.BLINKINGSTAR);
                    layoutParams = imageViewFactory.setLayoutParams(ImageViewFactory.ImageSize.SPECIAL);
                    sameItemEatenBonus++;
                } else if (questionMarkBonus > 0 && questionMarkBonus < 4) {
                    imageId = imageViewFactory.createImageView(PrimitiveImages.ImageScore.GOLDENMUSHROOM);
                    layoutParams = imageViewFactory.setLayoutParams(ImageViewFactory.ImageSize.SPECIAL);
                    questionMarkBonus++;
                } else {
                    imageId = imageViewFactory.createRegularImageView(
                            PrimitiveImages.ImageType.REGULARIMAGE);
                    layoutParams = imageViewFactory.setLayoutParams(ImageViewFactory.ImageSize.REGULAR);
                }
            }
            final ImageView currentImage = new ImageView(mainActivity);
            setDropImageView(currentImage, layoutParams);

            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    currentImage.setVisibility(View.VISIBLE);
                    layout.addView(currentImage);
                    currentImage.animate().y(metrics.heightPixels).setDuration(4000).rotation(360);
                    if (!mode.equals(PrimitiveImages.GameMode.MENU)) {
                        currentImage.animate().setUpdateListener(new MyUpdateListener(
                                currentImage,
                                piranhaLife));
                    }
                }
            });
        }
    }

    public static void endThread(){
        isFinished = true;
    }

    public void setDropImageView(ImageView iv, RelativeLayout.LayoutParams params){
        iv.setTag(imageId);
        iv.setImageResource(imageId);
        iv.setLayoutParams(params);
        Double width = metrics.widthPixels/1.2;
        iv.setX(random.nextInt(width.intValue() - iv.getWidth()));
        iv.setY(0.0f);
    }

    private class MyUpdateListener implements ValueAnimator.AnimatorUpdateListener {
        private ImageView currentImage;
        private CopyOnWriteArrayList<ImageView> piranhaLife;

        public MyUpdateListener(ImageView iv, CopyOnWriteArrayList<ImageView> piranhaLife){
            currentImage = iv;
            this.piranhaLife = piranhaLife;
        }
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            if (currentImage.getY() <= piranha.getY()) {
                final Rect piranhaRect = new Rect();
                final Rect currentImageRect = new Rect();
                piranha.getGlobalVisibleRect(piranhaRect);
                currentImage.getGlobalVisibleRect(currentImageRect);
                if (piranhaRect.intersect(currentImageRect)) {
                    if (imageViewFactory.getScoreTypeById((Integer) currentImage.getTag()).equals
                            (PrimitiveImages.ImageScore.QUESTIONMARK)) {
                        questionMarkBonus = 1;
                    }
                    if(mode.equals(PrimitiveImages.GameMode.TIMEATTACK)) {
                          soundPlay.playBiteSound();
                          if (sameItemEatenBonus >= 7) {
                            sameItemEatenBonus = 0;
                        } else if(!imageViewFactory.getScoreTypeById
                                  ((Integer)currentImage.getTag()).equals
                                  (PrimitiveImages.ImageScore.BLINKINGSTAR)&&
                                  currentImage.getTag().equals(formerImage.getTag())) {
                              sameItemEatenBonus++;
                        }
                        formerImage = currentImage;
                        myScore += imageViewFactory.getScoreById((Integer) currentImage.getTag());
                    }
                    else if(mode.equals(PrimitiveImages.GameMode.EVADE)){
                        if(piranhaLife.size()>0) {
                            if (questionMarkBonus != 1) {
                                ImageView tempImageView = piranhaLife.remove(piranhaLife.size() - 1);
                                tempLife.add(tempImageView);
                                tempImageView.setVisibility(View.INVISIBLE);
                                blinkingAnimation(piranha);
                            }
                        }
                        else {
                            isFinished = true;
                            EvadeMode.showEndLayout();
                        }
                    }
                    currentImage.setVisibility(View.GONE);
                    layout.removeView(currentImage);
                    scoresText.setText(String.valueOf(myScore));
                }
            }
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
}
