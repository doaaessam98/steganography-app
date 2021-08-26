package com.example.steganography.imageInImage.encode.encodeResult;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.steganography.R;
import com.example.steganography.base.BaseActivity;
import com.example.steganography.databinding.ActivityResultEncodeImageBinding;

public class ResultEncodeImage extends BaseActivity<ResultEncodeViewModel, ActivityResultEncodeImageBinding> {

    String string_image;
    Bitmap bitmapResultEncodeImage;
    ProgressDialog save;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databinding.setVm(viewModel);
        Intent intent = getIntent();
        bitmapResultEncodeImage = (Bitmap) intent.getParcelableExtra("BitmapImage");
        databinding.resultEncodeImage.setImageBitmap(bitmapResultEncodeImage);

        //string_image=getIntent().getStringExtra("image");
        //byte[] encodeByte = Base64.decode(string_image, Base64.DEFAULT);
        //bitmapResultEncodeImage = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        // databinding.resultEncodeImage.setImageBitmap(bitmapResultEncodeImage);
        // byte[] byteArray = getIntent().getByteArrayExtra("image");
        // bitmapResultEncodeImage= BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        // databinding.resultEncodeImage.setImageBitmap(bitmapResultEncodeImage);
       /* string_image=getIntent().getStringExtra("result_encode_image");
        byte[] encodeByte = Base64.decode(string_image, Base64.DEFAULT);
          bitmapDecodedImage = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        databinding.resultEncodeImage.setImageBitmap(bitmapDecodedImage);*/


        databinding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!string_image.isEmpty()) {

                    // saveDecodedImage();
                } else {
                    showMessage(ResultEncodeImage.this, null, "already  save", "ok"
                            , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    string_image = " ";
                                    dialog.dismiss();
                                }
                            });
                }
            }
        });

        databinding.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImageEncodedText(bitmapResultEncodeImage);
            }
        });

    }

  /*private void shareEncodedImage() {
        if(isEncodedComplete==true){
            shareImageEncodedText(encoded_image);}
        else {
            showMessage(this, null, "encoded do not complete", "ok"
                    , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }
    }*/


   /* private void saveDecodedImage() {


        Thread PerformEncoding = new Thread(new Runnable() {
            @Override
            public void run() {
                saveToInternalStorage(bitmapResultEncodeImage,save);
            }
        });
        save = new ProgressDialog(ResultEncodeImage.this);
        save.setMessage("Saving, Please Wait...");
        save.setTitle("Saving Image");
        save.setIndeterminate(false);
        save.setCancelable(false);
        save.show();
        PerformEncoding.start();
        showMessage(this, null, "decode and save  in download ", "ok"
                ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        string_image=" ";
                        dialog.dismiss();
                    }
                });
    }*/

    @Override
    protected ActivityResultEncodeImageBinding getDataBinding() {

        return DataBindingUtil.setContentView(this, R.layout.activity_result_encode_image);

    }

    @Override
    protected ResultEncodeViewModel initViewModel() {
        return new ViewModelProvider(this).get(ResultEncodeViewModel.class);

    }
}
