package com.example.project48;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class detailFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;

    private EditText searchEditText;

    private GoogleMap googleMap;

    private JSONObject nearestToilet;

    private LatLngBounds hongKongBounds;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        searchEditText = view.findViewById(R.id.searchEditText);
        mapView = view.findViewById(R.id.mapView);

        // 設置 Toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        // Set the toolbar as the activity's support action bar
        Activity activity = getActivity();
        if (activity instanceof AppCompatActivity) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
            appCompatActivity.setSupportActionBar(toolbar);
        }

        SupportMapFragment detailFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        if (detailFragment != null) {
            detailFragment.getMapAsync(this);
        }
        // Define the Hong Kong bounds
        hongKongBounds = new LatLngBounds(
                new LatLng(22.3608, 113.9154), // SW corner
                new LatLng(22.362745, 114.5025) // NE corner
        );
        return view;
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

    private void getDataFromIntent() {
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey("json")) {
            String json = arguments.getString("json");
            if (json != null) {
                try {
                    nearestToilet = new JSONObject(json);
                    // Process the JSON data here
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("DetailFragment", "No JSON data found in arguments");
            }
        } else {
            Log.e("DetailFragment", "No JSON data found in arguments");
        }
    }

    private void displayMarkersOnMap() {
        if (nearestToilet == null) {
            Toast.makeText(getActivity(), "No toilet data available", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject toiletObject = nearestToilet.getJSONObject("nearestToilet");
            String name = toiletObject.getString("name");
            String coordinates = toiletObject.getString("coordinates");
            double latitude = Double.parseDouble(coordinates.split(",")[0]);
            double longitude = Double.parseDouble(coordinates.split(",")[1]);
            LatLng latLng = new LatLng(latitude, longitude);

            double SampleLatitude = Double.parseDouble("22.317507333012692");
            double SampleLongitude = Double.parseDouble("114.1797521678627");
            LatLng sampleLatLng = new LatLng(SampleLatitude, SampleLongitude);



            googleMap.addMarker(new MarkerOptions().position(latLng).title(name));

            //sample marker
            googleMap.addMarker(new MarkerOptions().position(sampleLatLng).title("HKMU JC"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_loginsignup, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("MainActivity", "onOptionsItemSelected: item selected " + item.getItemId());
        switch (item.getItemId()) {
            case R.id.action_login_signup:
                showPopupMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // 實現顯示彈出菜單的邏輯
    private void showPopupMenu() {
        Log.d("MainActivity", "showPopupMenu: Showing popup menu");
        View menuView = getView().findViewById(R.id.action_login_signup);
        PopupMenu popup = new PopupMenu(requireContext(), menuView);
        popup.getMenuInflater().inflate(R.menu.login_navigation_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle menu item click here
                return false;
            }
        });
        popup.show();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        getDataFromIntent();
        displayMarkersOnMap(); // Add the marker to the map

        // Define the Hong Kong bounds
        final LatLngBounds hongKongBounds = new LatLngBounds(
                new LatLng(22.3608, 113.9154), // SW corner
                new LatLng(22.362745, 114.5025) // NE corner
        );

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
