package com.example.rick.catchit;

import android.widget.RelativeLayout;

public class ImageViewFactory {
    private static ImageViewFactory imageViewFactory = null;
    private ImageStorage imageStore;
    public enum ImageSize {
        HUGE,
        REGULAR,
        SPECIAL
    }
    protected ImageViewFactory(){
        imageStore = new ImageStorage();
    }

    public static ImageViewFactory getImageViewFactory(){
        if(imageViewFactory ==null){
            imageViewFactory = new ImageViewFactory();
        }
        return imageViewFactory;
    }
    public int createImageView(PrimitiveImages.ImageScore imageScore){
        return imageStore.getRandomImageIdByImageScoreType(imageScore);
    }
    public int createRegularImageView(PrimitiveImages.ImageType imageType){
        return imageStore.getRegularImageIdByImageType(imageType);
    }
    public RelativeLayout.LayoutParams setLayoutParams(ImageSize imageSize){
        if(imageSize.equals(ImageSize.HUGE)){
            return new RelativeLayout.LayoutParams(300, 300);
        }
        else if(imageSize.equals(ImageSize.SPECIAL)){
            return new RelativeLayout.LayoutParams(180, 180);
        }
        else if(imageSize.equals(ImageSize.REGULAR)){
            return new RelativeLayout.LayoutParams(120, 120);
        }
        throw new IllegalArgumentException("");
    }

    public PrimitiveImages.ImageScore getScoreTypeById(Integer id){
        return imageStore.getScoreTypeByImageId(id);
    }

    public int getScoreById(Integer id){
        return imageStore.getScoreByImageId(id);
    }
}
