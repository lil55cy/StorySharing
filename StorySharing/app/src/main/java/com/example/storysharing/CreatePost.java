package com.example.storysharing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import com.google.firebase.auth.FirebaseAuth;

public class CreatePost extends AppCompatActivity {
    private EditText title;
    private EditText body;
    private Switch anon;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        setTitle("Share a Story");

        title = findViewById(R.id.challenge_title);
        body = findViewById(R.id.description_input);
        anon = findViewById(R.id.switch1);
        mAuth = FirebaseAuth.getInstance();

        Button button = findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    String titleText = title.getText().toString();
                    String bodyText = body.getText().toString();
                    boolean isAnon = anon.isChecked();
                    String email;

                    if (isAnon) {
                        email = "Anonymous";
                    } else {
                        email = mAuth.getCurrentUser().getEmail();
                    }

                    FirebaseUtility.saveNewPost(titleText, bodyText, email, isAnon);

                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    }


    private boolean validateForm() {
        boolean valid = true;

        EditText title = findViewById(R.id.challenge_title);

        if (TextUtils.isEmpty(title.getText().toString())) {
            title.setError("Required.");
            valid = false;
        } else if (title.getText().length() > 50) {
            title.setError("Max 50 characters");
        } else {
            title.setError(null);
        }


        EditText description = findViewById(R.id.description_input);
        if (TextUtils.isEmpty(description.getText().toString())) {
            description.setError("Required.");
            valid = false;
        } else {
            description.setError(null);
        }

        return valid;
    }
}
