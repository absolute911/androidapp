package com.example.project48.Forum;

import androidx.appcompat.app.AppCompatActivity;

public class ForumThread  {
    private String title;
    private String content;
    private String id; // Assuming ID is a string, adjust as necessary

    // Constructor, getters, and setters
    public ForumThread(String title, String content, String id) {
        this.title = title;
        this.content = content;
        this.id = id;
    }

    // Getters
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getId() { return id; }
}
