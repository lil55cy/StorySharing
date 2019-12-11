package com.example.storysharing;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class FirebaseUtility {
    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    private static FirebaseAuth mAuth;

    public static Post saveNewPost(String title, String body, String email, boolean publish) {
        mAuth = FirebaseAuth.getInstance();
        String key = mDatabase.child("posts").push().getKey();
        Post post = new Post(title, body, email, publish, key, mAuth.getUid());

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, post);

        mDatabase.updateChildren(childUpdates);

        return post;
    }

    public static void saveNewUser(String fullname, String bio, String uid, String email) {
        mAuth = FirebaseAuth.getInstance();
        User user = new User(fullname, bio, uid, email);
        mDatabase.child("userinfo").child(mAuth.getUid()).setValue(user);
    }


    public static void saveImageToStorage(Bitmap bitmap) {
        mAuth = FirebaseAuth.getInstance();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] data = baos.toByteArray();

        mStorage.child(mAuth.getUid()).putBytes(data);

    }

    public static void rotateImage(Bitmap inputBitmap) {
        mAuth = FirebaseAuth.getInstance();


        float degrees = 90; //rotation degree
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees);
        Bitmap bitmap = Bitmap.createBitmap(inputBitmap, 0, 0, inputBitmap.getWidth(), inputBitmap.getHeight(), matrix, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] data = baos.toByteArray();

        mStorage.child(mAuth.getUid()).putBytes(data);
    }
}
