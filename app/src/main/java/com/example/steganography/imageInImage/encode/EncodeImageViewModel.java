package com.example.steganography.imageInImage.encode;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextEncodingCallback;
import com.ayush.imagesteganographylibrary.Text.ImageSteganography;
import com.ayush.imagesteganographylibrary.Text.TextEncoding;
import com.example.steganography.base.BaseViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class EncodeImageViewModel extends BaseViewModel<navigator> {


    public EncodeImageViewModel(@NonNull Application application) {
        super(application);
    }

    static final int PICK_CARRIER_IMAGE = 100;
    static final int PICK_HIDDEN_IMAGE = 200;
    public ObservableField<String> password = new ObservableField<>("");
    public Uri selectedHiddenImage;
    public Uri selectedCarrierImage;
    public ObservableField<Uri> carrierImageUrl = new ObservableField<>();
    public ObservableField<Uri> hiddenImageUrl = new ObservableField<>();
    public ByteArrayOutputStream boas;
    int pick;
    public byte[] imageByte;
    String downloadedUri;
    String string_error;
    Bitmap result;
    ImageSteganography imageSteganography;
    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference ref;
    private Bitmap original_carrier_image;
    private Bitmap original_hidden_image;
    private ProgressDialog save;
    String stringImageToEncode;

    int lengthbmp;

    public void openGalleryToPickHiddenImage() {
        navigator.openGalleryHidden();

    }

    public void openGalleryToPickCarrierImage() {
        navigator.openGalleryCarrier();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data, Activity activity) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PICK_CARRIER_IMAGE:
                    selectedCarrierImage = data.getData();
                    //imageUrl.postValue(data.getData());
                    try {
                        original_carrier_image = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), selectedCarrierImage);

                        carrierImageUrl.set(selectedCarrierImage);
                        /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        original_carrier_image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] imageInByte = stream.toByteArray();
                        lengthbmp = imageInByte.length;
                        Log.e("cover zise","="+lengthbmp);*/

                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                    break;

                case PICK_HIDDEN_IMAGE:
                    selectedHiddenImage = data.getData();
                    //imageUrl.postValue(data.getData());
                    try {
                        original_hidden_image = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), selectedHiddenImage);
                        //  Log.e("befor"," "+original_carrier_image.getWidth());
                        //Log.e("befor"," "+original_carrier_image.getHeight());

                        hiddenImageUrl.set(selectedHiddenImage);

                        //bitmapToString(original_hidden_image);
                        Log.e("befor", " " + stringImageToEncode);

                        //Bitmap converetdImage =getResizedBitmap(original_hidden_image,200);
                        Bitmap converetdImage = Bitmap.createScaledBitmap(original_hidden_image, 200, 250, true);


                        Log.e("befor", " " + converetdImage.getWidth());
                        Log.e("befor", " " + converetdImage.getHeight());
                        bitmapToString(converetdImage);


                        //uploadFile(activity);

                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                    break;
            }
        }

    }


    public void bitmapToString(Bitmap imageToConvert) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageToConvert.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        stringImageToEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);

    }


    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    public void encodedTextInImage(Activity activity, TextEncodingCallback textEncodingCallback) {

          /*
         resizeImage();
          Activity activity, TextEncodingCallback textEncodingCallback
        public Bitmap encodedTextInImage() {
        */
        if (stringImageToEncode != null) {
            if (password.get().toString() != null) {

                // if (downloadedUri!= null) {
                Log.e("enter encode", "password" + password.get());
                Log.e("enter encode", "uri" + stringImageToEncode);
                Log.e("enter encode", "uri" + original_carrier_image.getWidth());

                Log.e("enter encode", "uri" + original_hidden_image.getWidth());

        /*stegano s =new stegano(original_carrier_image,original_hidden_image);
            s.hide();
           result= s.getResultImage();*/

                imageSteganography = new ImageSteganography(stringImageToEncode, password.get().toString(),
                        original_carrier_image);


                TextEncoding textEncoding = new TextEncoding(activity, textEncodingCallback);
                textEncoding.execute(imageSteganography);


              /* imageSteganography = new ImageSteganography(downloadedUri, password.get().toString(),
                       original_carrier_image);


               TextEncoding textEncoding = new TextEncoding(activity, textEncodingCallback);
               textEncoding.execute(imageSteganography);*/


            } else {
                string_error = "please enter password";
                showErrorMessage();
            }

            // return result;
        } else {
            string_error = "please select image";
            showErrorMessage();
        }


        //return result;
        //  return null;
    }

    private void showErrorMessage() {
        navigator.showErrorMessage();
    }

    public void uploadFile(Activity activity) {

        if (selectedHiddenImage != null) {
            storage = FirebaseStorage.getInstance();

            storageRef = storage.getReference();
            ref = storageRef.child("images/" + UUID.randomUUID().toString());

            ref.putFile(selectedHiddenImage)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                    storageRef.child("images/" + taskSnapshot.getMetadata().getName()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {


                                            downloadedUri = uri.toString();


                                            Log.e("uri", "=" + downloadedUri);
                                            Log.e("uri", "==" + uri.getPath());

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            Log.e("uri", "==" + exception.getLocalizedMessage());

                                        }
                                    });

                                }
                            });


        }

    }


}
