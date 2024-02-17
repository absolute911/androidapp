package com.example.project48;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class detailActivity extends AppCompatActivity {
    private String latitude ="123.123";
    private String longitude = "321.321";
    private TextView poi_detail_name_textView;
    private TextView activity_poi_last_opentime_textView;
    private TextView activity_poi_location_textView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_detail);

        poi_detail_name_textView = findViewById(R.id.poi_detail_name_textView); // Make sure to replace 'your_text_view_id' with the actual ID of your TextView in the layout.
        activity_poi_last_opentime_textView = findViewById(R.id.activity_poi_last_opentime_textView);
        activity_poi_location_textView = findViewById(R.id.activity_poi_location_textView);

        // Convert latitude and longitude from String to double
        double lat = Double.parseDouble(latitude);
        double lon = Double.parseDouble(longitude);

        // Fetch the details as soon as the page loads
        getDetail(lat, lon);
    }
    //return json sample
    //{
    //    "rating": 3,
    //        "comments": [],
    //    "img": [],
    //    "_id": "6502fea138490f96fdbad3b2",
    //        "name": "東平洲公廁",
    //        "address": "東平洲",
    //        "open_hours": "24 小時",
    //        "coordinates": "22.544192,114.432503",
    //        "facility": [],
    //    "image": []
    //}

    private void getDetail(double latitude, double longitude) {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            String url = "http://192.168.50.143:3000/items/nearest?latitude=" + latitude + "&longitude=" + longitude;

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                final String responseBody = response.body() != null ? response.body().string() : null;
                runOnUiThread(() -> {
                    if (response.isSuccessful() && responseBody != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(responseBody);
                            // Extract data as before
                            String name = jsonObject.getString("name");
                            String open_hours = jsonObject.getString("open_hours");
                            String address = jsonObject.getString("address");
                            // Update the UI
                            poi_detail_name_textView.setText(name);
                            activity_poi_last_opentime_textView.setText(open_hours);
                            activity_poi_location_textView.setText(address);
                            // Additional UI updates as needed
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Handle JSON parsing errors
                        }
                    } else {
                        Toast.makeText(detailActivity.this, "fetch fail", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
