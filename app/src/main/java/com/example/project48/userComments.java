package com.example.project48;

public class userComments {

    private String username;
    private String comment;

    // Constructor
    public userComments(String username, String comment) {
        this.username = username;
        this.comment = comment;
    }

    // Getters

    public String getUserName() {
        return username;
    }

    public String getComment() {
        return comment;
    }
}
