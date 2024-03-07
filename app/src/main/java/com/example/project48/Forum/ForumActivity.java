package com.example.project48.Forum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project48.IntroActivity;
import com.example.project48.LoginActivity;
import com.example.project48.R;
import android.app.Activity;
import com.example.project48.Forum.ForumThread; // Replace with the actual package and class name

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



public class ForumActivity extends AppCompatActivity implements ThreadRecyclerViewAdapter.OnItemClickListener {
    private static final int REQUEST_ADD_POST = 1;
    private RecyclerView recyclerView;
    private ThreadRecyclerViewAdapter adapter;
    private ArrayList<ForumThread> threads = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum);

        recyclerView = findViewById(R.id.ThreadListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        adapter = new ThreadRecyclerViewAdapter(this, threads);
        adapter = new ThreadRecyclerViewAdapter(threads);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        // Fetch the details as soon as the page loads
        getForumlList();

        Button btnAddPost = findViewById(R.id.btnAddPost);
        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the add post activity
                Intent intent = new Intent(ForumActivity.this, AddPostActivity.class);
                startActivityForResult(intent, REQUEST_ADD_POST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD_POST && resultCode == Activity.RESULT_OK) {
            String title = data.getStringExtra("title");
            String content = data.getStringExtra("content");
            String id = data.getStringExtra("id");

            // Create a new ForumThread object with the entered title and content
            ForumThread newThread = new ForumThread(title, content, id);
            threads.add(0, newThread); // Add the new thread at the beginning of the list
            adapter.notifyDataSetChanged();
        }
    }


    public void onItemClick(ForumThread thread) {
        // Handle item click event
        Intent intent = new Intent(this, PostAndCommentActivity.class);
        startActivity(intent);
    }

    private void getForumlList() {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            String url = "http://192.168.50.143:3000/getAllThreads";

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            try (Response response = client.newCall(request).execute()) {
                final String responseBody = response.body() != null ? response.body().string() : null;
                if (response.isSuccessful() && responseBody != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(responseBody);
                        Log.d("getForumlList", "getForumlList: " + jsonArray.getJSONObject(0));
                        ArrayList<ForumThread> threadList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ForumThread thread = new ForumThread(
                                    jsonObject.getString("title"),
                                    "content",
                                    jsonObject.getString("_id")
                            );
                            threadList.add(thread);
                        }
                        runOnUiThread(() -> {
                            threads.clear();
                            threads.addAll(threadList);
                            adapter.notifyDataSetChanged();
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


}
