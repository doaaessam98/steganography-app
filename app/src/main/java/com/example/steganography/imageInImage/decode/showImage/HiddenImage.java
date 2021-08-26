package com.example.steganography.imageInImage.decode.showImage;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.steganography.R;
import com.example.steganography.base.BaseActivity;
import com.example.steganography.databinding.ActivityHiddenImageBinding;

public class HiddenImage extends BaseActivity<HiddenImageViewModel, ActivityHiddenImageBinding> {
    Bitmap bitmapDecodedImage;
    ProgressDialog save;
    String string_image;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databinding.setVm(viewModel);

        string_image = getIntent().getStringExtra("hidden_image");

        /*databinding.progressBar.setVisibility(View.VISIBLE);

        Glide.with(HiddenImage.this).load(Uri.parse(string_image.toString())).into(databinding.hiddenImage);
        databinding.progressBar.setVisibility(View.GONE);*/

        byte[] encodeByte = Base64.decode(string_image, Base64.DEFAULT);
        bitmapDecodedImage = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        databinding.hiddenImage.setImageBitmap(bitmapDecodedImage);

        databinding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!string_image.isEmpty()) {

                    saveDecodedImage();
                } else {
                    showMessage(HiddenImage.this, null, "already  save", "ok"
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

        databinding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databinding.hiddenImage.setImageResource(R.color.text_color);
                string_image = "";
            }
        });


    }

    @Override
    protected ActivityHiddenImageBinding getDataBinding() {
        return DataBindingUtil.setContentView(this, R.layout.activity_hidden_image);

    }

    @Override
    protected HiddenImageViewModel initViewModel() {
        return new ViewModelProvider(this).get(HiddenImageViewModel.class);

    }

    private void saveDecodedImage() {


        Thread PerformEncoding = new Thread(new Runnable() {
            @Override
            public void run() {
                saveImage(bitmapDecodedImage, save, "/DecodedImage", HiddenImage.this);
            }
        });
        save = new ProgressDialog(HiddenImage.this);
        save.setMessage("Saving, Please Wait...");
        save.setTitle("Saving Image");
        save.setIndeterminate(false);
        save.setCancelable(false);
        save.show();
        PerformEncoding.start();
        Toast.makeText(this, "save in /storage/ emulated/0/DecodedImage", Toast.LENGTH_LONG).show();


    }


}
