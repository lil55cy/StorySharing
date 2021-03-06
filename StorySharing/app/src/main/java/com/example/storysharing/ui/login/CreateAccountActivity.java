package com.example.storysharing.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.storysharing.FirebaseUtility;
import com.example.storysharing.MainActivity;
import com.example.storysharing.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;

public class CreateAccountActivity extends AppCompatActivity implements
        View.OnClickListener {


    /**
     * Unchangeable variable set to string "EmailPassword".
     */
    private static final String TAG = "EmailPassword";
    /**
     * Email text box.
     */
    private EditText mEmailField;
    /**
     * Password text box.
     */
    private EditText mPasswordField;
    /**
     * Bio text box.
     */
    private EditText mBioField;
    /**
     * Name text box.
     */
    private EditText mNameField;
    /**
     * Firebase authentication.
     */
    private FirebaseAuth mAuth;
    /**
     * Variable to hold bitmap of image.
     */
    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mEmailField = findViewById(R.id.username);
        mPasswordField = findViewById(R.id.password);
        mBioField = findViewById(R.id.bio);
        mNameField = findViewById(R.id.name);
        findViewById(R.id.create_account).setOnClickListener(this);

        final ImageView imageView = findViewById(R.id.profile_photo);

        int[] images = {R.drawable.avatar1,R.drawable.avatar2,R.drawable.avatar3,R.drawable.avatar4,
                R.drawable.avatar5, R.drawable.avatar6, R.drawable.avatar7};
        Random rand = new Random();
        int index = rand.nextInt(images.length);
        imageView.setImageResource(images[index]);

        image = BitmapFactory.decodeResource(getBaseContext().getResources(), images[index]);


        Button uploadImage = findViewById(R.id.uploadImageButton);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);
            }
        });

        Button takePictureButton = findViewById(R.id.takePictureButton);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 1);
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    /**
     * Method to switch to feed after user account is created.
     * @param user current account user
     */
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            String welcome = getString(R.string.welcome);
            Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.create_account) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
    }

    /**
     * Method to create new account.
     * @param email user email
     * @param password user password
     */
    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String name = ((EditText) findViewById(R.id.name)).getText().toString();
                            String bio = ((EditText) findViewById(R.id.bio)).getText().toString();
                            Log.d(TAG, "createUserWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseUtility.saveNewUser(name, bio, user.getUid(), user.getEmail());
                            if (image != null) {
                                FirebaseUtility.saveImageToStorage(image);
                            }

                            updateUI(user);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateAccountActivity.this, "Account already exists.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    /**
     * Method to check the format of email and length of password.
     * @return whether or not email and password are valid
     */
    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        String bio = mBioField.getText().toString();
        if (TextUtils.isEmpty(bio)) {
            mBioField.setError("Required.");
            valid = false;
        } else {
            mBioField.setError(null);
        }

        String name = mNameField.getText().toString();
        if (TextUtils.isEmpty(name)) {
            mNameField.setError("Required.");
            valid = false;
        } else {
            mNameField.setError(null);
        }

        return valid;
    }

    /**
     * Method to choose action based of request code.
     * @param requestCode whether or not to check result code
     * @param resultCode which action to perform
     * @param data intent
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {

                Uri selectedImageURI = data.getData();

                ImageView iv = findViewById(R.id.profile_photo);
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageURI);
                    iv.setImageBitmap(imageBitmap);

                    image = imageBitmap;

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

                image = imageBitmap;

            }

        }
    }
}
