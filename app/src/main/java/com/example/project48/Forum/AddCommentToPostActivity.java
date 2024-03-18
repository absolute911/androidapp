package com.example.project48.Forum;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project48.R;
import com.example.project48.SessionManager;
import com.example.project48.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddCommentToPostActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText commentEditText;
    private String _id;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_comments_to_post);

        //usernameEditText = findViewById(R.id.add_name_editText);
        commentEditText = findViewById(R.id.add_comment_editText);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        username = sessionManager.getUsername();

        getIntentId();

        Button submitButton = findViewById(R.id.btnAddComment);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String username = usernameEditText.getText().toString();
                String comment = commentEditText.getText().toString();
                sendCommentRequest(comment);
                finish();
            }
        });
    }

    private void sendCommentRequest(String commentText) {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            URL URL = new URL();
            String url = URL.getURL() + "thread/" + _id + "/comment";

            // Create a JSON object with the comment data
            JSONObject requestBody = new JSONObject();
            try {
                requestBody.put("username", username);
                requestBody.put("text", commentText);
                //requestBody.put("images", new JSONArray());
            } catch (JSONException e) {
                e.printStackTrace();
                // Handle error
            }

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(requestBody.toString(), JSON);

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                // Handle response if needed
            } catch (IOException e) {
                e.printStackTrace();
                // Handle error
            }
        }).start();
    }

    private void getIntentId() {
        Intent intent = getIntent();
        if (intent != null) {
            _id = intent.getStringExtra("_id");
        }
    }
}