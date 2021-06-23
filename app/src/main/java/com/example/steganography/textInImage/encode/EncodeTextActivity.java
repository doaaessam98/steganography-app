package com.example.steganography.textInImage.encode;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextEncodingCallback;
import com.ayush.imagesteganographylibrary.Text.ImageSteganography;
import com.example.steganography.R;
import com.example.steganography.base.BaseActivity;
import com.example.steganography.databinding.EncodeTextBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class EncodeTextActivity extends BaseActivity<EncodeViewModel, EncodeTextBinding> implements EncodeActivityNavigator {
    private static final int PICK_IMAGE = 100;
    Bitmap encoded_image;
    ImageView encoded;
    private ProgressDialog save;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databinding.setVm(viewModel);
        viewModel.navigator = this;
        checkAndRequestPermissions();

        databinding.message.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    databinding.message.setHint("");
                else
                    databinding.message.setHint(R.string.encode_hint);
            }
        });
        databinding.password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    databinding.password.setHint("");
                else
                    databinding.password.setHint("password");
            }
        });
        databinding.encodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.encodedTextInImage(EncodeTextActivity.this, new TextEncodingCallback() {
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
                            databinding.image.setImageBitmap(encoded_image);
                            // databinding.imageNew.setImageBitmap(encoded_image);
                            // whether_encoded.setText("Encoded");

                            //Toast.makeText(getApplication(), "encoded", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        });
        /*databinding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databinding.imageNew.setImageBitmap(encoded_image);


               viewModel.createFileToEncodedImage(encoded_image);


            }
        });*/
        databinding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // final Bitmap imgToSave = encoded_image;
                Thread PerformEncoding = new Thread(new Runnable() {
                    @Override
                    public void run() {


                        saveToInternalStorage(encoded_image);
                    }
                });
                save = new ProgressDialog(EncodeTextActivity.this);
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
    protected EncodeTextBinding getDataBinding() {
        return DataBindingUtil.setContentView(this, R.layout.encode_text);

    }


    @Override
    protected EncodeViewModel initViewModel() {
        return new ViewModelProvider(this).get(EncodeViewModel.class);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.onActivityResult(requestCode, resultCode, data, this);
    }


    @Override
    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
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


        File file = new File(Environment.getExternalStorageDirectory(), "steganography" + ".PNG"); // the File to save ,
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            save.dismiss();

        } catch (Exception e) {
            e.printStackTrace();
        }
       /* try {
            fOut = new FileOutputStream(myDir);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fOut); // saving the Bitmap to a file
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream
            save.dismiss();

            /* whether_encoded.post(new Runnable() {
                @Override
                public void run() {
                    save.dismiss();
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

}