package com.example.parentalcontrolapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChlidActionsAdapter extends RecyclerView.Adapter<ChlidActionsAdapter.ViewHolder> {
    private List<ChildActivities> childActivitiesList;
    private Context context;
    private childActionSelectListner listner;

    public ChlidActionsAdapter(List<ChildActivities> childActivitiesList, Context context, childActionSelectListner listner) {
        this.childActivitiesList = childActivitiesList;
        this.context = context;
        this.listner = listner;
    }




    public ChlidActionsAdapter(List<ChildActivities> childActivitiesList, Context context) {
        this.childActivitiesList = childActivitiesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chlidaction_custom_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int ps = position;
        ChildActivities childActivity = childActivitiesList.get(position);
        holder.activityName.setText(childActivity.getChildActivityName());
        holder.activityDateTime.setText(childActivity.getActivityDate() + ',' + childActivity.getActivityTime());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.onItemClicked(childActivitiesList.get(ps));
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listner.onItemLongClicked(childActivitiesList.get(ps));
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return childActivitiesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView activityName;
        private TextView activityDateTime;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            activityName = itemView.findViewById(R.id.activityName);
            activityDateTime = itemView.findViewById(R.id.activityDateTime);
            cardView = itemView.findViewById(R.id.cardViewContainer);
        }

    }
}
