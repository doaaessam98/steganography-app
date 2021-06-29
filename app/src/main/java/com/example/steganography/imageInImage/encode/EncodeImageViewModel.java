package com.example.steganography.imageInImage.encode;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
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

import java.io.IOException;
import java.util.UUID;

public class EncodeImageViewModel extends BaseViewModel<navigator> {


    public EncodeImageViewModel(@NonNull Application application) {
        super(application);
    }

    static final int PICK_CARRIER_IMAGE = 100;
    static final int PICK_HIDDEN_IMAGE = 200;
    public Uri selectedHiddenImage;
    public Uri selectedCarrierImage;
    public ObservableField<Uri> carrierImageUrl = new ObservableField<>();
    public ObservableField<Uri> hiddenImageUrl = new ObservableField<>();
    public ObservableField<String> password = new ObservableField<>(" ");
    int pick;
    String downloadedUri;
    ImageSteganography imageSteganography;
    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference ref;
    private Bitmap original_carrier_image;
    private Bitmap original_hidden_image;
    private ProgressDialog save;

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


                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                    break;

                case PICK_HIDDEN_IMAGE:
                    selectedHiddenImage = data.getData();
                    //imageUrl.postValue(data.getData());
                    try {
                        original_hidden_image = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), selectedHiddenImage);

                        hiddenImageUrl.set(selectedHiddenImage);
                        uploadFile();

                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                    break;
            }
        }

    }

    public void encodedTextInImage(Activity activity, TextEncodingCallback textEncodingCallback) {


        if (password.get().toString() != null) {
            if (hiddenImageUrl.toString() != null) {

                Log.e("enter encode", "password" + password.get());
                Log.e("enter encode", "uri" + downloadedUri);

                imageSteganography = new ImageSteganography(downloadedUri.toString(), password.get().toString(),
                        original_carrier_image);
                // Log.e("eeee", "ddd" + user_message.get());
                //Log.e("eeee", "ddd" + message_password.get());
                // Log.e("eeee", "ddd" + original_image.getWidth());

                TextEncoding textEncoding = new TextEncoding(activity, textEncodingCallback);
                textEncoding.execute(imageSteganography);

            }

        } else {
            Log.e("eeee", "error");

        }


    }

    public void uploadFile() {

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
/*public void downloadImageUri(){

    storage = FirebaseStorage.getInstance();

    storageRef = storage.getReference();
    ref.child("images").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
        @Override
        public void onComplete(@NonNull Task<Uri> task) {
            if (task.isSuccessful()){
                downloadedUri=task.toString();
                Log.e("uri","=="+downloadedUri);
            }

        }
    });

}*/

}
