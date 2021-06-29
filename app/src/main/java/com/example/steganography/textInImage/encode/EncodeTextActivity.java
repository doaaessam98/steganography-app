package com.example.steganography.textInImage.encode;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextEncodingCallback;
import com.ayush.imagesteganographylibrary.Text.ImageSteganography;
import com.example.steganography.R;
import com.example.steganography.base.BaseActivity;
import com.example.steganography.databinding.EncodeTextBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class EncodeTextActivity extends BaseActivity<EncodeViewModel, EncodeTextBinding> implements EncodeActivityNavigator {
    private static final int PICK_IMAGE = 100;
    Bitmap encoded_image;
    ImageView encoded;
    private ProgressDialog save;
    Boolean encodedCompleted = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databinding.setVm(viewModel);
        viewModel.navigator = this;
        viewModel.checkAndRequestPermissions(this);

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
                            Log.e("on complete", "message" + result.getMessage().toString());
                            Log.e("on complete", "message" + result.getEncrypted_message());
                            Log.e("on complete", "password" + result.getSecret_key());
                            Log.e("on complete", "tttt" + result.isEncoded());
                            Toast.makeText(getApplication(), "ok", Toast.LENGTH_LONG).show();


                            encoded_image = result.getEncoded_image();
                            databinding.image.setImageBitmap(encoded_image);
                            encodedCompleted = true;
                            // databinding.imageNew.setImageBitmap(encoded_image);
                            // whether_encoded.setText("Encoded");

                            //Toast.makeText(getApplication(), "encoded", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        });

        databinding.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareImageandText(encoded_image);
            }
        });
        databinding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (encodedCompleted == true) {

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
                } else {


                }
            }
        });

    }



    void saveToInternalStorage(Bitmap bitmapImage) {
        OutputStream fOut;
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "steganography" + ".PNG"); // the File to save ,
        try {
            fOut = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fOut); // saving the Bitmap to a file
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream
            save.dismiss();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }
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


    private void shareImageandText(Bitmap bitmap) {
        Uri uri = viewModel.getmageToShare(bitmap, this);
        Intent share = new Intent(Intent.ACTION_SEND);

        // putting uri of image to be shared
        share.putExtra(Intent.EXTRA_STREAM, uri);

        // adding text to share
        share.putExtra(Intent.EXTRA_TEXT, "Sharing Image");
        // Add subject Here

        // setting type to image
        share.setType("image/png");

        // calling startactivity() to share
        startActivity(Intent.createChooser(share, "Share Via"));
    }

}