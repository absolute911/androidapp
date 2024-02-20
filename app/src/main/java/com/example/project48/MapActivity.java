package com.example.project48;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
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
    private JSONObject nearestToilet;


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

        //checkLocationService();

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
        String json = getIntent().getStringExtra("json");
        if (json != null) {
            try {
                nearestToilet = new JSONObject(json);
                // Process the JSON data here
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("NextActivity", "No JSON data found in intent");
        }
    }

    private void displayMarkersOnMap() {
        if (nearestToilet == null) {
            Toast.makeText(this, "No toilet data available", Toast.LENGTH_SHORT).show();
            return;
        }

            try {
                JSONObject toiletObject = nearestToilet.getJSONObject("nearestToilet");
                String name = toiletObject.getString("name");
                String coordinates = toiletObject.getString("coordinates");
                double latitude = Double.parseDouble(coordinates.split(",")[0]);
                double longitude = Double.parseDouble(coordinates.split(",")[1]);
                LatLng latLng = new LatLng(latitude, longitude);


                googleMap.addMarker(new MarkerOptions().position(latLng).title(name));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
            } catch (JSONException e) {
                e.printStackTrace();
            }
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

    public void onMapReady(GoogleMap map) {
        googleMap = map;
        getDataFromIntent();
        displayMarkersOnMap(); // Add the marker to the map

        // Define the Hong Kong bounds
        final LatLngBounds hongKongBounds = new LatLngBounds(
                new LatLng(22.3608, 113.9154), // SW corner
                new LatLng(22.362745, 114.5025) // NE corner
        );

        // Get the mapView reference
        final View mapView = findViewById(R.id.mapView);

        // Wait for the map to be laid out before setting the bounds
        mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi") // We check which build version we are using.
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                // Check if nearestToilet is available
                if (nearestToilet != null) {
                    try {
                        JSONObject toiletObject = nearestToilet.getJSONObject("nearestToilet");
                        String coordinates = toiletObject.getString("coordinates");
                        double latitude = Double.parseDouble(coordinates.split(",")[0]);
                        double longitude = Double.parseDouble(coordinates.split(",")[1]);
                        LatLng latLng = new LatLng(latitude, longitude);

                        // Create a LatLngBounds with nearestToilet and Hong Kong bounds
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        builder.include(latLng);
                        builder.include(hongKongBounds.southwest);
                        builder.include(hongKongBounds.northeast);
                        LatLngBounds combinedBounds = builder.build();

                        // Specify the map's size directly
                        int width = mapView.getWidth();
                        int height = mapView.getHeight();
                        int padding = (int) (width * 0.10); // 10% padding around the bounds

                        // Animate the camera to show the combined bounds
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(combinedBounds, width, height, padding));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Fallback if no marker data is available, with dynamic map size
                    int width = mapView.getWidth();
                    int height = mapView.getHeight();
                    int padding = (int) (width * 0.10); // 10% padding

                    // Move the camera to show the Hong Kong bounds
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(hongKongBounds, width, height, padding));
                }
            }
        });
    }


}