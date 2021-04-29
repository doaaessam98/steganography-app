package com.example.steganography.register;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.steganography.MainActivity;
import com.example.steganography.R;
import com.example.steganography.base.BaseActivity;
import com.example.steganography.databinding.ActivityRegistrationBinding;

public class Registration extends BaseActivity<RegisterViewModel, ActivityRegistrationBinding> implements RegisterNavigator {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration);

        ActivityRegistrationBinding databinding = DataBindingUtil.setContentView(this, R.layout.activity_registration);
        databinding.setRegisterViweModel(viewModel);
        viewModel.navigator = this;

    }

    @Override
    protected RegisterViewModel initViewModel() {

        return new ViewModelProvider(this).get(RegisterViewModel.class);
    }

    @Override
    protected int getLayOut() {

        return R.layout.activity_registration;
    }


    @Override
    public void openHomeActivity() {
        Intent intent = new Intent(Registration.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
