package com.example.storysharing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;

public class ProfileInfoActivity extends AppCompatActivity {

    /**
     * Firebase database.
     */
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    /**
     * Firebase storage (using for images).
     */
    private static StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    /**
     * Selected post.
     */
    private Post post;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);

        post = (Post) getIntent().getSerializableExtra("post");

        Log.i("post id", post.uid + "");

        mDatabase.child("userinfo").child(post.uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Map userMap = (Map)dataSnapshot.getValue();
                TextView name = findViewById(R.id.name);
                name.setText((String)userMap.get("fullname"));
                name.setMovementMethod(new ScrollingMovementMethod());
                TextView bio = findViewById(R.id.bio);
                bio.setText((String)userMap.get("bio"));
                bio.setMovementMethod(new ScrollingMovementMethod());
                TextView email = findViewById(R.id.email);
                email.setText((String)userMap.get("email"));
                email.setMovementMethod(new ScrollingMovementMethod());
                Log.i("email", userMap.get("email") + "    ");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final ImageView imageView = findViewById(R.id.profile_photo);

        mStorage.child(post.uid).getBytes(5000000).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes , 0, bytes.length);
                imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                imageView.setImageResource(getRandomImage());
            }
        });

        final TextView emailText = findViewById(R.id.email);
        emailText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{post.email});
                startActivity(emailIntent);
            }
        });
    }

    /**
     * Method to generate random image.
     * @return random image
     */
    private int getRandomImage() {
        int randomNum = (int) (Math.random() * 8);
        switch (randomNum) {
            case 1:
                return R.drawable.avatar2;
            case 2:
                return R.drawable.avatar3;
            case 3:
                return R.drawable.avatar4;
            case 4:
                return R.drawable.avatar5;
            case 5:
                return R.drawable.avatar6;
            case 6:
                return R.drawable.avatar7;
            case 7:
                return R.drawable.avatar8;
            default:
                return R.drawable.avatar1;
        }
    }
}
