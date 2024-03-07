package com.example.project48.Forum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project48.R;
import com.example.project48.SessionManager;
import com.example.project48.UserCommentAdapter;

import org.json.JSONObject;

public class AddPostActivity extends AppCompatActivity {

    private EditText addTextTitle;
    private EditText addTextContent;

    private EditText addTextId;
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.row_add_post);

        addTextId = findViewById(R.id.add_id_editText);
        addTextTitle = findViewById(R.id.add_title_editText);
        addTextContent = findViewById(R.id.add_content_editText);
        buttonSubmit = findViewById(R.id.btnAddPost);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = addTextId.getText().toString();
                String title = addTextTitle.getText().toString();
                String content = addTextContent.getText().toString();

                if (title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(AddPostActivity.this, "Please enter title and content", Toast.LENGTH_SHORT).show();
                } else {
                    // Pass the entered title and content back to the ForumActivity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("id", id);
                    resultIntent.putExtra("title", title);
                    resultIntent.putExtra("content", content);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }
}
