package com.example.steganography.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

abstract public class BaseActivity<VM extends ViewModel, DB extends ViewDataBinding> extends AppCompatActivity {
    protected VM viewModel;
    protected DB databinding;

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

/*    ProgressDialog progressDialog = new ProgressDialog(this);

    public ProgressDialog showLoadingDialog(String message) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    public void hideLoadingDialog() {
        if (progressDialog.isShowing() == true) {
            progressDialog.dismiss();

        }

    }*/


    protected abstract VM initViewModel();


    public void showMessage(Activity activity, String title, String message, String posActionString,
                            DialogInterface.OnClickListener posAction,
                            Boolean isCancelable) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(posActionString, posAction)
                .setCancelable(isCancelable);
        builder.show();
    }
}