package com.example.steganography.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.steganography.R;
import com.example.steganography.base.BaseActivity;
import com.example.steganography.databinding.ActivityRegistrationBinding;
import com.example.steganography.home.HomeActivity;
import com.example.steganography.login.Login;

public class Registration extends BaseActivity<RegisterViewModel, ActivityRegistrationBinding> implements RegisterNavigator {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityRegistrationBinding databinding = DataBindingUtil.setContentView(this, R.layout.activity_registration);
        databinding.setRegisterViweModel(viewModel);
        viewModel.navigator = this;
        databinding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity();

            }
        });
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
        Intent intent = new Intent(Registration.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void openLoginActivity() {
        Intent intent = new Intent(Registration.this, Login.class);
        startActivity(intent);
        finish();
    }
}
