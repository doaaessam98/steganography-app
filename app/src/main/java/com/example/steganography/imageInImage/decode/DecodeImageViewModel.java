package com.example.steganography.imageInImage.decode;

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

public class DecodeImageViewModel extends BaseViewModel<DecodeImageNavigator> {
    private static final int PICK_IMAGE = 1;
    public ObservableField<Uri> decodeImage = new ObservableField<>();
    public ObservableField<String> password = new ObservableField<>("");
    public ImageSteganography imageSteganography;
    Uri selectedImage;
    Bitmap original_image;
    Bitmap result;
    String error_message;

    public DecodeImageViewModel(@NonNull Application application) {
        super(application);
    }

    public void openGalleryToPickImage() {

        navigator.PickImageToDecode();
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

    public void startDecode(Activity activity, TextDecodingCallback textDecodingCallback) {


        if (selectedImage != null) {

            if (password.get().toString() != null) {

                Log.e("start decode", "hh" + password.get().toString());

         /* stegano  s=new stegano(original_image);
         s.reveal();
           result = s.getResultImage();*/
                imageSteganography = new ImageSteganography(password.get().toString(), original_image);
                TextDecoding textDecoding = new TextDecoding(activity, textDecodingCallback);
                textDecoding.execute(imageSteganography);
                Log.e("null", "starat decode");


                // }
            } else {
                error_message = "password is null";
                showErrorMessage();

            }


        } else {

            error_message = "password is null";
            showErrorMessage();
        }


    }

    private void showErrorMessage() {
        navigator.showErrorMessage();
    }
}

