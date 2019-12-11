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

import com.example.storysharing.ui.profile.ProfileFragment;
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
                startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
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


        Button takePictureButton = findViewById(R.id.takePictureButton2);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Log.i("marker", "1");
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Log.i("marker", "2");
                    startActivityForResult(takePictureIntent, 1);
                }
                // Log.i("marker", "3");
                ImageView iv = findViewById(R.id.profile_photo);
                Uri selectedImageURI = takePictureIntent.getData();

                try {
                    // Log.i("marker", "4");
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(EditProfileActivity.this.getContentResolver(), selectedImageURI);
                    // Log.i("marker", "5");
                    iv.setImageBitmap(imageBitmap);
                    // Log.i("marker", "6");
                    uploadedImage = imageBitmap;
                    // Log.i("marker", "7");

                } catch (Exception e) {
                    //
                    // Log.e("special", e.toString());
                    // Log.i("marker", "8");
                }
            }
        });


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mStorage.child(mAuth.getUid()).getBytes(5000000).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes , 0, bytes.length);
                        FirebaseUtility.rotateImage(bitmap);

                        float rotation = profilePhoto.getRotation();
                        if (rotation >= 0 && rotation < 90) {
                            profilePhoto.setRotation(90);
                        } else if (rotation >= 90 && rotation < 180) {
                            profilePhoto.setRotation(180);
                        } else if (rotation >= 180 && rotation < 270) {
                            profilePhoto.setRotation(270);
                        } else {
                            profilePhoto.setRotation(0);
                        }
                    }
                });

            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {

                Uri selectedImageURI = data.getData();

                ImageView iv = findViewById(R.id.profile_photo);
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageURI);
                    iv.setImageBitmap(imageBitmap);

                    uploadedImage = imageBitmap;

                } catch (Exception e) {
                    //
                }

            }
            else if (requestCode == 1) {
                //camera used
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ImageView iv = findViewById(R.id.profile_photo);
                iv.setImageBitmap(imageBitmap);

                uploadedImage = imageBitmap;

            }

        }
    }
}
