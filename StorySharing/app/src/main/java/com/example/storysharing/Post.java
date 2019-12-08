package com.example.storysharing;

import java.io.Serializable;

public class Post implements Serializable {
    public String title;
    public String body;
    public boolean publish;
    public String pid; // post id
    public String uid; // user id

    public Post(String title, String body, boolean publish, String pid, String uid) {
        this.title = title;
        this.body = body;
        this.publish = publish;
        this.pid = pid;
        this.uid = uid;
    }
}
