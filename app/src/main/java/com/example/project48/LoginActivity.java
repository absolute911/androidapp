package com.example.project48;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

// ...其他导入

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        editTextUsername = findViewById(R.id.editTextUserAccount);
        editTextPassword = findViewById(R.id.editTextPassword);

        Button loginButton = findViewById(R.id.buttonLogin2);
        if (loginButton != null) {
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    attemptLogin();
                }
            });
        }
    }

    private void attemptLogin() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // 输入验证（省略）

        // 执行登录
        performLogin(username, password);
    }

    private void performLogin(String username, String password) {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("username", username)
                        .add("password", password)
                        .build();

                Request request = new Request.Builder()
                        .url("您的登录接口 URL") // 替换为您的登录接口 URL
                        .post(formBody)
                        .build();

                Response response = client.newCall(request).execute();

                runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        // 登录成功
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainpageActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // 登录失败
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "登录出错", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}




