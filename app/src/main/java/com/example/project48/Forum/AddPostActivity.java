package com.example.project48.Forum;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.project48.R;
import com.example.project48.misc.SessionManager;
import com.example.project48.misc.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddPostActivity extends AppCompatActivity {

    private EditText addTextTitle;
    private EditText addTextContent;

    private EditText addTextId;
    private Button buttonSubmit;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_post);

        addTextTitle = findViewById(R.id.add_title_editText);
        addTextContent = findViewById(R.id.add_content_editText);
        buttonSubmit = findViewById(R.id.btnAddPost);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        username = sessionManager.getUsername();

        // 设置 Toolbar
        Toolbar toolbar = findViewById(R.id.add_point_toolBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 关闭当前 Activity
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = addTextTitle.getText().toString();
                String content = addTextContent.getText().toString();

                if (title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(AddPostActivity.this, "Please enter title and content", Toast.LENGTH_SHORT).show();
                } else {
                    createNewPost(title, content);
                    finish();
                }
            }
        });
    }

    private void createNewPost(String title, String content) {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            URL URL = new URL();
            String url = URL.getURL() + "thread";

            // Create a JSON object with the request data
            JSONObject requestBody = new JSONObject();
            try {
                requestBody.put("title", title);
                requestBody.put("content", content);
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
}