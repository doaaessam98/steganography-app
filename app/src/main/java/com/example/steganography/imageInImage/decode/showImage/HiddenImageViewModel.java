package com.example.steganography.imageInImage.decode.showImage;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.example.steganography.base.BaseViewModel;

public class HiddenImageViewModel extends BaseViewModel {


    public ObservableField<Boolean> progress_bar = new ObservableField<>(false);

    public HiddenImageViewModel(@NonNull Application application) {
        super(application);
    }
}
