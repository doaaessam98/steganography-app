package com.example.steganography.imageInImage.encode;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.steganography.R;
import com.example.steganography.base.BaseActivity;
import com.example.steganography.databinding.ActivityEncodeImageBinding;

public class EncodeImageActivity extends BaseActivity<EncodeImageViewModel, ActivityEncodeImageBinding> implements navigator {

    private static final int PICK_IMAGE = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encode_image);
    }

    @Override
    protected ActivityEncodeImageBinding getDataBinding() {
        return DataBindingUtil.setContentView(this, R.layout.activity_encode_image);
    }

    @Override
    protected EncodeImageViewModel initViewModel() {
        return new ViewModelProvider(this).get(EncodeImageViewModel.class);
    }

    @Override
    public void openGallery() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }
}
