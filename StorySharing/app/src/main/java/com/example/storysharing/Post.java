package com.example.storysharing;

import java.io.Serializable;

public class Post implements Serializable {
    /**
     * Title of post.
     */
    public String title;
    /**
     * Post body.
     */
    public String body;
    /**
     * Email address of user who created post.
     */
    public String email;
    /**
     * Whether or not post should be anonymous.
     */
    public boolean publish;
    /**
     * Post id.
     */
    public String pid;
    /**
     * ID of user who created post.
     */
    public String uid;
    /**
     * Photo number.
     */
    public int photoNum;

    public Post() {

    }

    public Post(String title, String body, String email, boolean publish, String pid, String uid) {
        this.title = title;
        this.body = body;
        this.email = email;
        this.publish = publish;
        this.pid = pid;
        this.uid = uid;
        this.photoNum = -1;
    }
}
