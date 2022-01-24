package com.example.parentalcontrolapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.Query;

import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.ViewHolder> {
    private List<User> userList;
    private Context context;

    public AdminAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User usr = userList.get(position);
        holder.textViewUserId.setText(usr.getId());
        holder.textViewUserName.setText(usr.getUsername());
        holder.textViewUserEmail.setText(usr.getEmail());
        holder.textViewUserType.setText(usr.getUserType());

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewUserId;
        public TextView textViewUserName;
        public TextView textViewUserEmail;
        public TextView textViewUserType;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserId = (TextView)itemView.findViewById(R.id.showUserID);
            textViewUserName = (TextView)itemView.findViewById(R.id.showUsername);
            textViewUserEmail = (TextView)itemView.findViewById(R.id.showUserEmail);
            textViewUserType = (TextView)itemView.findViewById(R.id.showUserType);
        }
    }
}
