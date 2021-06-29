package com.example.steganography.database;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DataBaseFirebaes {

    static String USER_REF = "user";
    static String HIDDEN_IMAGE = "images/space.jpg";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();


    public CollectionReference getUse() {

        return db.collection(USER_REF);
    }

    public StorageReference getData() {

        return storage.getReferenceFromUrl(HIDDEN_IMAGE);
    }

}
