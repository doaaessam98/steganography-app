package com.example.steganography.base;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

abstract public class BaseActivity<VM extends ViewModel, DB extends ViewDataBinding> extends AppCompatActivity {
    protected VM viewModel;
    protected DB databinding;
    public File direct;
    public String path;
    public Boolean isSaved = false;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databinding = getDataBinding();
        viewModel = initViewModel();


      /*  viewModel.progress_bar.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
  showLoadingDialog("loading");
            }
        });*/

    }

    protected abstract DB getDataBinding();

    //ProgressDialog progressDialog = new ProgressDialog(this);

   /* public AlertDialog showLoadingDialog(String message ,Activity activity) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton(positiveBtnText, positiveBtnClickListener);
        if (negativeBtnText != null)
            builder.setNegativeButton(negativeBtnText, negativeBtnClickListener);
        AlertDialog alert = builder.create();
        alert.show();
        return alert;

    }

   /* public void hideLoadingDialog() {
        if (progressDialog.isShowing() == true) {
            progressDialog.dismiss();

        }

    }*/



    protected abstract VM initViewModel();


    public void showMessage(Activity activity, String title, String message, String posActionString,
                            DialogInterface.OnClickListener posAction) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(posActionString, posAction)
                .setCancelable(true);
        AlertDialog alert = builder.create();

        builder.show();

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

    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/DirName");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/DirName/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File("/sdcard/DirName/", fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveImage(Bitmap bitmap, ProgressDialog save, String folderName, Activity activity) {
        FileOutputStream outputStream = null;
        File filepath = Environment.getExternalStorageDirectory();
        direct = new File(filepath + folderName);
        Log.e("direct", "" + direct.toString());
        path = direct.toString();
        direct.mkdirs();
        String nameFile = String.format("%d.png", System.currentTimeMillis());
        File file = new File(direct, nameFile);
        try {

            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            save.dismiss();
        } catch (Exception e) {
            save.setMessage("cant save");
            save.dismiss();


            e.printStackTrace();
        }


    }


    public void shareImageEncodedText(Bitmap bitmap) {
        Uri uri = getmageToShare(bitmap, this);
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

    private void saveToInternalStorage(Bitmap bitmapImage, ProgressDialog save) {
        OutputStream fOut;
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "Encoded" + ".PNG"); // the File to save ,
        try {
            fOut = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fOut); // saving the Bitmap to a file
            fOut.flush(); // Not really required
            fOut.close();
            save.dismiss();// do not forget to close the stream

           /* whether_encoded.post(new Runnable() {
                @Override
                public void run() {
                    save.dismiss();
                }
            });*/
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkAndRequestPermissions() {
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
}