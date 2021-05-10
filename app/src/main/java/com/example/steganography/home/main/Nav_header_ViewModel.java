package com.example.steganography.home.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.example.steganography.base.BaseViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Nav_header_ViewModel extends BaseViewModel {


    public ObservableField<String> userEmail = new ObservableField<>("");
    public ObservableField<String> username = new ObservableField<>("");

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser != null) {
                String userId = firebaseUser.getUid();
                userEmail.set(firebaseUser.getEmail());
                username.set(firebaseUser.getDisplayName());

            }
        }
    };

    public Nav_header_ViewModel(@NonNull Application application) {
        super(application);
    }
}
