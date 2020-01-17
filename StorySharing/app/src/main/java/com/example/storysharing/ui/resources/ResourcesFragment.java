package com.example.storysharing.ui.resources;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.storysharing.R;

public class ResourcesFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_resources, container, false);

        root.findViewById(R.id.hotline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialContactPhone("18002738255");
            }
        });

        root.findViewById(R.id.website).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openURL("https://suicidepreventionlifeline.org/");
            }
        });

        root.findViewById(R.id.help_someone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openURL("https://suicidepreventionlifeline.org/help-someone-else/");
            }
        });
        return root;
    }

    /**
     * Method to dial given phone number into default phone app.
     * @param phoneNumber input phone number
     */
    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

    /**
     * Method to open given url into default browser.
     * @param url input url
     */
    private void openURL(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}