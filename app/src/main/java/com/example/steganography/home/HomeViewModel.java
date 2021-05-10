package com.example.steganography.home;

import android.app.Application;

import androidx.annotation.NonNull;

import com.example.steganography.base.BaseViewModel;

public class HomeViewModel extends BaseViewModel<HomeNavigator> {
    public HomeViewModel(@NonNull Application application) {
        super(application);
    }
}
