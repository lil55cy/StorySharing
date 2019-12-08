package com.example.storysharing;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

        // childUpdates.put("/posts/" + mAuth.getUid() + "/" + key, post);
        childUpdates.put("/posts/" + key, post);

        mDatabase.updateChildren(childUpdates);

        return post;
    }
}
