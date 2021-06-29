package com.example.steganography.textInImage.encode;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.ObservableField;

import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextEncodingCallback;
import com.ayush.imagesteganographylibrary.Text.ImageSteganography;
import com.ayush.imagesteganographylibrary.Text.TextEncoding;
import com.example.steganography.base.BaseViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getCacheDir;

public class EncodeViewModel extends BaseViewModel<EncodeActivityNavigator> {

    private static final int PICK_IMAGE = 100;
    public ImageView imageView;
    public ObservableField<Uri> imageUrl = new ObservableField<>();
    public ObservableField<String> user_message = new ObservableField<>(" ");
    public ObservableField<String> message_password = new ObservableField<>(" ");
    public Bitmap encoded_image;
    public Uri selectedImage;
    ImageSteganography imageSteganography;
    private ProgressDialog save;
    private Bitmap original_image;

    public EncodeViewModel(@NonNull Application application) {
        super(application);


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

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkAndRequestPermissions(Activity activity) {
        int permissionWriteStorage = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int ReadPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (ReadPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[0]), 1);
        }
    }

    public Uri getmageToShare(Bitmap bitmap, Activity activity) {
        File imagefolder = new File(getCacheDir(), "images");

        Uri uri = null;
        try {
            imagefolder.mkdirs();
            File file = new File(imagefolder, "shared_image.png");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
            uri = FileProvider.getUriForFile(activity, "com.anni.shareimage.fileprovider", file);
        } catch (Exception e) {
            Toast.makeText(activity, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return uri;
    }




    public void goToGallery() {

        navigator.openGallery();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data, Activity activity) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PICK_IMAGE:
                    selectedImage = data.getData();
                    //imageUrl.postValue(data.getData());
                    try {
                        original_image = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), selectedImage);

                        imageUrl.set(selectedImage);
                        //imageUrl.set(Uri.parse(original_image.toString()));

                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                    break;
            }
        }
    }

   /* public void savEncodedImage() {

        Thread PerformEncoding = new Thread(new Runnable() {
                @Override
                public void run() {
                    createFileToEncodedImage(encoded_image);
                }
            });
            save = new ProgressDialog(getApplication());
            save.setMessage("Saving, Please Wait...");
            save.setTitle("Saving Image");
            save.setIndeterminate(false);
            save.setCancelable(false);
            save.show();
            PerformEncoding.start();
        }*/


    public void encodedTextInImage(Activity activity, TextEncodingCallback textEncodingCallback) {

        Log.e("dddddd", "in  encode" + user_message.get());
        Log.e("dddddd", "in  encode" + message_password.get());

        if (selectedImage != null) {
            if (user_message.get() != null && message_password.get() != null) {
                Log.e("eee", "mmm" + user_message.get());
                imageSteganography = new ImageSteganography(user_message.get().toString(),
                        message_password.get().toString(),
                        original_image);
                Log.e("eeee", "ddd" + user_message.get());
                Log.e("eeee", "ddd" + message_password.get());
                Log.e("eeee", "ddd" + original_image.getWidth());

                TextEncoding textEncoding = new TextEncoding(activity, textEncodingCallback);
                textEncoding.execute(imageSteganography);

            }

        }

    }


    public void createFileToEncodedImage(Bitmap bitmap) {
        Log.e("dddddd", "in view model");
        FileOutputStream outputStream = null;
        File Card = Environment.getExternalStorageDirectory();
        File direct = new File(Card.getAbsolutePath() + "/steganography");
        direct.mkdir();
        Log.e("dddddd", "in view model" + direct.mkdir());

        String nameFile = String.format("%d.png", System.currentTimeMillis());
        Log.e("dddddd", "in view model" + nameFile);
        Log.e("ddddd", "direct" + direct.getName());
        Log.e("ddddd", "direct" + direct.getAbsolutePath());
        Log.e("ddddd", "direct" + direct.canWrite());
        Log.e("ddddd", "direct" + direct.exists());

        File outFile = new File(direct, nameFile);
        //Toast.makeText(getApplication(), "ok save", Toast.LENGTH_LONG);
        try {

            outputStream = new FileOutputStream(outFile);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            //Toast.makeText(getApplication(), "ok save", Toast.LENGTH_LONG);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}


