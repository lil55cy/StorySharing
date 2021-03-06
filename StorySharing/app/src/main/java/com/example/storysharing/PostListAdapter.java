package com.example.storysharing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class PostListAdapter extends ArrayAdapter<Post> {

    /**
     * Context of feed.
     */
    private Context mContext;
    /**
     * List item style (cell).
     */
    int mResource;
    /**
     * Firebase storage (using for images).
     */
    private static StorageReference mStorage = FirebaseStorage.getInstance().getReference();


    public PostListAdapter(Context context, int resource, List<Post> posts) {
        super(context, resource, posts);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String body = getItem(position).body;
        String title = getItem(position).title;
        String email = getItem(position).email;


        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource, parent, false);

        TextView titleTextView = convertView.findViewById(R.id.titleTextView);
        TextView emailTextView = convertView.findViewById(R.id.email);
        TextView bodyTextView = convertView.findViewById(R.id.bodyTextView);

        titleTextView.setText(title);
        emailTextView.setText(email);
        bodyTextView.setText(body);



        //set image
        final ImageView imageView = convertView.findViewById(R.id.profile_photo);
        String uid = getItem(position).uid;


        mStorage.child(uid).getBytes(5000000).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                if (getItem(position).publish) {
                    imageView.setImageResource(R.drawable.avatar8);
                } else {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageView.setImageBitmap(bitmap);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //set default random
                // imageView.setImageResource(R.drawable.avatar1);
                int i = getRandomImage();
                getItem(position).photoNum = i;
                imageView.setImageResource(i);
            }
        });


        return convertView;
    }

    /**
     * Method to generate random image.
     * @return random image
     */
    private int getRandomImage() {
        int randomNum = (int) (Math.random() * 7);
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
            default:
                return R.drawable.avatar1;
        }
    }
}
