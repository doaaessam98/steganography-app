package com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback;

import android.app.ProgressDialog;

import com.ayush.imagesteganographylibrary.Text.ImageSteganography;

/**
 * This the callback interface for TextDecoding AsyncTask.
 */

public interface TextDecodingCallback {

    void onStartTextDecoding(ProgressDialog progressDialog);

    void onCompleteTextDecoding(ImageSteganography result);

}
