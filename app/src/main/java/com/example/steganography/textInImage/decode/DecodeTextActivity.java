package com.example.steganography.textInImage.decode;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextDecodingCallback;
import com.ayush.imagesteganographylibrary.Text.ImageSteganography;
import com.example.steganography.R;
import com.example.steganography.base.BaseActivity;
import com.example.steganography.databinding.ActivityDecodeBinding;

public class DecodeTextActivity extends BaseActivity<DecodeViewModel, ActivityDecodeBinding> implements DecodeNavigator {

    private static final int PICK_IMAGE = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databinding.setDecodeViewModel(viewModel);
        viewModel.navigator = this;


        databinding.password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    databinding.password.setHint("");
                else
                    databinding.password.setHint("password");
            }
        });

        databinding.decodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.startDecode(DecodeTextActivity.this, new TextDecodingCallback() {


                    @Override
                    public void onStartTextEncoding() {

                    }

                    @Override
                    public void onCompleteTextEncoding(ImageSteganography result) {

                        Log.e("llll", "lll" + result.getSecret_key());
                        Log.e("llll", "lll" + result.getMessage());

                        if (result != null) {
                            Log.e("llll", "lll" + result.getMessage().toString());
                            Log.e("llll", "lll" + result.getMessage());

                            Log.e("llll", "lll" + result.getSecret_key().toString());
                            Log.e("llll", "lll" + result.isSecretKeyWrong());
                            if (!result.isDecoded())
                                databinding.whetherDecoded.setText("No message found");
                            else {
                                if (!result.isSecretKeyWrong()) {
                                    databinding.whetherDecoded.setText("Decoded");
                                    databinding.decodeMessage.setText("" + result.getMessage());
                                } else {
                                    databinding.whetherDecoded.setText("Wrong secret key");
                                }
                            }
                        } else {
                            databinding.whetherDecoded.setText("Select Image First");
                        }

                    }
                });

            }
        });
    }

    @Override
    protected ActivityDecodeBinding getDataBinding() {
        return DataBindingUtil.setContentView(this, R.layout.activity_decode);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.onActivityResult(requestCode, resultCode, data, this);

    }

    @Override
    protected DecodeViewModel initViewModel() {
        return new ViewModelProvider(this).get(DecodeViewModel.class);
    }


    @Override
    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }


}
