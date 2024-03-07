package com.example.project48;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
//<<<<<<< HEAD
import android.view.View;
//=======
import android.util.Log;
//>>>>>>> origin/master
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class listActivity extends AppCompatActivity {

    private String latitude ="22.317507333012692";
    private String longitude = "114.1797521678627";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toilet_list);

        double lat = Double.parseDouble(latitude);
        double lon = Double.parseDouble(longitude);

        // Fetch the details as soon as the page loads
        getDetailList(lat, lon);

    }


    private void getDetailList(double latitude, double longitude) {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            String url = "http://192.168.50.143:3000/items/list?latitude=" + latitude + "&longitude=" + longitude;

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                final String responseBody = response.body() != null ? response.body().string() : null;
                runOnUiThread(() -> {
                    if (response.isSuccessful() && responseBody != null) {
                        try {
                            JSONArray jsonArray = new JSONArray(responseBody);
                            ArrayList<Toilet> toiletList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Toilet toilet = new Toilet(
                                        jsonObject.getInt("rating"),
                                        jsonObject.getString("name"),
                                        jsonObject.getDouble("distance"),
                                        jsonObject.getString("_id"),
                                        jsonObject.getString("open_hours"),
                                        jsonObject.getString("address")

                                );
                                toiletList.add(toilet);
                            }
                            ListView listView = findViewById(R.id.ToiletListView);
                            ToiletAdapter adapter = new ToiletAdapter(this, toiletList);
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener((parent, view, position, id) -> {
                                Toilet selectedToilet = (Toilet) parent.getItemAtPosition(position);
                                Intent intent = new Intent(listActivity.this, detailActivity.class);
                                intent.putExtra("toilet", selectedToilet);
                                startActivity(intent);
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Handle error
                        }

                    } else {
                        Toast.makeText(listActivity.this, "fetch fail", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
