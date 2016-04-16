package com.example.rick.catchit;

import java.util.ArrayList;
import java.util.Random;

public class ImageStorage {
    private ArrayList<PrimitiveImages> imageStorage;
    public ImageStorage(){
        imageStorage = new ArrayList<PrimitiveImages>();
        imageStorage.add(new PrimitiveImages(R.drawable.badegg,
                PrimitiveImages.ImageScore.BADDY, PrimitiveImages.ImageType.REGULARIMAGE));
        imageStorage.add(new PrimitiveImages(R.drawable.bluemushroom,
                PrimitiveImages.ImageScore.SMALLEDIBLE,PrimitiveImages.ImageType.REGULARIMAGE));
        imageStorage.add(new PrimitiveImages(R.drawable.boss,
                PrimitiveImages.ImageScore.BADDY,PrimitiveImages.ImageType.REGULARIMAGE));
        imageStorage.add(new PrimitiveImages(R.drawable.fish,
                PrimitiveImages.ImageScore.BADDY, PrimitiveImages.ImageType.REGULARIMAGE));
        imageStorage.add(new PrimitiveImages(R.drawable.flower,
                PrimitiveImages.ImageScore.SMALLEDIBLE, PrimitiveImages.ImageType.REGULARIMAGE));
        imageStorage.add(new PrimitiveImages(R.drawable.ghost,
                PrimitiveImages.ImageScore.BADDY, PrimitiveImages.ImageType.REGULARIMAGE));
        imageStorage.add(new PrimitiveImages(R.drawable.missle,
                PrimitiveImages.ImageScore.BADDY, PrimitiveImages.ImageType.REGULARIMAGE));
        imageStorage.add(new PrimitiveImages(R.drawable.redmushroom,
                PrimitiveImages.ImageScore.SMALLEDIBLE, PrimitiveImages.ImageType.REGULARIMAGE));
        imageStorage.add(new PrimitiveImages(R.drawable.redturtle,
                PrimitiveImages.ImageScore.BADDY, PrimitiveImages.ImageType.REGULARIMAGE));
        imageStorage.add(new PrimitiveImages(R.drawable.smallboss,
                PrimitiveImages.ImageScore.BADDY, PrimitiveImages.ImageType.REGULARIMAGE));
        imageStorage.add(new PrimitiveImages(R.drawable.stingfish,
                PrimitiveImages.ImageScore.BADDY, PrimitiveImages.ImageType.REGULARIMAGE));
        imageStorage.add(new PrimitiveImages(R.drawable.greenturtle,
                PrimitiveImages.ImageScore.BADDY, PrimitiveImages.ImageType.REGULARIMAGE));
        imageStorage.add(new PrimitiveImages(R.drawable.mushroombee,
                PrimitiveImages.ImageScore.SMALLEDIBLE, PrimitiveImages.ImageType.REGULARIMAGE));
        imageStorage.add(new PrimitiveImages(R.drawable.octopus,
                PrimitiveImages.ImageScore.BADDY, PrimitiveImages.ImageType.REGULARIMAGE));
        imageStorage.add(new PrimitiveImages(R.drawable.chainchompicon,
                PrimitiveImages.ImageScore.BADDY, PrimitiveImages.ImageType.REGULARIMAGE));
        imageStorage.add(new PrimitiveImages(R.drawable.flowerice,
                PrimitiveImages.ImageScore.SMALLEDIBLE, PrimitiveImages.ImageType.REGULARIMAGE));
        imageStorage.add(new PrimitiveImages(R.drawable.question,
                PrimitiveImages.ImageScore.QUESTIONMARK, PrimitiveImages.ImageType.REGULARIMAGE));
        imageStorage.add(new PrimitiveImages(R.drawable.mushroomgolden,
                PrimitiveImages.ImageScore.GOLDENMUSHROOM, PrimitiveImages.ImageType.SPECIALIMAGE));
        imageStorage.add(new PrimitiveImages(R.drawable.blinkingstar,
                PrimitiveImages.ImageScore.BLINKINGSTAR, PrimitiveImages.ImageType.SPECIALIMAGE));
        imageStorage.add(new PrimitiveImages(R.drawable.piranhaleft,
                PrimitiveImages.ImageScore.PIRANHALEFT, PrimitiveImages.ImageType.PIRANHAREVERSE));
        imageStorage.add(new PrimitiveImages(R.drawable.piranharight,
                PrimitiveImages.ImageScore.PIRANHARIGHT, PrimitiveImages.ImageType.PIRANHA));
    }

    public int getScoreByImageId(int imageId){
        for (PrimitiveImages primitiveImages : imageStorage){
            if(primitiveImages.getImageId() == imageId){
                return primitiveImages.getImageScore().getScore();
            }
        }
        throw new IllegalArgumentException("");
    }

    public PrimitiveImages.ImageScore getScoreTypeByImageId(int imageId){
        for (PrimitiveImages primitiveImages : imageStorage){
            if(primitiveImages.getImageId()==imageId){
                return primitiveImages.getImageScore();
            }
        }
        throw new IllegalArgumentException("");
    }

    public PrimitiveImages.ImageType getImageTypeByImageId(int imageId){
        for (PrimitiveImages primitiveImages : imageStorage){
            if(primitiveImages.getImageId()==imageId){
                return primitiveImages.getImageType();
            }
        }
        throw new IllegalArgumentException("");
    }

    public int getRegularImageIdByImageType(PrimitiveImages.ImageType imageType){
        Random random = new Random();
        PrimitiveImages primitiveImages = imageStorage.get(random.nextInt(imageStorage.size() - 4));
       /* while (!primitiveImages.getImageType().equals(imageType)){
            primitiveImages = imageStorage.get(15 + random.nextInt(6));//imageStorage.get(random.nextInt(imageStorage.size()));
        }*/
        return primitiveImages.getImageId();
    }

    public int getSpecificImageIdByImageSocreType(PrimitiveImages.ImageScore imageScore){
        for (PrimitiveImages primitiveImages : imageStorage){
            if(primitiveImages.getImageScore().equals(imageScore)){
                return primitiveImages.getImageId();
            }
        }
        throw new IllegalArgumentException("");
    }

    public int getRandomImageIdByImageScoreType(PrimitiveImages.ImageScore imageScore){
        Random random = new Random();
        PrimitiveImages primitiveImages = imageStorage.get(random.nextInt(imageStorage.size()));
        while (!primitiveImages.getImageScore().equals(imageScore)){
            primitiveImages = imageStorage.get(random.nextInt(imageStorage.size()));
        }
        return primitiveImages.getImageId();
    }
}
