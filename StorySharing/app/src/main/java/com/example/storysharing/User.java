package com.example.storysharing;

import java.io.Serializable;

public class User implements Serializable {

    public String fullname;
    public String bio;
    public String uid;
    public String email;


    public User() {

    }

    public User(String fullname, String bio, String uid, String email) {
        this.fullname = fullname;
        this.bio = bio;
        this.uid = uid;
        this.email = email;
    }
}
