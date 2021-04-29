package com.example.steganography.database;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;

public class UserDao {

    public static void addUser(User user, OnCompleteListener<Void> onCompleteListener) {

        DocumentReference userDoc = new DataBaseFirebaes().getUse().document(user.id);

        userDoc.set(user).addOnCompleteListener(onCompleteListener);

    }
}
