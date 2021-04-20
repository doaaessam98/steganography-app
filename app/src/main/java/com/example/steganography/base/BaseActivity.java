package com.example.steganography.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

abstract public class BaseActivity<VM extends BaseViewModel, DB extends ViewDataBinding> extends AppCompatActivity {
    VM viewModel;
    DB binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(this, getLayOut());
        viewModel = initViewModel();

    }

    protected abstract VM initViewModel();

    protected abstract int getLayOut();


}