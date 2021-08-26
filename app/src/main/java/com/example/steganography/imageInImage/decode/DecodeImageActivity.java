package com.example.steganography.imageInImage.decode;

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

import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextDecodingCallback;
import com.ayush.imagesteganographylibrary.Text.ImageSteganography;
import com.example.steganography.R;
import com.example.steganography.base.BaseActivity;
import com.example.steganography.databinding.ActivityDecodeImageBinding;
import com.example.steganography.imageInImage.decode.showImage.HiddenImage;

public class DecodeImageActivity extends BaseActivity<DecodeImageViewModel, ActivityDecodeImageBinding>
        implements DecodeImageNavigator {

    private static final int PICK_IMAGE = 1;
    Bitmap bitmapDecodedImage;
    ProgressDialog save;
    String result_string_image;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        databinding.setVm(viewModel);
        viewModel.navigator = this;
      /* databinding.decodedImageInImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewModel.startDecode();
                //Bitmap bitmap=Bitmap.createBitmap(viewModel.result.getWidth(),viewModel.result.getHeight(), Bitmap.Config.ARGB_8888);
                databinding.resultImage.setImageBitmap(viewModel.result);

            }
        });*/
        databinding.decodedImageInImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  /*showMessage(DecodeImageActivity.this, null, "must be connicting to network", "ok", new DialogInterface.OnClickListener() {
                               @Override
                              public void onClick(DialogInterface dialog, int which) {
                                          dialog.dismiss();
                                                  decode();
    }
});*/


                viewModel.startDecode(DecodeImageActivity.this, new TextDecodingCallback() {
                    @Override
                    public void onStartTextDecoding(ProgressDialog progressDialog) {


                    }

                    @Override
                    public void onCompleteTextDecoding(ImageSteganography result) {
                        Log.e("llll", "lll" + result.getSecret_key());
                        Log.e("llll", "lll" + result.getMessage());
                        Log.e("llll", "lll" + result.getEncrypted_message());

                        Log.e("llll", "lll" + result.getImage());


                        if (result != null) {
                            Log.e("llll", "lll" + result.getMessage().toString());
                            Log.e("llll", "lll" + result.getMessage());

                            Log.e("llll", "lll" + result.getSecret_key().toString());
                            Log.e("llll", "lll" + result.isSecretKeyWrong());


                            if (!result.isDecoded()) {
                                Toast.makeText(DecodeImageActivity.this, "No message found", Toast.LENGTH_LONG).show();

                            } else {
                                if (!result.isSecretKeyWrong()) {
                                    Log.e("me", "gg" + result.getMessage());
                                    //off line  decode
                                    result_string_image = result.getMessage();
                                    openHiddenImage();


                                } else {
                                    showMessage(DecodeImageActivity.this, null, "Wrong secret key", "ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    Toast.makeText(DecodeImageActivity.this, "Wrong secret key", Toast.LENGTH_LONG).show();

                                }
                            }
                        } else {

                            Toast.makeText(DecodeImageActivity.this, "select image first", Toast.LENGTH_LONG).show();
                            // databinding.whetherDecoded.setText("Select Image First");
                        }

                    }
                });


            }
        });
    }


    private void openHiddenImage() {


        Intent intent = new Intent(DecodeImageActivity.this, HiddenImage.class);

        intent.putExtra("hidden_image", result_string_image);
        startActivity(intent);
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

    @Override
    public void showErrorMessage() {
        showMessage(this, null, viewModel.error_message, "ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }
}
