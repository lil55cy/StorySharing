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
    /**
     * Firebase database.
     */
    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    /**
     * Firebase storage (using for images).
     */
    private static StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    /**
     * Firebase authentication.
     */
    private static FirebaseAuth mAuth;

    /**
     * Add post to firebase real time database.
     * @param title title of post
     * @param body body of post
     * @param email email address of user who created post
     * @param publish whether or not the post should be anonymous
     * @return post object
     */
    public static Post saveNewPost(String title, String body, String email, boolean publish) {
        mAuth = FirebaseAuth.getInstance();
        String key = mDatabase.child("posts").push().getKey();
        Post post = new Post(title, body, email, publish, key, mAuth.getUid());

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, post);

        mDatabase.updateChildren(childUpdates);

        return post;
    }

    /**
     * Save new user to firebase.
     * @param fullname name of user
     * @param bio user's bio
     * @param uid user id
     * @param email user's email address
     */
    public static void saveNewUser(String fullname, String bio, String uid, String email) {
        mAuth = FirebaseAuth.getInstance();
        User user = new User(fullname, bio, uid, email);
        mDatabase.child("userinfo").child(mAuth.getUid()).setValue(user);
    }

    /**
     * Save image to firebase storage.
     * @param bitmap image
     */
    public static void saveImageToStorage(Bitmap bitmap) {
        mAuth = FirebaseAuth.getInstance();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] data = baos.toByteArray();

        mStorage.child(mAuth.getUid()).putBytes(data);

    }

    /**
     * Rotate image 90 degrees clockwise.
     *
     * Rotating the image deteriorates quality.
     * @param inputBitmap image
     */
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
