package com.example.project48;

// MainActivity.java
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class filterActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton btnMale, btnFemale, btnBabyChanging, btnAccessible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_activity);

        // 初始化按鈕
        btnMale = findViewById(R.id.imageButton_male);
        btnFemale = findViewById(R.id.imageButton_female);
        btnBabyChanging = findViewById(R.id.imageButton_baby_changing);
        btnAccessible = findViewById(R.id.imageButton_accessible);

        // 設置點擊監聽器
        btnMale.setOnClickListener(this);
        btnFemale.setOnClickListener(this);
        btnBabyChanging.setOnClickListener(this);
        btnAccessible.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton_male:
                // 點擊男廁按鈕
                Toast.makeText(this, "篩選男廁", Toast.LENGTH_SHORT).show();
                // 在此執行篩選男廁的相關操作
                break;
            case R.id.imageButton_female:
                // 點擊女廁按鈕
                Toast.makeText(this, "篩選女廁", Toast.LENGTH_SHORT).show();
                // 在此執行篩選女廁的相關操作
                break;
            case R.id.imageButton_baby_changing:
                // 點擊育嬰室按鈕
                Toast.makeText(this, "篩選育嬰室", Toast.LENGTH_SHORT).show();
                // 在此執行篩選育嬰室的相關操作
                break;
            case R.id.imageButton_accessible:
                // 點擊傷殘洗手間按鈕
                Toast.makeText(this, "篩選傷殘洗手間", Toast.LENGTH_SHORT).show();
                // 在此執行篩選傷殘洗手間的相關操作
                break;
        }
    }
}

