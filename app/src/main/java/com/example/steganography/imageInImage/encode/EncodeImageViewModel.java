package com.example.steganography.imageInImage.encode;

import android.app.Application;

import androidx.annotation.NonNull;

import com.example.steganography.base.BaseViewModel;

public class EncodeImageViewModel extends BaseViewModel<navigator> {


    public EncodeImageViewModel(@NonNull Application application) {
        super(application);
    }

    public void startEncodeImage() {


    }

    public void openGallery() {
        navigator.openGallery();

    }

}
