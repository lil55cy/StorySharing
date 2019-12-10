package com.example.storysharing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SelectPostActivity extends AppCompatActivity {

    TextView title;
    TextView body;
    TextView email;
    ImageView picture;

    private static StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_post);

        findElements();
        post = (Post) getIntent().getSerializableExtra("post");
        title.setText(post.title);
        body.setText(post.body);
        email.setText(post.email);

        mStorage.child(post.uid).getBytes(5000000).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes , 0, bytes.length);
                picture.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //set default
                try {
                    picture.setImageResource(post.photoNum);
                } catch (Exception exc) {
                    picture.setImageResource(R.drawable.avatar1);
                }
            }
        });

        if (!post.email.equals("Anonymous")) {
            picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SelectPostActivity.this, ProfileInfoActivity.class);
                    intent.putExtra("post", post);
                    startActivity(intent);
                }
            });
        }
    }

    private void findElements() {
        title = findViewById(R.id.title);
        body = findViewById(R.id.body);
        email = findViewById(R.id.email);
        picture = findViewById(R.id.profile_image);

        body.setMovementMethod(new ScrollingMovementMethod());
    }
}
