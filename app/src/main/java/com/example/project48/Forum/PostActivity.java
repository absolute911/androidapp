package com.example.project48.Forum;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project48.R;
import com.example.project48.SessionManager;
import com.example.project48.UserCommentActivity;
import com.example.project48.UserCommentAdapter;
import com.example.project48.userComments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostActivity extends AppCompatActivity {
    private EditText postEditText;
    private Button addButton;
    private RecyclerView commentRecyclerView;

    private UserCommentAdapter commentAdapter;
    private JSONObject nearestToilet;
    private String toiletName, toiletID;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.row_add_post);

//        getDataFromIntent();

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        username = sessionManager.getUsername();

        postEditText = findViewById(R.id.detail_post);
        addButton = findViewById(R.id.detail_add_post_btn);
//        postRecyclerView = findViewById(R.id.rv_post);

        // Initialize the comments list and adapter
//        postRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        getPostList();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String post = postEditText.getText().toString().trim();

                if (!post.isEmpty()) {
                    if (sessionManager.isLoggedIn()) {
                        // User is logged in, proceed to the next activity or show logged in state
//                        toiletPost(username, post);
                    } else {
                        // User is not logged in, redirect to login activity
                        //userName.setText("Please login");
                    }

                }
            }
        });
    }

//    The following coding from UserCommentActivity

//    private void postToiletComment(int rating, String username, String text) {
//        new Thread(() -> {
//            OkHttpClient client = new OkHttpClient();
//            String url = "http://192.168.50.143:3000/addToiletComment/" + toiletID;
//
//            // Create a JSON object with the comment data
//            JSONObject commentJson = new JSONObject();
//            try {
//                commentJson.put("rating", rating);
//                commentJson.put("username", username);
//                commentJson.put("text", text);
//            } catch (JSONException e) {
//                e.printStackTrace();
//                // Handle error
//            }
//
//            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), commentJson.toString());
//
//            Request request = new Request.Builder()
//                    .url(url)
//                    .post(requestBody)
//                    .build();
//
//            try (Response response = client.newCall(request).execute()) {
//                final String responseBody = response.body() != null ? response.body().string() : null;
//                runOnUiThread(() -> {
//                    if (response.isSuccessful() && responseBody != null) {
//                        // Comment posted successfully
//                        // Refresh the comment list
//                        getCommentList();
//                    } else {
//                        // Failed to post comment
//                        // Handle error
//                    }
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//                // Handle error
//            }
//        }).start();
//    }

//    private void getDataFromIntent() {
//        String json = getIntent().getStringExtra("json");
//        if (json != null) {
//            try {
//                nearestToilet = new JSONObject(json);
//                JSONObject toiletObject = nearestToilet.getJSONObject("nearestToilet");
//                toiletName  = toiletObject.getString("name");
//                toiletID = toiletObject.getString("_id");
//                // Process the JSON data here
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } else {
//            Log.e("NextActivity", "No JSON data found in intent");
//        }
//    }


//    private void getCommentList() {
//        new Thread(() -> {
//            OkHttpClient client = new OkHttpClient();
//            String url = "http://192.168.50.143:3000/getToiletComment/" + toiletID;
//
//            Request request = new Request.Builder()
//                    .url(url)
//                    .get()
//                    .build();
//
//            try (Response response = client.newCall(request).execute()) {
//                final String responseBody = response.body() != null ? response.body().string() : null;
//                runOnUiThread(() -> {
//                    if (response.isSuccessful() && responseBody != null) {
//                        try {
//                            JSONArray jsonArray = new JSONArray(responseBody);
//                            ArrayList<userComments> userCommentList = new ArrayList<>(); // Add this line
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                Log.d("getCommentList", "getCommentList: " + jsonObject);
//                                userComments comment = new userComments(
//                                        jsonObject.getString("username"),
//                                        jsonObject.getString("text")
//                                );
//                                userCommentList.add(comment);
//                            }
//                            commentRecyclerView = findViewById(R.id.rv_comment);
//                            UserCommentAdapter adapter = new UserCommentAdapter(this, userCommentList); // Update this line
//                            commentRecyclerView.setAdapter(adapter);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            // Handle error
//                        }
//
//                    } else {
//                        //Toast.makeText(listActivity.this, "fetch fail", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }
}
