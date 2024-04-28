package com.example.sqlassignment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DeletedPasswordsAdapter extends RecyclerView.Adapter<DeletedPasswordsAdapter.ViewHolder> {

    private List<Password> deletedPasswords;

    public DeletedPasswordsAdapter(List<Password> deletedPasswords) {
        this.deletedPasswords = deletedPasswords;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deleted_password, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Password password = deletedPasswords.get(position);
        holder.appNameTextView.setText(password.getAppName());
        holder.passwordTextView.setText(password.getPassword());
        holder.urlTextView.setText(password.getUrl());
    }

    @Override
    public int getItemCount() {
        return deletedPasswords.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView appNameTextView;
        public TextView passwordTextView;
        public TextView urlTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            appNameTextView = itemView.findViewById(R.id.appNameTextView);
            passwordTextView = itemView.findViewById(R.id.passwordTextView);
            urlTextView = itemView.findViewById(R.id.urlTextView);
        }
    }
}