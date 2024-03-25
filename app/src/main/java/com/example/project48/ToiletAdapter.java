package com.example.project48;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class ToiletAdapter extends ArrayAdapter<Toilet> {
    public ToiletAdapter(MainActivity context, ArrayList<Toilet> toilets) {
        super(context, 0, toilets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Toilet toilet = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_toilet, parent, false);
        }
        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvDistance = convertView.findViewById(R.id.tvDistance);
        TextView tvRating = convertView.findViewById(R.id.tvRating);

        tvName.setText(toilet.getName());
        tvDistance.setText(String.format(Locale.getDefault(), "%.2f meters", toilet.getDistance()));
        tvRating.setText(String.format(Locale.getDefault(), "Rating: %d", toilet.getRating()));

        return convertView;
    }
}
