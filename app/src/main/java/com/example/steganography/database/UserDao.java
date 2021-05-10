package com.example.steganography.database;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class UserDao {

    public static void addUser(User user, OnCompleteListener<Void> onCompleteListener) {

        DocumentReference userDoc = new DataBaseFirebaes().getUse().document(user.id);

        userDoc.set(user).addOnCompleteListener(onCompleteListener);

    }


    public static void getUser(String usrId, OnCompleteListener<DocumentSnapshot> onCompleteListener) {

        Task<DocumentSnapshot> userDoc = new DataBaseFirebaes().getUse()
                .document(usrId).get().addOnCompleteListener(onCompleteListener);


    }



}
