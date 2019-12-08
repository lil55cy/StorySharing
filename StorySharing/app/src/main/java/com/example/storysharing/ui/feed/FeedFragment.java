package com.example.storysharing.ui.feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.storysharing.Post;
import com.example.storysharing.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {

    private ListView listView;
    private Button createPostButton;
    private SwipeRefreshLayout srl;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private List<Post> posts = new ArrayList<>();

    private boolean alreadyLoaded = false;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_feed, container, false);

        listView = root.findViewById(R.id.tableLayout);

        return root;
    }
}