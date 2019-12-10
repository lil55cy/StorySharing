package com.example.storysharing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
    }

     */

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    private FirebaseAuth mAuth;
    private Bitmap uploadedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Button updateProfile = findViewById(R.id.update_profile);
        final EditText editName = findViewById(R.id.edit_name);
        final EditText editBio = findViewById(R.id.edit_bio);

        mAuth = FirebaseAuth.getInstance();

        updateProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseUtility.saveNewUser(editName.getText().toString(), editBio.getText().toString(), mAuth.getUid(), mAuth.getCurrentUser().getEmail());
                Toast.makeText(EditProfileActivity.this, "Your modification has been saved!", Toast.LENGTH_SHORT).show();
            }
        });


        mDatabase.child("userinfo").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Map user = (Map)dataSnapshot.getValue();
                editName.setText((String)user.get("fullname"));
                editBio.setText((String)user.get("bio"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final ImageView profilePhoto = findViewById(R.id.profile_photo);
        mStorage.child(mAuth.getUid()).getBytes(5000000).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes , 0, bytes.length);
                profilePhoto.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //set default
                profilePhoto.setImageResource(R.drawable.avatar1);
            }
        });


        Button uploadPhoto = findViewById(R.id.upload_image);
        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);
                if (uploadedImage != null) {
                    //upload image to storage if they added one
                    FirebaseUtility.saveImageToStorage(uploadedImage);
                }
                ImageView iv = findViewById(R.id.profile_photo);
                Uri selectedImageURI = intent.getData();

                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(EditProfileActivity.this.getContentResolver(), selectedImageURI);
                    iv.setImageBitmap(imageBitmap);
                    uploadedImage = imageBitmap;
                    Log.i("photo", "is this being called?");

                } catch (Exception e) {
                    //
                }
            }
        });


    }
}
