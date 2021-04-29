package com.example.steganography.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

abstract public class BaseActivity<VM extends ViewModel, DB extends ViewDataBinding> extends AppCompatActivity {
    public VM viewModel;
    public DB databinding;
    public AppCompatActivity activity;
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;

        databinding = DataBindingUtil.setContentView(this, getLayOut());
        viewModel = initViewModel();

      /*  viewModel.progress_bar.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
  showLoadingDialog("loading");
            }
        });*/

    }

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

    protected abstract int getLayOut();


}