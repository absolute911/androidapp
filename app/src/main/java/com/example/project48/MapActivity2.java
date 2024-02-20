package com.example.project48;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// Testing
public class MapActivity2 extends AppCompatActivity implements OnMapReadyCallback {

    private TextView locationTextView;
    private TextView distanceTextView;
    private GoogleMap googleMap;

    private LatLngBounds.Builder boundsBuilder;

    private List<Marker> markerList = new ArrayList<>();

    private Button mapButton2;

    private Button button100m;

    private Button button500m;

    private Button button1km;

    private Button button2km;

    private MapView mapView2;
    
    private float[] distanceLevels = {100f, 500f, 1000f, 2000f}; // Distance levels in meters
    private int selectedDistanceIndex = 0; // Initially select the first distance level (100m)

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity2);

        button100m = findViewById(R.id.button100m);
        button500m = findViewById(R.id.button500m);
        button1km = findViewById(R.id.button1km);
        button2km = findViewById(R.id.button2km);

        mapView2 = findViewById(R.id.mapView2);
        mapView2.onCreate(savedInstanceState);
        mapView2.getMapAsync(this);

        // Set the initial background colors
        updateDistanceButtonColors();

        // Find the mapButton2 and set its click listener
        mapButton2 = findViewById(R.id.mapButton2);
        mapButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the map activity
                Intent intent = new Intent(MapActivity2.this, MapActivity3.class);
                startActivity(intent);
            }
        });

        locationTextView = findViewById(R.id.locationTextView);
        distanceTextView = findViewById(R.id.distanceTextView);

        distanceTextView.setText("10 meters"); // Set the distance text

    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView2.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView2.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView2.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView2.onLowMemory();
    }

    private void getDetail(double latitude, double longitude) {
        OkHttpClient client = new OkHttpClient();
        String url = "http://192.168.50.143:3000/items/nearest?latitude=" + latitude + "&longitude=" + longitude;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MapActivity2.this, "Fetch failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseBody = response.body() != null ? response.body().string() : null;
                runOnUiThread(new Runnable() {
                    private BigInteger markers;

                    @Override
                    public void run() {
                        if (response.isSuccessful() && responseBody != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(responseBody);
                                if (jsonObject.has("nearestToilet")) {
                                    JSONArray nearestToiletArray = jsonObject.getJSONArray("nearestToilet");

                                    for (int i = 0; i < nearestToiletArray.length(); i++) {
                                        JSONObject nearestToiletObject = nearestToiletArray.getJSONObject(i);
                                        String name = nearestToiletObject.getString("name");
                                        String openHours = nearestToiletObject.getString("open_hours");
                                        String address = nearestToiletObject.getString("address");
                                        String coordinates = nearestToiletObject.getString("coordinates");

                                        // Extract latitude and longitude from coordinates string
                                        String[] latLng = coordinates.split(",");
                                        double markerLat = Double.parseDouble(latLng[0]);
                                        double markerLng = Double.parseDouble(latLng[1]);

                                        // Add marker to the map
                                        Marker marker = googleMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(markerLat, markerLng))
                                                .title(name)
                                                .snippet("Open Hours: " + openHours + "\nAddress: " + address));
                                        assert marker != null;
//                                        markers.add(marker);
                                    }
                                } else {
                                    // Handle the case where the "nearestToilet" key is missing in the JSON response
                                    // You can show an error message or handle it as needed
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                // Handle JSON parsing errors
                            }
                        } else {
                            Toast.makeText(MapActivity2.this, "Fetch failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


    public void onDistanceButtonClick(View view) {
        int clickedButtonId = view.getId();

        // Determine the selected distance level based on the clicked button
        if (clickedButtonId == R.id.button100m) {
            selectedDistanceIndex = 0;
        } else if (clickedButtonId == R.id.button500m) {
            selectedDistanceIndex = 1;
        } else if (clickedButtonId == R.id.button1km) {
            selectedDistanceIndex = 2;
        } else if (clickedButtonId == R.id.button2km) {
            selectedDistanceIndex = 3;
        }

        // Update the map's zoom level based on the selected distance level
        if (googleMap != null) {
            float selectedDistance = distanceLevels[selectedDistanceIndex];
            LatLngBounds bounds = computeBounds(selectedDistance);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100);
            googleMap.animateCamera(cameraUpdate);
        }

        // Update the background colors of the distance buttons
        updateDistanceButtonColors();
    }

    private LatLngBounds computeBounds(float distance) {
        LatLng currentLocation = googleMap.getCameraPosition().target;
        double latitude = currentLocation.latitude;
        double longitude = currentLocation.longitude;
        double latOffset= distance / 111000f;
        double lonOffset = distance / (111000f * Math.cos(Math.toRadians(latitude)));
        LatLng northeast = new LatLng(latitude + latOffset, longitude + lonOffset);
        LatLng southwest = new LatLng(latitude - latOffset, longitude - lonOffset);
        return new LatLngBounds(southwest, northeast);
    }

    private void updateDistanceButtonColors() {
        button100m.setBackgroundColor(
                getResources().getColor(selectedDistanceIndex == 0 ? R.color.colorAccent : R.color.colorPrimary));
        button500m.setBackgroundColor(
                getResources().getColor(selectedDistanceIndex == 1 ? R.color.colorAccent : R.color.colorPrimary));
        button1km.setBackgroundColor(
                getResources().getColor(selectedDistanceIndex == 2 ? R.color.colorAccent : R.color.colorPrimary));
        button2km.setBackgroundColor(
                getResources().getColor(selectedDistanceIndex == 3 ? R.color.colorAccent : R.color.colorPrimary));
    }

    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String locationName = (String) marker.getTag();
                locationTextView.setText(locationName);
                return false;
            }
        });

        // Disable the default zoom controls
        googleMap.getUiSettings().setZoomControlsEnabled(false);

        // Add multiple locations using LatLng (latitude, longitude)
        addLocationToMap(22.301115, 114.171868, "Mira Place");
        addLocationToMap(22.303536, 114.171094, "The One");
        addLocationToMap(22.304386, 114.172137, "Prudential Centre");

        // Zoom to fit all markers on the map
        zoomToMarkers();
    }

    private void addLocationToMap(double latitude, double longitude, String locationName) {
        LatLng latLng = new LatLng(latitude, longitude);
        BitmapDescriptor customMarkerIcon = getBitmapFromVectorDrawable(R.drawable.ic_restroom);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(locationName)
                .icon(customMarkerIcon);

        Marker marker = googleMap.addMarker(markerOptions);
        marker.setTag(locationName);

        // Extend the bounds to include the marker's position
        if (boundsBuilder == null) {
            boundsBuilder = new LatLngBounds.Builder().include(latLng);
        } else {
            boundsBuilder.include(latLng);
        }

        markerList.add(marker); // Add the marker to the list

    }

    private BitmapDescriptor getBitmapFromVectorDrawable(int vectorDrawableResourceId) {
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), vectorDrawableResourceId);
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888
        );
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void zoomToMarkers() {
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

        for (Marker marker : markerList) {
            boundsBuilder.include(marker.getPosition());
        }

        LatLngBounds bounds = boundsBuilder.build();
        int padding = 100; // Adjust the padding as per your needs
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.moveCamera(cameraUpdate);
    }

}