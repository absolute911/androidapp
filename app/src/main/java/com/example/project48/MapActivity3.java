package com.example.project48;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
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

import java.util.ArrayList;
import java.util.List;

public class MapActivity3 extends AppCompatActivity implements OnMapReadyCallback {

    private AppCompatRatingBar ratingTextView;
    private TextView locationTextView;
    private TextView distanceTextView;

    private Button cmtButton;

    private GoogleMap googleMap;

    private MapView mapView3;

    private LatLngBounds.Builder boundsBuilder;

    private List<Marker> markerList = new ArrayList<>();

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity3);

        mapView3 = findViewById(R.id.mapView3);
        mapView3.onCreate(savedInstanceState);
        mapView3.getMapAsync(this);

        cmtButton = findViewById(R.id.cmtButton);
        cmtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the map activity
                //Intent intent = new Intent(MapActivity3.this, MapActivity4.class);
                //startActivity(intent);
            }
        });

        ratingTextView = findViewById(R.id.ratingTextView);
        locationTextView = findViewById(R.id.locationTextView);
        distanceTextView = findViewById(R.id.distanceTextView);

        distanceTextView.setText("10 meters"); // Set the distance text

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView3.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView3.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView3.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView3.onLowMemory();
    }

    public void onMapReady(GoogleMap map) {
        googleMap = map;

        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                addLocationToMap(22.301115, 114.171868, "Mira Place");
                // Add other locations here

                // Zoom to fit all markers on the map
                zoomToMarkers();
            }
        });

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
