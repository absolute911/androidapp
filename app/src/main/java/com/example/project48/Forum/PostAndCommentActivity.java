package com.example.project48.Forum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project48.R;

public class PostAndCommentActivity extends AppCompatActivity {
    private TextView commentContentTextView;

    private static final int ADD_COMMENT_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.row_post_comment);

        commentContentTextView = findViewById(R.id.comment_content);

        Button addCommentButton = findViewById(R.id.add_comment_btn);
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostAndCommentActivity.this, AddCommentActivity.class);
                startActivityForResult(intent, ADD_COMMENT_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_COMMENT_REQUEST_CODE && resultCode == RESULT_OK) {
//            String username = data.getStringExtra("username");
            String comment = data.getStringExtra("comment");

            if (comment != null && !comment.isEmpty()) {
                String updatedComment = comment;
                commentContentTextView.setText(updatedComment);
            }
        }
    }
}
