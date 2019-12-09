package com.example.storysharing;

import java.io.Serializable;

public class User implements Serializable {

    public String fullname;
    public String bio;
    public String uid;


    public User() {

    }

    public User(String fullname, String bio, String uid) {
        this.fullname = fullname;
        this.bio = bio;
        this.uid = uid;
    }
}
