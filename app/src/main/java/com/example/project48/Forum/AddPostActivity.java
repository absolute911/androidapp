package com.example.project48.Forum;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project48.R;
import com.example.project48.SessionManager;
import com.example.project48.UserCommentAdapter;

import org.json.JSONObject;

public class AddPostActivity extends AppCompatActivity {
    private EditText postEditText;
    private Button addButton;
    private RecyclerView postRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.row_add_post);

//        getDataFromIntent();

        SessionManager sessionManager = new SessionManager(getApplicationContext());
//        username = sessionManager.getUsername();

        postEditText = findViewById(R.id.detail_post);
        addButton = findViewById(R.id.detail_add_post_btn);
        postRecyclerView = findViewById(R.id.rv_post);

        // Initialize the comments list and adapter
        postRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        getCommentList();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String post = postEditText.getText().toString().trim();

                if (!post.isEmpty()) {
                    if (sessionManager.isLoggedIn()) {
                        // User is logged in, proceed to the next activity or show logged in state
//                        toiletAddPost(post);
                    } else {
                        // User is not logged in, redirect to login activity
                        //userName.setText("Please login");
                    }
                }
            }
        });
    }
}
