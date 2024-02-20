package com.example.project48.Forum;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.project48.R;

import java.util.ArrayList;


public class ThreadAdapter extends ArrayAdapter<ForumThread> {
    public ThreadAdapter(Activity context, ArrayList<ForumThread> threads) {
        super(context, 0, threads);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ForumThread thread = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.threat_item, parent, false);
        }
        // Lookup view for data population
        TextView tvTitle = convertView.findViewById(R.id.textViewTitle);
        TextView tvContent = convertView.findViewById(R.id.textViewContent);
        // Populate the data into the template view using the data object
        tvTitle.setText(thread.getTitle());
        tvContent.setText(thread.getContent());
        // Return the completed view to render on screen
        return convertView;
    }


}
