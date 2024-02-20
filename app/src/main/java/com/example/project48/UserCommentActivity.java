package com.example.project48;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserCommentActivity extends AppCompatActivity {
    private EditText commentEditText;
    private Button addButton;
    private RecyclerView commentRecyclerView;
    private UserCommentAdapter commentAdapter;
    private List<String> commentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_comment);

        commentEditText = findViewById(R.id.post_detail_comment);
        addButton = findViewById(R.id.post_detail_add_comment_btn);
        commentRecyclerView = findViewById(R.id.rv_comment);

        // Initialize the comments list and adapter
        commentsList = new ArrayList<>();
        commentAdapter = new UserCommentAdapter(commentsList);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentRecyclerView.setAdapter(commentAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentEditText.getText().toString().trim();
                if (!comment.isEmpty()) {
                    commentsList.add(comment);
                    commentAdapter.notifyDataSetChanged();
                    commentEditText.setText("");
                }
            }
        });
    }
}
