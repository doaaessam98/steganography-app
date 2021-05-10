package com.example.steganography.encode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextEncodingCallback;
import com.ayush.imagesteganographylibrary.Text.ImageSteganography;
import com.ayush.imagesteganographylibrary.Text.TextEncoding;
import com.example.steganography.R;
import com.example.steganography.base.BaseActivity;
import com.example.steganography.databinding.ActivityEncodeBinding;

public class EncodeActivity extends BaseActivity<EncodeViewModel, ActivityEncodeBinding> implements EncodeActivityNavigator, TextEncodingCallback {
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    ImageView imageView;
    ActivityEncodeBinding databinding;
    private Bitmap encoded_image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databinding = DataBindingUtil.setContentView(this, R.layout.activity_encode);
        databinding.setVm(viewModel);
        viewModel.navigator = this;


    }

    @Override
    protected EncodeViewModel initViewModel() {
        return new ViewModelProvider(this).get(EncodeViewModel.class);
    }

    @Override
    protected int getLayOut() {
        return R.layout.activity_encode;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.onActivityResult(requestCode, resultCode, data, this);
    }


    @Override
    public void openGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICK_IMAGE);

    }

    @Override
    public void encodeText() {
        Log.e("dddddd", "in maim encode");
        TextEncoding textEncoding = new TextEncoding(EncodeActivity.this, EncodeActivity.this);
        textEncoding.execute(viewModel.imageSteganography);
        Log.e("dddddd", "ok encode");

    }

    @Override
    public void onStartTextEncoding() {

    }

    @Override
    public void onCompleteTextEncoding(ImageSteganography result) {

        if (result != null && result.isEncoded()) {
            viewModel.encoded_image = result.getEncoded_image();
            // whether_encoded.setText("Encoded");
            databinding.image.setImageBitmap(encoded_image);
            Toast.makeText(this, "encoded", Toast.LENGTH_LONG).show();
        }
    }

}