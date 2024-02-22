package com.example.project48;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
//        setupCategorySelector();

        // 图片添加布局点击监听器
        setupImageAddListener();

        // 定位图标点击监听器
        setupLocationIconListener();
    }

//    private void setupCategorySelector() {
//        AutoCompleteTextView categorySelector = findViewById(R.id.add_point_selector_textView);
//        String[] categories = getResources().getStringArray(R.array.categories_array); // 确保您在res/values/strings.xml中定义了categories_array
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
//        categorySelector.setAdapter(adapter);
//    }

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
                TextInputEditText ETcoordinates = findViewById(R.id.point_location_edit_text);
                ETcoordinates.setText("22.317507333012692, 114.1797521678627");
                // 实现定位或打开地图选择位置的逻辑
                Toast.makeText(AddToiletActivity.this, "Location icon clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addToiletBtnClick(View view) {
        TextInputEditText ETcoordinates = findViewById(R.id.point_location_edit_text);
        TextInputEditText ETname = findViewById(R.id.add_name_editText);
        TextInputEditText ETopenhour = findViewById(R.id.add_openhour_editText);
        TextInputEditText ETaddress = findViewById(R.id.add_address_editText);
        String name = ETname.getText().toString();
        String open_hours = ETopenhour.getText().toString();
        String address = ETaddress.getText().toString();
        String coordinates = ETcoordinates.getText().toString();
        Toast.makeText(this, "Thank you", Toast.LENGTH_SHORT).show();
        addToilet(name, address, open_hours, coordinates);


        Toast.makeText(this, "Thank you", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(AddToiletActivity.this, MainpageActivity.class);
        startActivity(intent);
    }
    private void addToilet(String name, String address, String open_hours, String coordinates) {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            String url = "http://192.168.50.143:3000/addToilet";

            // Create a JSON object with the comment data
            JSONObject toiletJson = new JSONObject();
            try {
                toiletJson.put("name", name);
                toiletJson.put("address", address);
                toiletJson.put("open_hours", open_hours);
                toiletJson.put("coordinates", coordinates);

            } catch (JSONException e) {
                e.printStackTrace();
                // Handle error
            }

            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), toiletJson.toString());

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                final String responseBody = response.body() != null ? response.body().string() : null;
                runOnUiThread(() -> {
                    if (response.isSuccessful() && responseBody != null) {
                        Log.d("addToilet", "addToilet: " + responseBody);
                        // Comment posted successfully
                        // Refresh the comment list
                    } else {
                        // Failed to post comment
                        // Handle error
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                // Handle error
            }
        }).start();
    }

}
