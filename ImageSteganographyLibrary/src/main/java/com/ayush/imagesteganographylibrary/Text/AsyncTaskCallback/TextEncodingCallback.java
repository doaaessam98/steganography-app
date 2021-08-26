package com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback;

import android.app.ProgressDialog;

import com.ayush.imagesteganographylibrary.Text.ImageSteganography;

/**
 * This the callback interface for TextEncoding AsyncTask.
 */

public interface TextEncodingCallback {

    void onStartTextEncoding(ProgressDialog progressDialog);

    void onCompleteTextEncoding(ImageSteganography result);

}
