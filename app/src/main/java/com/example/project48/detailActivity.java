package com.example.project48;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    //hkmu jc coordinates
    //22.317507333012692, 114.1797521678627
    private String latitude ="22.317507333012692";
    private String longitude = "114.1797521678627";
    private TextView poi_detail_name_textView;
    private TextView activity_poi_last_opentime_textView;
    private TextView activity_poi_location_textView;
    private TextView point_detail_report_text;
    private TextView activity_poi_distance_textView;
    private TextView point_detail_rating_text;

    private String json;

    private String ToiletID = "";

    private Toilet selectedToilet;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_detail);

        try{
            getIntentId();
        }catch (Exception e){

        }

        poi_detail_name_textView = findViewById(R.id.poi_detail_name_textView); // Make sure to replace 'your_text_view_id' with the actual ID of your TextView in the layout.
        activity_poi_last_opentime_textView = findViewById(R.id.activity_poi_last_opentime_textView);
        activity_poi_location_textView = findViewById(R.id.activity_poi_location_textView);
        point_detail_report_text = findViewById(R.id.point_detail_report_text);
        activity_poi_distance_textView = findViewById(R.id.activity_poi_distance_textView);
        point_detail_rating_text = findViewById(R.id.point_detail_rating_text);

        point_detail_report_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goComment();
            }
        });

        // Convert latitude and longitude from String to double
        double lat = Double.parseDouble(latitude);
        double lon = Double.parseDouble(longitude);

        // Fetch the details as soon as the page loads
        if (ToiletID == ""){
            getDetail(lat, lon);
        }
        else{
            getDetailById(lat, lon);
            Log.d("selectedToilet", "onCreate: getting");
        }
    }

    private void getDetailById( double latitude, double longitude) {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            String url = "http://192.168.50.143:3000/items/getToiletById/" + ToiletID + "?latitude=" + latitude + "&longitude=" + longitude;
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
                            if (jsonObject.has("nearestToilet")) {
                                JSONObject toiletDetailObject = jsonObject.getJSONObject("nearestToilet");
                                json = jsonObject.toString();
                                String name = toiletDetailObject.getString("name");
                                String open_hours = toiletDetailObject.getString("open_hours");
                                String address = toiletDetailObject.getString("address");
                                String distance = jsonObject.getString("distance"); // If distance is relevant for this call
                                String rating = toiletDetailObject.getString("rating");
                                // Update the UI
                                poi_detail_name_textView.setText(name);
                                activity_poi_last_opentime_textView.setText(open_hours);
                                activity_poi_location_textView.setText(address);
                                activity_poi_distance_textView.setText(distance); // If you're showing distance
                                point_detail_rating_text.setText("評分" + rating);

                            } else {
                                // Handle the case where the "toiletDetail" key is missing in the JSON response
                                // You can show an error message or handle it as needed
                            }
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
                            if (jsonObject.has("nearestToilet")) {
                                JSONObject nearestToiletObject = jsonObject.getJSONObject("nearestToilet");
                                // After parsing the JSON response
                                json = jsonObject.toString();
                                String name = nearestToiletObject.getString("name");
                                String open_hours = nearestToiletObject.getString("open_hours");
                                String address = nearestToiletObject.getString("address");
                                String distance = jsonObject.getString("distance");
                                String rating = nearestToiletObject.getString("rating");
                                // Update the UI
                                poi_detail_name_textView.setText(name);
                                activity_poi_last_opentime_textView.setText(open_hours);
                                activity_poi_location_textView.setText(address);
                                activity_poi_distance_textView.setText(distance);
                                point_detail_rating_text.setText("評分" + rating);

                            } else {
                                // Handle the case where the "nearestToilet" key is missing in the JSON response
                                // You can show an error message or handle it as needed
                            }
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


    private void goComment(){
        Intent intent = new Intent(detailActivity.this, UserCommentActivity.class);
        intent.putExtra("json", json); // Replace 'jsonString' with your JSON string
        startActivity(intent);

    }

    private void getIntentId() {
        Intent intent = getIntent();
        if (intent != null) {
            selectedToilet = intent.getParcelableExtra("toilet");
            Log.d("selectedToilet", "selectedToilet: " + selectedToilet.getId() + selectedToilet.getName());
            ToiletID = selectedToilet.getId();
        }
    }


    public void directionPointBtnClick(View view) {
        Intent intent = new Intent(detailActivity.this, MapActivity.class);
        intent.putExtra("json", json); // Replace 'jsonString' with your JSON string
        startActivity(intent);
    }
}
