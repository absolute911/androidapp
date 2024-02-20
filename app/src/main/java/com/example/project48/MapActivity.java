package com.example.project48;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String latitude ="123.123";
    private String longitude = "321.321";
    private TextView locationTextView;
    private TextView distanceTextView;
    private Button mapButton;

    private MapView mapView;

    private GoogleMap googleMap;

    private JSONArray nearestToiletArray;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        locationTextView = findViewById(R.id.locationTextView);
        distanceTextView = findViewById(R.id.distanceTextView);
        mapButton = findViewById(R.id.mapButton);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

//        // Convert latitude and longitude from String to double
//        double lat = Double.parseDouble(latitude);
//        double lon = Double.parseDouble(longitude);

        // Fetch the details as soon as the page loads
//        getDataFromIntent(lat, lon);

        checkLocationService();

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the map activity
                Intent intent = new Intent(MapActivity.this, MapActivity2.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void getDataFromIntent() {
        try {
            String json = getIntent().getStringExtra("json");
            JSONObject jsonObject = new JSONObject(json);
            JSONArray nearestToiletArray = jsonObject.getJSONArray("nearestToilet");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayMarkersOnMap() {
        if (nearestToiletArray == null) {
            Toast.makeText(this, "No toilet data available", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < nearestToiletArray.length(); i++) {
            try {
                JSONObject toiletObject = nearestToiletArray.getJSONObject(i);
                String name = toiletObject.getString("name");
                String coordinates = toiletObject.getString("coordinates");
                double latitude = Double.parseDouble(coordinates.split(",")[0]);
                double longitude = Double.parseDouble(coordinates.split(",")[1]);
                LatLng latLng = new LatLng(latitude, longitude);

                Marker marker = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(name));

                // Fetch the distance for the marker's coordinates
                fetchDistance(marker, latitude, longitude);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (nearestToiletArray == null || nearestToiletArray.length() == 0) {
            Toast.makeText(this, "No toilet data available", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void fetchDistance(Marker marker, double latitude, double longitude) {
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
                            double distance = jsonObject.optDouble("distance");

                            // Update the marker's snippet with the distance
                            marker.setSnippet("Distance: " + distance + " meters");
                            marker.showInfoWindow();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(this, "Failed to fetch distance", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    private void checkLocationService() {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gpsEnabled) {
            // Show the dialog asking to turn on device location
            showDialog();
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
        builder.setTitle("Uses Google's Location Service")
                .setMessage("Turn on device location")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Proceed to location settings
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        getDataFromIntent();
        displayMarkersOnMap();
        // Zoom to show the whole of Hong Kong
        LatLngBounds hongKongBounds = new LatLngBounds(new LatLng(22.3608, 113.9154),
                new LatLng(22.362745, 114.5025));

        if (mapView.getViewTreeObserver().isAlive()) {
            mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(hongKongBounds, 0));
                }
            });
        }
    }
}