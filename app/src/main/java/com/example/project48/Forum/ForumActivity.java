package com.example.project48.Forum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project48.R;
import com.example.project48.misc.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



public class ForumActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ThreadRecyclerViewAdapter adapter;
    private ArrayList<ForumThread> threads = new ArrayList<>();
    private Button button_add_new_post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum);

        recyclerView = findViewById(R.id.ThreadListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ThreadRecyclerViewAdapter(this, threads);
        recyclerView.setAdapter(adapter);
        button_add_new_post = findViewById(R.id.button_add_new_post);

        // 设置 Toolbar
        Toolbar toolbar = findViewById(R.id.add_post_toolBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 关闭当前 Activity
            }
        });


        button_add_new_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumActivity.this, AddPostActivity.class);
                startActivity(intent);
            }
        });

        // Fetch the details as soon as the page loads
        getForumlList();

    }

    private void getForumlList() {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            URL URL = new URL();
            String url = URL.getURL() + "getAllThreads";

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            try (Response response = client.newCall(request).execute()) {
                final String responseBody = response.body() != null ? response.body().string() : null;
                if (response.isSuccessful() && responseBody != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(responseBody);
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
                            ThreadRecyclerViewAdapter adapter = new ThreadRecyclerViewAdapter(ForumActivity.this, threads, new ThreadRecyclerViewAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(ForumThread thread) {
                                    Log.d("Thread ID", "Clicked Thread ID: " + thread.getId()); // Check the thread ID
                                    Intent intent = new Intent(ForumActivity.this, ForumPostActivity.class);
                                    intent.putExtra("_id", thread.getId());
                                    startActivity(intent);
                                }
                            });
                            recyclerView.setAdapter(adapter);
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


