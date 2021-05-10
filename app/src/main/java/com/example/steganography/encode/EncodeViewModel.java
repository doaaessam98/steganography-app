package com.example.steganography.encode;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ayush.imagesteganographylibrary.Text.ImageSteganography;
import com.example.steganography.base.BaseViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class EncodeViewModel extends BaseViewModel<EncodeActivityNavigator> {

    private static final int PICK_IMAGE = 100;
    public ImageView imageView;
    public ObservableField<Uri> imageUrl = new ObservableField<>();
    public ObservableField<String> user_message = new ObservableField<>("");
    public ObservableField<String> message_password = new ObservableField<>();
    public Bitmap encoded_image;
    public Uri selectedImage;
    ImageSteganography imageSteganography;
    private ProgressDialog save;
    private Bitmap original_image;

    public EncodeViewModel(@NonNull Application application) {
        super(application);


    }


    public void isPermissionGranted() {

        //return ContextCompat.checkSelfPermission(getApplication(),Manifest.permission.READ_EXTERNAL_STORAGE)
        //  ==PackageManager.PERMISSION_GRANTED;

    }

    public void goToGallery() {

        navigator.openGallery();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data, Activity activity) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PICK_IMAGE:
                    selectedImage = data.getData();
                    try {
                        original_image = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), selectedImage);

                        imageUrl.set(selectedImage);


                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                    break;
            }
        }
    }

    public void onClick() {
        final Bitmap imgToSave = encoded_image;
        if (imgToSave != null) {


            Thread PerformEncoding = new Thread(new Runnable() {
                @Override
                public void run() {
                    saveImage(imgToSave);
                }
            });
            save = new ProgressDialog(getApplication());
            save.setMessage("Saving, Please Wait...");
            save.setTitle("Saving Image");
            save.setIndeterminate(false);
            save.setCancelable(false);
            save.show();
            PerformEncoding.start();
        } else {
            Log.e("error", " image =null");
        }
    }


    public void encodedTextInImage() {

        Log.e("dddddd", "in  encode" + user_message.get());
        Log.e("dddddd", "in  encode" + message_password.get());

        if (imageUrl != null) {
            if (user_message.get() != null && message_password.get() != null) {
                imageSteganography = new ImageSteganography(user_message.get().toString(),
                        message_password.get().toString(),
                        original_image);
                executeEncode();
            }

        } else {
            Log.e("dddddd", "image null");

        }

    }

    private void executeEncode() {
        navigator.encodeText();
    }


    public void saveImage(Bitmap bitmap) {

        OutputStream output;
        // Find the SD Card path
        File filepath = Environment.getExternalStorageDirectory();

        // Create a new folder in SD Card
        File dir = new File(filepath.getAbsolutePath()
                + "/WhatSappIMG/");
        dir.mkdirs();

        // Retrieve the image from the res folder
        //  BitmapDrawable drawable = (BitmapDrawable) encoded_image.getDrawable();
        bitmap = encoded_image;

        // Create a name for the saved image
        File file = new File(dir, "Wallpaper.jpg");
        try {
            output = new FileOutputStream(file);

            // Compress into png format image from 0% - 100%
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();
            String url = MediaStore.Images.Media.insertImage(getApplication().getContentResolver(), bitmap,
                    "Wallpaper.jpg", null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
}




