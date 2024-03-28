package com.example.project48;

import android.app.Activity;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;

import com.example.project48.Forum.AddPostActivity;
import com.example.project48.Forum.ForumActivity;
import com.example.project48.Login.LoginActivity;
import com.example.project48.Login.SignupActivity;
import com.example.project48.misc.SessionManager;
import com.example.project48.misc.URL;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private GoogleMap googleMap;
    private Toilet nearestToilet;
    private LatLngBounds hongKongBounds;
    private Geocoder geocoder;
    private Marker searchMarker;
    double SampleLatitude = Double.parseDouble("22.317507333012692");
    double SampleLongitude = Double.parseDouble("114.1797521678627");
    private String initialCoordinates;
    private String initialName;

    private Button listButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mapView = findViewById(R.id.mapView);
        listButton = findViewById(R.id.listButton);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        mapView = findViewById(R.id.mapView);
        geocoder = new Geocoder(getApplicationContext());
        getDataFromBundle();

        SupportMapFragment detailFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        if (detailFragment != null) {
            detailFragment.getMapAsync(this);
        }

        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, listActivity.class);
                startActivity(intent);
            }
        });

        hongKongBounds = new LatLngBounds(
                new LatLng(22.3608, 113.9154),
                new LatLng(22.362745, 114.5025));
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void getDataFromBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            nearestToilet = bundle.getParcelable("selectedToilet");
        }
    }

    private void displayMarkersOnMap() {
        if (nearestToilet == null) {
            getDetail(SampleLatitude, SampleLongitude);
            return;
        }

        try {
            String name = nearestToilet.getName();
            String coordinates = nearestToilet.getCoordinates();
            double latitude = Double.parseDouble(coordinates.split(",")[0]);
            double longitude = Double.parseDouble(coordinates.split(",")[1]);
            LatLng latLng = new LatLng(latitude, longitude);
            LatLng sampleLatLng = new LatLng(SampleLatitude, SampleLongitude);
            googleMap.addMarker(new MarkerOptions().position(latLng).title(name));
            googleMap.addMarker(new MarkerOptions().position(sampleLatLng).title("HKMU JC"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDetail(double latitude, double longitude) {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            URL URL = new URL();
            String url = URL.getURL() + "items/nearest?latitude=" + latitude + "&longitude=" + longitude;
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
                                initialName = nearestToiletObject.getString("name");
                                initialCoordinates = nearestToiletObject.getString("coordinates");
                                initialMarkersOnMap(initialCoordinates, initialName);
                            } else {
                                Toast.makeText(DetailActivity.this, "Nearest toilet data not found.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(DetailActivity.this, "Fetch failed", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(DetailActivity.this, "Error fetching details.", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void initialMarkersOnMap(String coordinates, String name) {
        Log.d("initialMarkersOnMap", "initialMarkersOnMap: ");
        try {
            double latitude = Double.parseDouble(coordinates.split(",")[0]);
            double longitude = Double.parseDouble(coordinates.split(",")[1]);
            LatLng latLng = new LatLng(latitude, longitude);
            LatLng sampleLatLng = new LatLng(SampleLatitude, SampleLongitude);
            googleMap.addMarker(new MarkerOptions().position(latLng).title(name));
            googleMap.addMarker(new MarkerOptions().position(sampleLatLng).title("HKMU JC"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        displayMarkersOnMap();
        final LatLngBounds hongKongBounds = new LatLngBounds(
                new LatLng(22.3608, 113.9154), new LatLng(22.362745, 114.5025));
        mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                googleMap.setLatLngBoundsForCameraTarget(hongKongBounds);
                googleMap.setMinZoomPreference(10);
                googleMap.setMaxZoomPreference(20);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_overflow:
                View menuView = findViewById(R.id.action_overflow); // Replace with the correct view ID
                SessionManager sessionManager = new SessionManager(this);
                showPopupMenu(menuView, sessionManager);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void showPopupMenu(View menuView, SessionManager sessionManager) {
        PopupMenu popup = new PopupMenu(this, menuView);

        boolean isLoggedIn = sessionManager.isLoggedIn();

        if (isLoggedIn) {
            popup.getMenuInflater().inflate(R.menu.after_login_navigation_menu, popup.getMenu());
        } else {
            popup.getMenuInflater().inflate(R.menu.login_navigation_menu, popup.getMenu());
        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (isLoggedIn) {
                    if (itemId == R.id.menu_add_point_toilet_add) {
                        Intent intent = new Intent(DetailActivity.this, AddToiletActivity.class);
                        startActivity(intent);
                        return true;
                    } else if (itemId == R.id.menu_point_detail_report) {
                        sessionManager.clearLoginSession();
                        Intent intent = new Intent(DetailActivity.this, DetailActivity.class);
                        startActivity(intent);
                        return true;
                    }
                } else {
                    if (itemId == R.id.menu_header_login) {
                        Intent intent = new Intent(DetailActivity.this, LoginActivity.class);
                        startActivity(intent);
                        return true;
                    } else if (itemId == R.id.menu_signup_login) {
                        Intent intent = new Intent(DetailActivity.this, SignupActivity.class);
                        startActivity(intent);
                        return true;
                    }
                }
                return false;
            }
        });
        popup.show();
    }
}
