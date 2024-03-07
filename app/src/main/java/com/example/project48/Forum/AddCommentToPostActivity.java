package com.example.project48.Forum;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project48.R;

public class AddCommentToPostActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText commentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_comments_to_post);

        //usernameEditText = findViewById(R.id.add_name_editText);
        commentEditText = findViewById(R.id.add_comment_editText);

        Button submitButton = findViewById(R.id.btnAddComment);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String username = usernameEditText.getText().toString();
                String comment = commentEditText.getText().toString();

                Intent intent = new Intent();
                //intent.putExtra("username", username);
                intent.putExtra("comment", comment);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}