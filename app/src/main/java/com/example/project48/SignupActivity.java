package com.example.project48;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class SignupActivity extends AppCompatActivity {

    private EditText editTextUserName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        // 初始化控件
        editTextUserName = findViewById(R.id.editTextUserName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPasswordConfirm = findViewById(R.id.editTextPasswordConfirm);

        Button buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        // 获取用户输入
        String userName = editTextUserName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString();
        String passwordConfirm = editTextPasswordConfirm.getText().toString();

        // 输入验证
        if (TextUtils.isEmpty(userName)) {
            editTextUserName.setError("请输入用户名");
            return;
        }

        if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("请输入有效的电子邮件地址");
            return;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            editTextPassword.setError("密码长度至少为6");
            return;
        }

        if (!password.equals(passwordConfirm)) {
            editTextPasswordConfirm.setError("两次输入的密码不一致");
            return;
        }

        // 向后端服务器发送请求
        sendRegistrationRequest(userName, email, password);
    }

    private void sendRegistrationRequest(String userName, String email, String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null; // 将 response 声明移到这里
                try {
                    OkHttpClient client = new OkHttpClient();

                    RequestBody requestBody = new FormBody.Builder()
                            .add("username", userName)
                            .add("email", email)
                            .add("password", password)
                            .build();

                    Request request = new Request.Builder()
                            .url("您的注册接口 URL") // 替换为您的注册接口 URL
                            .post(requestBody)
                            .build();

                    response = client.newCall(request).execute(); // 赋值操作

                    // 这里需要检查 response 是否为 null
                    if (response != null && response.isSuccessful()) {
                        runOnUiThread(() -> Toast.makeText(SignupActivity.this, "注册成功", Toast.LENGTH_SHORT).show());
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        runOnUiThread(() -> Toast.makeText(SignupActivity.this, "注册失败", Toast.LENGTH_SHORT).show());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(SignupActivity.this, "注册出错", Toast.LENGTH_SHORT).show());
                } finally {
                    if (response != null) {
                        response.close(); // 确保关闭 response
                    }
                }
            }
        }).start();
    }

}




