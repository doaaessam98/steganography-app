package com.example.steganography.imageInImage.encode;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextEncodingCallback;
import com.ayush.imagesteganographylibrary.Text.ImageSteganography;
import com.example.steganography.R;
import com.example.steganography.base.BaseActivity;
import com.example.steganography.databinding.ActivityEncodeImageBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class EncodeImageActivity extends BaseActivity<EncodeImageViewModel, ActivityEncodeImageBinding> implements navigator {
    Bitmap encoded_image;
    private ProgressDialog save;

    // static final int PICK_HIDDEN_IMAGE = 200;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databinding.setVmImage(viewModel);
        viewModel.navigator = this;
        databinding.encodeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.encodedTextInImage(EncodeImageActivity.this, new TextEncodingCallback() {
                    @Override
                    public void onStartTextEncoding() {

                    }

                    @Override
                    public void onCompleteTextEncoding(ImageSteganography result) {


                        if (result != null && result.isEncoded()) {
                            Log.e("dddd", "tttt" + result.getMessage().toString());
                            Log.e("dddd", "tttt" + result.getEncrypted_message());
                            Log.e("dddd", "tttt" + result.getSecret_key());
                            Log.e("dddd", "tttt" + result.isEncoded());
                            Toast.makeText(getApplication(), "ok", Toast.LENGTH_LONG).show();


                            encoded_image = result.getEncoded_image();
                            databinding.resultEncodeImage.setImageBitmap(encoded_image);
                            // databinding.imageNew.setImageBitmap(encoded_image);
                            // whether_encoded.setText("Encoded");

                            //Toast.makeText(getApplication(), "encoded", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        });


        databinding.buttonSaveEncodedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // final Bitmap imgToSave = encoded_image;
                Thread PerformEncoding = new Thread(new Runnable() {
                    @Override
                    public void run() {


                        saveToInternalStorage(encoded_image);
                    }
                });
                save = new ProgressDialog(EncodeImageActivity.this);
                save.setMessage("Saving, Please Wait...");
                save.setTitle("Saving Image");
                save.setIndeterminate(false);
                save.setCancelable(false);
                save.show();
                PerformEncoding.start();
            }
        });

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

    private void checkAndRequestPermissions() {
        int permissionWriteStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int ReadPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (ReadPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), 1);
        }
    }

    private void saveToInternalStorage(Bitmap bitmapImage) {


        File file = new File(Environment.getExternalStorageDirectory(), "steganographyImage" + ".PNG"); // the File to save ,
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            save.dismiss();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
