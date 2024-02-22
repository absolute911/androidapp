package com.example.project48;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserCommentAdapter extends RecyclerView.Adapter<UserCommentAdapter.CommentViewHolder> {

    private ArrayList<userComments> userCommentList;

    public UserCommentAdapter(UserCommentActivity userCommentActivity, ArrayList<userComments> userCommentList) {
        this.userCommentList = userCommentList;
    }


    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentTextView;
        TextView userNameTextView;

        public CommentViewHolder(View itemView) {
            super(itemView);
            commentTextView = itemView.findViewById(R.id.comment_text_view);
            userNameTextView = itemView.findViewById(R.id.userName_text_view);
        }
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.comment_item_layout, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        userComments comment = userCommentList.get(position);
        holder.commentTextView.setText(comment.getComment());
        holder.userNameTextView.setText(comment.getUserName());
    }

    @Override
    public int getItemCount() {
        return userCommentList.size();
    }
}