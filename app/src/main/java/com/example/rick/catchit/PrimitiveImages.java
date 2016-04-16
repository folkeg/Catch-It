package com.example.rick.catchit;

public class PrimitiveImages {
    private int imageId = 0;
    private ImageScore imageScore;
    private ImageType imageType;
    public enum GameMode {
        MENU,
        TIMEATTACK,
        EVADE;
    }
    public  enum ImageType {
        REGULARIMAGE,
        SPECIALIMAGE,
        PIRANHAREVERSE,
        PIRANHA;
    }

    public enum ImageScore {
        BADDY(-10),
        SMALLEDIBLE(10),
        GOLDENMUSHROOM(20),
        BLINKINGSTAR(30),
        QUESTIONMARK(0),
        PIRANHARIGHT(-1),
        PIRANHALEFT(-1);

        private final int score;

        ImageScore(int score){
            this.score = score;
        }

        public int getScore(){
            return score;
        }
    }

    public PrimitiveImages(int imageId, ImageScore imageScore, ImageType imageType){
        this.imageId = imageId;
        this.imageScore = imageScore;
        this.imageType = imageType;
    }

    public int getImageId(){
        return imageId;
    }

    public ImageScore getImageScore(){
        return imageScore;
    }

    public ImageType getImageType(){
        return imageType;
    }
}
