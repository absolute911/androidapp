package com.example.project48;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.textfield.TextInputLayout;

public class AddToiletActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_toilet); // 确保您有一个名为 activity_add_toilet.xml 的布局文件

        // 设置 Toolbar
        Toolbar toolbar = findViewById(R.id.add_point_toolBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 关闭当前 Activity
            }
        });

        // 分类选择器设置
        setupCategorySelector();

        // 图片添加布局点击监听器
        setupImageAddListener();

        // 定位图标点击监听器
        setupLocationIconListener();
    }

    private void setupCategorySelector() {
        AutoCompleteTextView categorySelector = findViewById(R.id.add_point_selector_textView);
        String[] categories = getResources().getStringArray(R.array.categories_array); // 确保您在res/values/strings.xml中定义了categories_array
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
        categorySelector.setAdapter(adapter);
    }

    private void setupImageAddListener() {
        RelativeLayout pictureLayout = findViewById(R.id.add_point_picture_layout);
        pictureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 实现打开图片选择器或相机的逻辑
                Toast.makeText(AddToiletActivity.this, "Add picture clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupLocationIconListener() {
        TextInputLayout locationLayout = findViewById(R.id.point_location_text_layout);
        locationLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 实现定位或打开地图选择位置的逻辑
                Toast.makeText(AddToiletActivity.this, "Location icon clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
