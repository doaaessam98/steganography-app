package com.example.steganography.imageInImage.decode;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextDecodingCallback;
import com.ayush.imagesteganographylibrary.Text.ImageSteganography;
import com.bumptech.glide.Glide;
import com.example.steganography.R;
import com.example.steganography.base.BaseActivity;
import com.example.steganography.databinding.ActivityDecodeImageBinding;

public class DecodeImageActivity extends BaseActivity<DecodeImageViewModel, ActivityDecodeImageBinding>
        implements DecodeImageNavigator {

    private static final int PICK_IMAGE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        databinding.setVm(viewModel);
        viewModel.navigator = this;
        databinding.decodedImageInImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.startDecode(DecodeImageActivity.this, new TextDecodingCallback() {


                    @Override
                    public void onStartTextEncoding() {

                    }

                    @Override
                    public void onCompleteTextEncoding(ImageSteganography result) {

                        Log.e("on complete", "password" + result.getSecret_key());
                        Log.e("on complete", "message" + Uri.parse(result.getMessage()));

                        if (result != null) {
                            Log.e("not null", "message" + result.getMessage().toString());

                            // databinding.resultImage.setImageURI(Uri.parse(result.toString()));
                           /* GlideApp.with(DecodeImageActivity.this )
                                    .load(result)
                                    .into(databinding.resultImage);*/
                            Glide.with(DecodeImageActivity.this).load(Uri.parse(result.getMessage().toString())).into(databinding.resultImage);

                            Log.e("llll", "lll" + result.getMessage());
                            // Uri uri=Uri.parse(result.toString());
                            //databinding.resultImage.setImageURI(Uri.parse(result.toString()));
                            // databinding.resultImage.setImageURI();
                            Log.e("not null", "message" + result.getMessage().toString());
                            Log.e("llll", "lll" + result.getMessage());

                            Log.e("not null", "password" + result.getSecret_key().toString());
                            Log.e("llll", "lll" + result.isSecretKeyWrong());
                            if (!result.isDecoded()) {
                            }
                            // databinding.whetherDecoded.setText("No message found");
                            else {
                                if (!result.isSecretKeyWrong()) {
                                    // databinding.whetherDecoded.setText("Decoded");
                                    //databinding.decodeMessage.setText("" + result.getMessage());
                                } else {
                                    //databinding.whetherDecoded.setText("Wrong secret key");
                                }
                            }
                        } else {
                            // databinding.whetherDecoded.setText("Select Image First");
                        }

                    }
                });

            }
        });
    }


    @Override
    protected ActivityDecodeImageBinding getDataBinding() {
        return DataBindingUtil.setContentView(this, R.layout.activity_decode_image);

    }

    @Override
    protected DecodeImageViewModel initViewModel() {
        return new ViewModelProvider(this).get(DecodeImageViewModel.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.onActivityResult(requestCode, resultCode, data, this);

    }


    @Override
    public void PickImageToDecode() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

    }
}
