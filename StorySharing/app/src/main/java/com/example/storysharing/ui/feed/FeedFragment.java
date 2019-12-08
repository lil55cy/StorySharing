package com.example.storysharing.ui.feed;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.storysharing.Post;
import com.example.storysharing.PostListAdapter;
import com.example.storysharing.R;
import com.example.storysharing.SelectPostActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {

    private ListView listView;
    private Button createPostButton;
    private SwipeRefreshLayout srl;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private List<Post> posts = new ArrayList<>();

    private boolean alreadyLoaded = false;

    PostListAdapter postListAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_feed, container, false);

        listView = root.findViewById(R.id.tableLayout);
        postListAdapter = new PostListAdapter(getContext(), R.layout.post_cell, posts);
        listView.setAdapter(postListAdapter);

        fetchPosts();

        srl = root.findViewById(R.id.swiperefresh);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fetchPosts();
                        srl.setRefreshing(false);}
                });
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), SelectPostActivity.class);
                intent.putExtra("post", posts.get(i));
                startActivity(intent);
            }
        });



        return root;
    }


    public void fetchPosts() {
        if (alreadyLoaded) {
            posts.clear();
        }

        mDatabase.child("posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    Post post = noteDataSnapshot.getValue(Post.class);
                    posts.add(0, post);
                    postListAdapter.notifyDataSetChanged();
                }
            }
            public void onCancelled(@NonNull DatabaseError err) {

            }
        });

        alreadyLoaded = true;
    }
}