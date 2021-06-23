package com.example.steganography.home.decodeHomeFragment;

import android.app.Application;

import androidx.annotation.NonNull;

import com.example.steganography.base.BaseViewModel;

public class DecodeHomeFragmentViewModel extends BaseViewModel<DecodeHomeFragmentNavigator> {
    public DecodeHomeFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public void goToDecodeTextActivity() {
        navigator.openDecodeTextActivity();


    }

    public void goToDecodeImageActivity() {
        navigator.OpenDecodeImageActivity();


    }
}
