package com.example.steganography.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.steganography.R;
import com.example.steganography.base.BaseActivity;
import com.example.steganography.databinding.ActivityLoginBinding;
import com.example.steganography.home.HomeActivity;
import com.example.steganography.register.Registration;
import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseUser;

public class Login extends BaseActivity<LoginViewModel, ActivityLoginBinding> implements LoginNavigator {
    Animation topAnim;
    Animation bottomAnim;
    Animation topRotet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLoginBinding databinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        databinding.setLoginViewModel(viewModel);
        viewModel.navigator = this;
        FacebookSdk.sdkInitialize(Login.this);
        databinding.facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.loginWithFacebook(Login.this);
            }
        });

        databinding.openRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });

        databinding.googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.loginWithGoogle(Login.this);
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = viewModel.auth.getCurrentUser();
        if (currentUser != null) {
            openHomeActivity();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        viewModel.onActivityResult(
                requestCode,
                resultCode,
                data,
                this);
    }


    @Override
    protected LoginViewModel initViewModel() {
        return new ViewModelProvider(this).get(LoginViewModel.class);

    }

    @Override
    protected int getLayOut() {
        return R.layout.activity_login;
    }

    @Override
    public void openHomeActivity() {
        Intent intent = new Intent(Login.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


    public void openRegisterActivity() {
        Intent intent = new Intent(Login.this, Registration.class);
        startActivity(intent);
        finish();
    }
}