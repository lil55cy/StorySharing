package com.example.storysharing;

import java.io.Serializable;

public class User implements Serializable {

    /**
     * User's name.
     */
    public String fullname;
    /**
     * User's bio.
     */
    public String bio;
    /**
     * User's id.
     */
    public String uid;
    /**
     * User's email address.
     */
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
