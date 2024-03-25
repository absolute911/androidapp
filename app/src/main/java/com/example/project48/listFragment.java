package com.example.project48;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import okhttp3.OkHttpClient;

import com.example.project48.detail.detailActivity;
import com.example.project48.misc.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Request;
import okhttp3.Response;

public class listFragment extends Fragment {

    private String latitude ="22.317507333012692";
    private String longitude = "114.1797521678627";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        double lat = Double.parseDouble(latitude);
        double lon = Double.parseDouble(longitude);

        // Fetch the details as soon as the page loads
        getDetailList(lat, lon);

        return view;
    }

    private void getDetailList(double latitude, double longitude) {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            URL URL = new URL();

            String url = URL.getURL() + "items/list?latitude=" + latitude + "&longitude=" + longitude;

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                final String responseBody = response.body() != null ? response.body().string() : null;
                requireActivity().runOnUiThread(() -> {
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
                            ListView listView = requireView().findViewById(R.id.ToiletListView);
                            ToiletAdapter adapter = new ToiletAdapter((listActivity) requireContext(), toiletList);
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener((parent, view, position, id) -> {
                                Toilet selectedToilet = (Toilet) parent.getItemAtPosition(position);
                                Intent intent = new Intent(requireContext(), detailActivity.class);
                                intent.putExtra("toilet", selectedToilet);
                                startActivity(intent);
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Handle error
                        }
                    } else {
                        Toast.makeText(requireContext(), "fetch fail", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
