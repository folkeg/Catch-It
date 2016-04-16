package com.example.rick.catchit;

import android.widget.RelativeLayout;

import java.util.Random;

public class StageSetup {
    private static int stage = 0;
    private int sleepTime = 0;
    private int imageId = 0;
    private int countDownTime = 0;
    private int targetFood = 0;
    private int targetScore = 0;
    private int targetEvadeBaddy = 0;
    private int duration = 0;
    private String task;
    private Random random;
    private StageMode stageMode;
    private ImageViewFactory imageViewFactory;
    private RelativeLayout.LayoutParams layoutParams;

    public enum StageMode {
        COUNTDOWN_MODE(1),
        LIFE_MODE(2),
        AMOUNT_TARGET_MODE(3);

        private int type;

        StageMode(int type){
            this.type = type;
        }
        public int getType(){
            return type;
        }
    }

    public StageSetup(){
        random = new Random();
        imageViewFactory = ImageViewFactory.getImageViewFactory();
        setMode();
        setSleepTime();
        setCountDownTime(); // for mode countdown time
        setTargetFood();  // for mode amount target
        setTargetScore(); // for mode countdown time
        setDuration();
        setTargetEvadeBaddy();
        setTaskForStage();
    }

    public void setSleepTime(){
        if(stageMode.getType()==1){
            if(stage<9) {
                sleepTime = 700;
            }
            else if(stage>=9 && stage<18){
                sleepTime = 500;
            }
            else {
                sleepTime = 400;
            }
        }
        else if(stageMode.getType()==2){
            if(stage<9) {
                sleepTime = 350;
            }
            else if(stage>=9 && stage<18){
                sleepTime = 300;
            }
            else {
                sleepTime = 250;
            }
        }
        else if(stageMode.getType()==3){
            if(stage<9) {
                sleepTime = 700;
            }
            else if(stage>=9 && stage<18){
                sleepTime = 600;
            }
            else {
                sleepTime = 550;
            }
        }
    }

    public int getSleepTime(){
        return sleepTime;
    }

    public void setMode(){
        if (stage%9==0 || stage%9==1 ||stage%9==2 ){
            stageMode = StageMode.COUNTDOWN_MODE;
        }
        else if(stage%9==3 || stage%9==4 ||stage%9==5){
            stageMode = StageMode.LIFE_MODE;
        }
        else if(stage%9==6 || stage%9==7 ||stage%9==8){
            stageMode = StageMode.AMOUNT_TARGET_MODE;
        }
    }

    public int getMode(){
        return stageMode.getType();
    }

    public void setCountDownTime(){
        if(getMode()==1){
            if(stage%9==0){
                countDownTime=20000;
            }
            else if(stage%9==1){
                countDownTime=30000;
            }
            else if(stage%9==2){
                countDownTime=40000;
            }
        }
    }

    public int getCountDownTime(){
        return countDownTime;
    }

    public void setTargetFood(){
        if(getMode()==3){
            if(stage%9==6){
                targetFood=15;
            }
            else if(stage%9==7){
                targetFood=20;
            }
            else if(stage%9==8){
                targetFood=25;
            }
        }
    }

    public int getTargetFood(){
        return targetFood;
    }

    public void setImageId(int questionMarkFlag){
        int randomNumber = random.nextInt(100);
        if(getMode()== 1){
            if(questionMarkFlag>0 && questionMarkFlag<4){
                imageId = imageViewFactory.createImageView(
                        PrimitiveImages.ImageScore.GOLDENMUSHROOM);
            }
            else {
                if (stage < 9) {
                    if (randomNumber > 70) {
                        imageId = imageViewFactory.createImageView(
                                PrimitiveImages.ImageScore.BADDY);
                    } else {
                        setSpecialImageId(randomNumber);
                    }
                } else if (stage >= 9 || stage < 18) {
                    if (randomNumber > 65) {
                        imageId = imageViewFactory.createImageView(
                                PrimitiveImages.ImageScore.BADDY);
                    } else {
                        setSpecialImageId(randomNumber);
                    }
                } else {
                    if (randomNumber > 60) {
                        imageId = imageViewFactory.createImageView(
                                PrimitiveImages.ImageScore.BADDY);
                    } else {
                        setSpecialImageId(randomNumber);
                    }
                }
            }
        }
        else if(getMode()==2){
            if(randomNumber>95){
                imageId = imageViewFactory.createImageView(
                        PrimitiveImages.ImageScore.QUESTIONMARK);
            }
            else {
                imageId = imageViewFactory.createImageView(
                        PrimitiveImages.ImageScore.BADDY);
            }
        }
        else if(getMode()==3){
            imageId = imageViewFactory.createImageView(
                    PrimitiveImages.ImageScore.SMALLEDIBLE);
        }
    }

    public int getImageId(){
        return imageId;
    }

    public void setLayoutParams(){
        int randomNumber = random.nextInt(100);
        if(imageId==imageViewFactory.createImageView(
                PrimitiveImages.ImageScore.BADDY)){
            if(randomNumber>95) {
                layoutParams = imageViewFactory.setLayoutParams(
                        ImageViewFactory.ImageSize.HUGE);
            }
            else {
                layoutParams = imageViewFactory.setLayoutParams(
                        ImageViewFactory.ImageSize.REGULAR);
            }
        }
        else if(imageId==imageViewFactory.createImageView(
                PrimitiveImages.ImageScore.BLINKINGSTAR)){
            layoutParams = imageViewFactory.setLayoutParams(
                    ImageViewFactory.ImageSize.SPECIAL);
        }
        else if(imageId==imageViewFactory.createImageView(
                PrimitiveImages.ImageScore.GOLDENMUSHROOM)){
            layoutParams = imageViewFactory.setLayoutParams(
                    ImageViewFactory.ImageSize.SPECIAL);
        }
        else {
            layoutParams = imageViewFactory.setLayoutParams(
                    ImageViewFactory.ImageSize.REGULAR);
        }
    }

    public RelativeLayout.LayoutParams getLayoutParams(){
        return layoutParams;
    }

    public void setTargetScore(){
        if(getMode()==1){
            if(countDownTime==20000){
                if(stage<9) {
                    targetScore = 100;
                }
                else if(stage>=9 && stage<18){
                    targetScore = 120;
                }
                else{
                    targetScore = 140;
                }
            }
            else if(countDownTime==30000){
                if(stage<9) {
                    targetScore = 150;
                }
                else if(stage>=9 && stage<18){
                    targetScore = 170;
                }
                else{
                    targetScore = 200;
                }
            }
            else if(countDownTime==40000){
                if(stage<9) {
                    targetScore = 200;
                }
                else if(stage>=9 && stage<18){
                    targetScore = 250;
                }
                else{
                    targetScore = 300;
                }
            }
        }
    }

    public int getTargetScore(){
        return targetScore;
    }

    public static void setStage(int newStage){
        stage = newStage;
    }

    public void setDuration(){
        if(getMode()== 1){
            if(stage<9) {
                duration = 4000;
            }
            else if(stage>=9 && stage<18){
                duration = 3700;
            }
            else {
                duration = 3500;
            }
        }
        else if(getMode()==2){
            if(stage<9) {
                duration = 3500;
            }
            else if(stage>=9 && stage<18){
                duration = 3200;
            }
            else {
                duration = 3000;
            }
        }
        else if(getMode()==3){
            if(stage<9) {
                duration = 4000;
            }
            else if(stage>=9 && stage<18){
                duration = 3700;
            }
            else {
                duration = 3400;
            }
        }
    }

    public int getDuration(){
        return duration;
    }

    public void setTargetEvadeBaddy(){
        if(getMode()==2){
            if(stage==3){
                targetEvadeBaddy =15;
            }
            else if(stage==4){
                targetEvadeBaddy=20;
            }
            else if(stage==5){
                targetEvadeBaddy=25;
            }
            else if(stage>=12 && stage<18){
                targetEvadeBaddy =30;
            }
            else {
                targetEvadeBaddy =40;
            }
        }
    }

    public int getTargetEvadeBaddy(){
        return targetEvadeBaddy;
    }

    public void setTaskForStage(){
        if(getMode()==1){
            task = "FINISH "+getTargetScore()+" POINTS!";
        }
        else if(getMode()==2){
            task = "EVADE "+getTargetEvadeBaddy()+" BADDYS!";
        }
        else if(getMode()==3){
            task = "DO NOT LET "+getTargetFood()+" FOOD ESCAPE!";
        }
    }

    public String getTaskForStage(){
        return task;
    }

    private void setSpecialImageId(int randomNumber){
        if (randomNumber < 5) {
            imageId = imageViewFactory.createImageView(
                    PrimitiveImages.ImageScore.QUESTIONMARK);
        } else if (randomNumber < 7 && randomNumber >= 5) {
            imageId = imageViewFactory.createImageView(
                    PrimitiveImages.ImageScore.BLINKINGSTAR);
        } else {
            imageId = imageViewFactory.createImageView(
                    PrimitiveImages.ImageScore.SMALLEDIBLE);
        }
    }
}
