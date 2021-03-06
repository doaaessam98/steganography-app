package com.example.steganography.textInImage.decode;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextDecodingCallback;
import com.ayush.imagesteganographylibrary.Text.ImageSteganography;
import com.ayush.imagesteganographylibrary.Text.TextDecoding;
import com.example.steganography.base.BaseViewModel;

import java.io.IOException;

public class DecodeViewModel extends BaseViewModel<DecodeNavigator> {
    private static final int PICK_IMAGE = 100;

    public ObservableField<Uri> decodeImage = new ObservableField<>();
    public ObservableField<String> secret_key = new ObservableField<>();

    public Uri selectedImage;

    public Bitmap original_image;
    public ImageSteganography imageSteganography;

    public DecodeViewModel(@NonNull Application application) {
        super(application);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data, Activity activity) {

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PICK_IMAGE:
                    selectedImage = data.getData();
                    //imageUrl.postValue(data.getData());
                    try {
                        original_image = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), selectedImage);

                        decodeImage.set(selectedImage);


                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                    break;
            }
        }


    }


    public void GoToOpenGallery() {

        navigator.openGallery();
    }

    public void startDecode(Activity activity, TextDecodingCallback textDecodingCallback) {
        if (secret_key.get() != null) {
            Log.e("ggg", "hh" + secret_key.get());
            if (selectedImage != null) {

                imageSteganography = new ImageSteganography(secret_key.get().toString(),
                        original_image);
                TextDecoding textDecoding = new TextDecoding(activity, textDecodingCallback);
                textDecoding.execute(imageSteganography);

            }
        } else {
            Log.e("null", "passwors=null");
        }


    }
}