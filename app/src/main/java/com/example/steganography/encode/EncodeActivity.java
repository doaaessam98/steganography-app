package com.example.steganography.encode;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import com.example.steganography.R;
import com.example.steganography.base.BaseActivity;
import com.example.steganography.databinding.ActivityEncodeBinding;

public class EncodeActivity extends BaseActivity<EncodeViewModel, ActivityEncodeBinding> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encode);
    }

    @Override
    protected EncodeViewModel initViewModel() {
        EncodeViewModel model;
        model = new ViewModelProvider(this).get(EncodeViewModel.class);

        return model;
    }

    @Override
    protected int getLayOut() {
        return R.layout.activity_encode;
    }
}
