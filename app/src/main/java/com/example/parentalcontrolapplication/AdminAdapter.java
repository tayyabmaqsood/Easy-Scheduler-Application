package com.example.parentalcontrolapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.Query;

import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.ViewHolder> {
    private List<User> userList;
    private Context context;
    private adminSelectListner listner;

    public AdminAdapter(List<User> userList, Context context, adminSelectListner listner) {
        this.userList = userList;
        this.context = context;
        this.listner = listner;
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
        int index = position;
        User usr = userList.get(position);
        User user =userList.get(position);

        holder.textViewUserId.setText(usr.getId());
        holder.textViewUserName.setText(usr.getUsername());
        holder.textViewUserEmail.setText(usr.getEmail());
        holder.textViewUserType.setText(usr.getUserType());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.onItemClicked(userList.get(index));
            }
        });

        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
            //    listner.onItemClicked(userList.get(index));
                listner.onItemLongClicked(userList.get(index));
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewUserId;
        private TextView textViewUserName;
        private TextView textViewUserEmail;
        private TextView textViewUserType;
        private LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserId = (TextView)itemView.findViewById(R.id.showUserID);
            textViewUserName = (TextView)itemView.findViewById(R.id.showUsername);
            textViewUserEmail = (TextView)itemView.findViewById(R.id.showUserEmail);
            textViewUserType = (TextView)itemView.findViewById(R.id.showUserType);
            linearLayout = itemView.findViewById(R.id.adminRecyclerViewMainContainer);

        }
    }
}




















