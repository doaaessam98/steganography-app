package com.example.steganography.home.encode;

import android.app.Application;

import androidx.annotation.NonNull;

import com.example.steganography.base.BaseViewModel;

public class EcodeViewModel extends BaseViewModel<EncodeNavigator> {


    public EcodeViewModel(@NonNull Application application) {
        super(application);
    }

    public void openEncodeTextActivity() {

        navigator.openEncodeTextActivity();



    }

    public void openEncodeImageActivity() {

        navigator.openEncodeImageActivity();


    }
}
