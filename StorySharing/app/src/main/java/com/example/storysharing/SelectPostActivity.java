package com.example.storysharing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
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

    /**
     * Title text.
     */
    TextView title;
    /**
     * Body text.
     */
    TextView body;
    /**
     * Email text.
     */
    TextView email;
    /**
     * Profile picture.
     */
    ImageView picture;
    /**
     * Delete button.
     */
    Button delete;
    /**
     * Firebase database.
     */
    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    /**
     * Firebase storage (used for images).
     */
    private static StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    /**
     * Firebase authentication.
     */
    private static FirebaseAuth mAuth;
    /**
     * Selected post.
     */
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_post);

        findElements();
        mAuth = FirebaseAuth.getInstance();
        post = (Post) getIntent().getSerializableExtra("post");
        title.setText(post.title);
        body.setText(post.body);
        email.setText(post.email);

        if (!post.email.equals("Anonymous")) {
            email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{post.email});
                    startActivity(emailIntent);
                }
            });
        }

        mStorage.child(post.uid).getBytes(5000000).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                if (post.publish) {
                    picture.setImageResource(R.drawable.avatar8);
                } else {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    picture.setImageBitmap(bitmap);
                }
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

        if (mAuth.getUid().equals(post.uid) || mAuth.getUid().equals("9i8hFxVElYdJ4zCzUw8udw3AjoW2")) {
            delete.setVisibility(View.VISIBLE);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDatabase.child("posts").child(post.pid).removeValue();
                    startActivity(new Intent(SelectPostActivity.this, MainActivity.class));
                }
            });
        }
    }

    /**
     * Find elements on app screen.
     */
    private void findElements() {
        title = findViewById(R.id.title);
        body = findViewById(R.id.body);
        email = findViewById(R.id.email);
        picture = findViewById(R.id.profile_image);
        delete = findViewById(R.id.delete_button);

        body.setMovementMethod(new ScrollingMovementMethod());
        delete.setVisibility(View.INVISIBLE);
    }
}
