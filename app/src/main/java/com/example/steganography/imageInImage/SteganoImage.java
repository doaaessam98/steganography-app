package com.example.steganography.imageInImage;

import android.graphics.Bitmap;

public class SteganoImage {


    private Bitmap imageSource;
    private Bitmap imageToHide;
    private Bitmap imageResult;
    private int width;
    private int height;

  /*  public SteganoImage() {

        this.imageSource = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        this.imageToHide = Bitmap.createBitmap(width, 600, Bitmap.Config.ARGB_8888);
        // this.encrypted_zip = new byte[0];
        this. imageResult=Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        // imageResult = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }
    // Constructor with 2 images (hiding process)
    public SteganoImage(Bitmap sourceImg, Bitmap hiddenImg) {
        width = sourceImg.getWidth();
        height = sourceImg.getHeight();
        this.imageSource=sourceImg;
        // imageSource = sourceImg;
        this.imageToHide=hiddenImg;
        //imageToHide = hiddenImg;
        width = sourceImg.getWidth();
        height = sourceImg.getHeight();

        imageResult=Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
    }



    public static Bitmap HideImage(Bitmap sourceImg,
                                   Bitmap hiddenImg, int hidden_bits)
    {


        int shift = (8 - hidden_bits);
        int visible_mask = 0xFF << hidden_bits;
        int hidden_mask = 0xFF >> shift;
        Bitmap bm_combined =  Bitmap.createBitmap(sourceImg.getWidth(),
                sourceImg.getHeight(), Bitmap.Config.RGB_565);
        for (int x = 0; x < sourceImg.getWidth(); x++)
        {
            for (int y = 0; y < sourceImg.getHeight(); y++)
            {
                Color clr_visible = sourceImg.getColor(x, y);
                Color clr_hidden = hiddenImg.getColor(x, y);
                int r = (clr_visible.RED & visible_mask) +
                        ((clr_hidden.RED >> shift) & hidden_mask);
                int g = (clr_visible.GREEN & visible_mask) +
                        ((clr_hidden.GREEN >> shift) & hidden_mask);
                int b = (clr_visible.BLUE & visible_mask) +
                        ((clr_hidden.BLUE >> shift) & hidden_mask);
                bm_combined.setPixel(x, y,
                        Color.argb(255, r, g, b));
            }
        }
        //Color.FromArgb(255, r, g, b));

        return bm_combined;
    }

    public static Bitmap RecoverImage(Bitmap bm_combined,
                                      int hidden_bits)
    {
        int shift = (8 - hidden_bits);
        int hidden_mask = 0xFF >> shift;
        Bitmap bm_hidden = new Bitmap(bm_combined.get,
                bm_combined.Height);
        for (int x = 0; x < bm_combined.Width; x++)
        {
            for (int y = 0; y < bm_combined.Height; y++)
            {
                Color clr_combined = bm_combined.GetPixel(x, y);
                int r = (clr_combined.R & hidden_mask) << shift;
                int g = (clr_combined.G & hidden_mask) << shift;
                int b = (clr_combined.B & hidden_mask) << shift;
                bm_hidden.SetPixel(x, y, Color.FromArgb(255, r, g, b));
            }
        }
        return bm_hidden;
    }*/
}
