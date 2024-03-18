package com.example.project48.Forum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project48.R;
import com.example.project48.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForumPostActivity extends AppCompatActivity {

    private TextView postContent;
    private TextView postTitle;
    private ListView commentsList;
    private Button addCommentButton;
    private ArrayList<String> comments;
    private ArrayAdapter<String> commentsAdapter;
    private String _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_post);

        try{
            getIntentId();
        }catch(Exception e){

        }
        getForumThreadDetails(_id);

        // Initialize views
        postContent = findViewById(R.id.postContent);
        commentsList = findViewById(R.id.commentsList);
        addCommentButton = findViewById(R.id.addCommentButton);
        postTitle = findViewById(R.id.postTitle);


        // Initialize comments list with mock data
        initializeCommentsList();

        // Setup add comment button
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumPostActivity.this, AddCommentToPostActivity.class);
                intent.putExtra("_id", _id);
                startActivity(intent);
            }
        });
    }

    private void initializeCommentsList() {
        comments = new ArrayList<>();
        commentsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, comments);
        commentsList.setAdapter(commentsAdapter);
    }

    private void getForumThreadDetails(String threadId) {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            URL URL = new URL();
            String url = URL.getURL() + "thread/" + threadId;

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            try (Response response = client.newCall(request).execute()) {
                final String responseBody = response.body() != null ? response.body().string() : null;
                if (response.isSuccessful() && responseBody != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseBody);
                        JSONArray commentsArray = jsonObject.getJSONArray("comments");

                        // Example post content
                        postContent.setText(jsonObject.getString("content"));
                        postTitle.setText(jsonObject.getString("title"));

                        for (int i = 0; i < commentsArray.length(); i++) {
                            JSONObject commentObject = commentsArray.getJSONObject(i);
                            String commentText = commentObject.getString("text");
                            addNewComment(commentText);
                            Log.d("commentText", "getForumThreadDetails: " + commentText);
                        }

                        runOnUiThread(() -> {
                            commentsAdapter.notifyDataSetChanged();
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // Handle error
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Handle error
            }
        }).start();
    }
    private void addNewComment(String comment) {
        // Add comment to the list and notify the adapter
        comments.add(comment);
        commentsAdapter.notifyDataSetChanged();
    }

    private void getIntentId() {
        Intent intent = getIntent();
        if (intent != null) {
            _id = intent.getStringExtra("_id");
            Log.d("_id", "Retrieved ID: " + _id); // Check the value after retrieving it
        }
    }
}