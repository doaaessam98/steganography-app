package com.example.steganography.database;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DataBaseFirebaes {

    static String USER_REF = "user";
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public CollectionReference getUse() {

        return db.collection(USER_REF);
    }


}
