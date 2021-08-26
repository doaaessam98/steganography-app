package com.example.steganography.imageInImage;
//package fr.bastienfaure.stegano;

import android.graphics.Bitmap;

public class stegano {


    private Bitmap imageSource;
    private Bitmap imageToHide;
    private Bitmap imageResult;
    private int width;
    private int height;

    public stegano() {

        this.imageSource = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        this.imageToHide = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        this.imageResult = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    }

    // Constructor with 2 images (hiding process)
    public stegano(Bitmap sourceImg, Bitmap hiddenImg) {
        width = sourceImg.getWidth();
        height = sourceImg.getHeight();
        this.imageSource = sourceImg;
        this.imageToHide = hiddenImg;

        sourceImg = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        hiddenImg = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        imageResult = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    }

    // Constructor with 1 image (revealing process)
    public stegano(Bitmap sourceImg) {
        this(sourceImg, null);
    }

    // Hide the first 3 MSB of pixelB in pixelA

    private int hidePixel(int pixelA, int pixelB) {
        //return pixelA & 0xFFFFF8F8 | (pixelB & 0x0F) ;
        return pixelA & 0xFFF8F8F8 | (pixelB & 0x00E0E0E0) >> 5;

    }

    // Extract the last 3 LSB
    private int revealPixel(int pixel) {
        return (pixel & 0xFF070707) << 5;
    }

    // Hide hiddenImage into coverImage
    public void hide() {
        // int[] oneDMod = Utility.byteArrayToIntArray(imageResult);


        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                imageResult.setPixel(i, j, hidePixel(imageSource.getPixel(i, j), imageToHide.getPixel(i, j)));


            }
        }
    }


    // Reveal hidden image into resultImage
    public void reveal() {

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                imageResult.setPixel(i, j, revealPixel(imageSource.getPixel(i, j)));

            }
        }
    }

    // Return resultImage
    public Bitmap getResultImage() {

        return imageResult;
    }
}













