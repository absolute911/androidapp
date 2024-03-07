package com.example.project48.Forum;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project48.R;

import java.util.ArrayList;

public class ThreadRecyclerViewAdapter extends RecyclerView.Adapter<ThreadRecyclerViewAdapter.ViewHolder> {

    private static ArrayList<ForumThread> threads;



    // Constructor
    public ThreadRecyclerViewAdapter(ForumActivity context, ArrayList<ForumThread> threads) {
        this.threads = threads;
    }

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public TextView tvContent;
        public TextView tvID;

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.textViewTitle);
            tvContent = itemView.findViewById(R.id.textViewContent);
            tvID = itemView.findViewById(R.id.textViewID);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.onItemClick(threads.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

    private OnItemClickListener listener;

    // Modify the constructor to include the listener parameter
    public ThreadRecyclerViewAdapter(ForumActivity context, ArrayList<ForumThread> threads, OnItemClickListener listener) {
        this.threads = threads;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ForumThread thread);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ThreadRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.threat_item, parent, false);
        return new ViewHolder(view, listener); // Pass the listener here
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ForumThread thread = threads.get(position);
        holder.tvTitle.setText(thread.getTitle());
        //holder.tvContent.setText(thread.getContent());
        holder.tvID.setText(thread.getId());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return threads.size();
    }

    public void updateThreads(ArrayList<ForumThread> newThreads) {
        threads.clear();
        threads.addAll(newThreads);
        notifyDataSetChanged();
    }
}
