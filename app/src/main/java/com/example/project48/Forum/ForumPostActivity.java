package com.example.project48.Forum;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project48.R;

import java.util.ArrayList;

public class ForumPostActivity extends AppCompatActivity {

    private TextView postContent;
    private ListView commentsList;
    private Button addCommentButton;
    private ArrayList<String> comments;
    private ArrayAdapter<String> commentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_post);

        // Initialize views
        postContent = findViewById(R.id.postContent);
        commentsList = findViewById(R.id.commentsList);
        addCommentButton = findViewById(R.id.addCommentButton);

        // Example post content
        postContent.setText("This is an example of a forum post content.");

        // Initialize comments list with mock data
        initializeCommentsList();

        // Setup add comment button
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Example action: Add a new comment
                addNewComment("New comment " + (comments.size() + 1));
            }
        });
    }

    private void initializeCommentsList() {
        comments = new ArrayList<>();
        // Adding some mock comments
        comments.add("Great post!");
        comments.add("Very informative, thanks for sharing.");
        comments.add("I have a question about this.");

        // Setting up adapter for ListView
        commentsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, comments);
        commentsList.setAdapter(commentsAdapter);
    }

    private void addNewComment(String comment) {
        // Add comment to the list and notify the adapter
        comments.add(comment);
        commentsAdapter.notifyDataSetChanged();
    }
}