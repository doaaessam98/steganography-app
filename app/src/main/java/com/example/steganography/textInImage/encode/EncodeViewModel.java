package com.example.steganography.textInImage.encode;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextEncodingCallback;
import com.ayush.imagesteganographylibrary.Text.ImageSteganography;
import com.ayush.imagesteganographylibrary.Text.TextEncoding;
import com.example.steganography.base.BaseViewModel;

import java.io.IOException;

public class EncodeViewModel extends BaseViewModel<EncodeActivityNavigator> {

    private static final int PICK_IMAGE = 100;
    public ImageView imageView;
    public ObservableField<Uri> imageUrl = new ObservableField<>();
    public ObservableField<String> user_message = new ObservableField<>();
    public ObservableField<String> message_password = new ObservableField<>();
    public Bitmap encoded_image;
    public Uri selectedImage;
    ImageSteganography imageSteganography;
    private ProgressDialog save;
    private Bitmap original_image;
    String textEncodeErrorMessage;
    public EncodeViewModel(@NonNull Application application) {
        super(application);


    }


    public void goToGallery() {

        navigator.openGallery();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data, Activity activity) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PICK_IMAGE:
                    selectedImage = data.getData();
                    //imageUrl.postValue(data.getData());
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


    public void encodedTextInImage(Activity activity, TextEncodingCallback textEncodingCallback) {

        if (selectedImage != null) {
            if (user_message.get() != null) {
                if (message_password.get() != null) {
                    imageSteganography = new ImageSteganography(user_message.get().toString(),
                            message_password.get().toString(), original_image);

                    TextEncoding textEncoding = new TextEncoding(activity, textEncodingCallback);
                    textEncoding.execute(imageSteganography);

                } else {
                    textEncodeErrorMessage = "password is empty";
                    showMessage();
                }
            } else {
                textEncodeErrorMessage = "please enter message to encoded";
                showMessage();
            }

        } else {
            textEncodeErrorMessage = "please selected image first";

            showMessage();
        }


    }

    private void showMessage() {
        navigator.showErrorMessage();
    }


}


