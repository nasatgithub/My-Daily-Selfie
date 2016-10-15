package com.androidapp.nasir.mydailyselfie;

import android.graphics.Bitmap;

/**
 * Created by NasirAhmed on 16-Jul-15.
 */
public class ImageItem {
    private String imageId;
    private Bitmap image;
    public ImageItem(String imageId,Bitmap image){
        this.imageId=imageId;
        this.image=image;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
