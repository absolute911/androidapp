package com.example.project48;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_activity); // 使用 intro_activity.xml 作为布局

        Button buttonWithoutLogin = findViewById(R.id.buttonWithoutLogin);
        if (buttonWithoutLogin != null) {
            buttonWithoutLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 启动 LoginActivity
                    Intent intent = new Intent(IntroActivity.this, MainpageActivity.class);
                    startActivity(intent);
                }
            });
        }


        // 登入按钮
        Button loginButton = findViewById(R.id.buttonLogin); // 确保这是您登入按钮的正确 ID
        if (loginButton != null) {
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 启动 LoginActivity
                    Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }

        // 注册按钮
        Button registerButton = findViewById(R.id.buttonRegister); // 确保这是您的注册按钮 ID
        if (registerButton != null) {
            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 启动 SignupActivity
                    Intent intent = new Intent(IntroActivity.this, SignupActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}





