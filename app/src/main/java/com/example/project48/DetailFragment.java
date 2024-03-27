package com.example.project48;

import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Geocoder;
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
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.project48.Login.LoginActivity;
import com.example.project48.Login.SignupActivity;
import com.example.project48.detail.detailActivity;
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

public class DetailFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;

    private SearchView searchView;

    private GoogleMap googleMap;

    private Toilet nearestToilet;

    private LatLngBounds hongKongBounds;

    private Geocoder geocoder;
    private Marker searchMarker;

    double SampleLatitude = Double.parseDouble("22.317507333012692");
    double SampleLongitude = Double.parseDouble("114.1797521678627");

    private String initialCoordinates;
    private String initialName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        mapView = view.findViewById(R.id.mapView);
        geocoder = new Geocoder(requireContext());

        getDataFromBundle();
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

    private void getDataFromBundle() {
        Bundle bundle = getArguments();
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

            //sample marker
            googleMap.addMarker(new MarkerOptions().position(sampleLatLng).title("HKMU JC"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("detailFragment", "onOptionsItemSelected: item selected " + item.getItemId());
        switch (item.getItemId()) {
            case R.id.action_overflow:
                View menuView = getActivity().findViewById(R.id.action_overflow); // Replace with the correct view ID
                SessionManager sessionManager = new SessionManager(getActivity());
                showPopupMenu(menuView, sessionManager, getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void showPopupMenu(View menuView, SessionManager sessionManager, Activity activity) {
        PopupMenu popup = new PopupMenu(getContext(), menuView);

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
                        Intent intent = new Intent(getContext(), AddToiletActivity.class);
                        startActivity(intent);
                        return true;
                    } else if (itemId == R.id.menu_point_detail_report) {
                        sessionManager.clearLoginSession();
                        Intent intent = new Intent(getContext(), detailActivity.class);
                        startActivity(intent);
                        return true;
                    }
                } else {
                    if (itemId == R.id.menu_header_login) {
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                        return true;
                    } else if (itemId == R.id.menu_signup_login) {
                        Intent intent = new Intent(getContext(), SignupActivity.class);
                        startActivity(intent);
                        return true;
                    }
                }
                return false;
            }
        });
        popup.show();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
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
                        String coordinates = nearestToilet.getCoordinates();
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

                    } catch (Exception e) {
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
                // Make sure you are calling runOnUiThread() on an Activity's context.
                if(getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    if (response.isSuccessful() && responseBody != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(responseBody);
                            if (jsonObject.has("nearestToilet")) {
                                JSONObject nearestToiletObject = jsonObject.getJSONObject("nearestToilet");
                                initialName = nearestToiletObject.getString("name");
                                initialCoordinates = nearestToiletObject.getString("coordinates");
                                initialMarkersOnMap(initialCoordinates, initialName);
                            } else {
                                Toast.makeText(getActivity(), "Nearest toilet data not found.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Fetch failed", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                // It's good practice to handle errors, maybe update the UI to show an error message
                if(getActivity() == null) return;
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Error fetching details.", Toast.LENGTH_SHORT).show());
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

            //sample marker
            googleMap.addMarker(new MarkerOptions().position(sampleLatLng).title("HKMU JC"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
