package com.example.steganography.imageInImage.encode;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextEncodingCallback;
import com.ayush.imagesteganographylibrary.Text.ImageSteganography;
import com.example.steganography.R;
import com.example.steganography.base.BaseActivity;
import com.example.steganography.databinding.ActivityEncodeImageBinding;

public class EncodeImageActivity extends BaseActivity<EncodeImageViewModel, ActivityEncodeImageBinding> implements navigator {
    private static final String CHANNEL_ID = " encode";
    private ProgressDialog save;
    Boolean isEncodedComplete = false;
    Bitmap encoded_image;
    // static final int PICK_HIDDEN_IMAGE = 200;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databinding.setVmImage(viewModel);
        viewModel.navigator = this;
        checkAndRequestPermissions();
   /* databinding.encodeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.encodedTextInImage();
                databinding.resultEncodeImage.setImageBitmap(viewModel.result);
                encoded_image=viewModel.result;

            }
        });*/
        databinding.buttonSaveEncodedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEncodedComplete == true) {
                    saveEncodeImage();

                } else {

                    showMessage(EncodeImageActivity.this, null, "please Encode image first", "ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
        databinding.buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEncodedComplete == true) {
                    shareImageEncodedText(encoded_image);
                } else {

                    showMessage(EncodeImageActivity.this, null, "please Encode image first", "ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });

        databinding.encodeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* showMessage(EncodeImageActivity.this, null, "must be connecting to network", "ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
       }
                });*/
                viewModel.encodedTextInImage(EncodeImageActivity.this, new TextEncodingCallback() {
                    @Override
                    public void onStartTextEncoding(ProgressDialog progressDialog) {


                    }

                    @Override
                    public void onCompleteTextEncoding(ImageSteganography result) {
                        if (result != null && result.isEncoded()) {
                            Log.e("dddd", "tttt" + result.getMessage().toString());
                            Log.e("dddd", "tttt" + result.getEncrypted_message());
                            Log.e("dddd", "tttt" + result.getSecret_key());
                            Log.e("dddd", "tttt" + result.getImage());
                            Log.e("dddd", "tttt" + result.getEncoded_image());
                            Log.e("dddd", "ok");
                            Toast.makeText(getApplication(), "Encode Successfully", Toast.LENGTH_SHORT).show();
                            encoded_image = result.getEncoded_image();
                            databinding.resultEncodeImage.setImageBitmap(encoded_image);
                            isEncodedComplete = true;

                        }

                    }
                });


            }
        });
    }

    private void saveEncodeImage() {


        Thread PerformEncoding = new Thread(new Runnable() {
            @Override
            public void run() {
                saveImage(encoded_image, save, "/imageSteganography", EncodeImageActivity.this);


            }
        });
        save = new ProgressDialog(EncodeImageActivity.this);
        save.setMessage("Saving, Please Wait...");
        save.setTitle("Saving Image");
        save.setIndeterminate(false);
        save.setCancelable(false);
        save.show();
        PerformEncoding.start();
        Toast.makeText(this, "save in /storage/ emulated/0/ImageSteganography", Toast.LENGTH_LONG).show();

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
    public void openGalleryHidden() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select hidden Picture"), viewModel.PICK_HIDDEN_IMAGE);

    }

    @Override
    public void openGalleryCarrier() {


        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select  Picture"), viewModel.PICK_CARRIER_IMAGE);


    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.onActivityResult(requestCode, resultCode, data, this);

    }


    @Override
    public void showErrorMessage() {
        showMessage(this, null, viewModel.string_error, "ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
    }
}
