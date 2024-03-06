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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



public class ForumActivity extends AppCompatActivity implements ThreadRecyclerViewAdapter.OnItemClickListener {

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
