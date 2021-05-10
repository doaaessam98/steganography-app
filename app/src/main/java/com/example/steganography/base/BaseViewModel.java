package com.example.steganography.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;


public class BaseViewModel<T> extends AndroidViewModel {
    public T navigator;

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }




    // public T nn= (T) new Object();
}
