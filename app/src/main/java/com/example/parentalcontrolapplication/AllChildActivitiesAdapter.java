package com.example.parentalcontrolapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AllChildActivitiesAdapter extends RecyclerView.Adapter<AllChildActivitiesAdapter.ViewHolder>{
    private Context context;
    private List<ChildActivities> activityList;
    private childActionSelectListner listner;

    public AllChildActivitiesAdapter( List<ChildActivities> activityList,Context context) {
        this.context = context;
        this.activityList = activityList;
    }

    public AllChildActivitiesAdapter(List<ChildActivities> activityList, Context context, childActionSelectListner listner) {
        this.activityList = activityList;
        this.context = context;
        this.listner = listner;
    }

    @NonNull
    @Override
    public AllChildActivitiesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.childactivitydetails_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AllChildActivitiesAdapter.ViewHolder holder, int position) {
        int ps = position;
        ChildActivities childActivity = activityList.get(position);
        holder.activityName.setText(childActivity.getChildName());
        holder.activityDateTime.setText(childActivity.getChildActivityName());
        holder.activityDescription.setText(childActivity.getActivityDescription());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.onItemClicked(activityList.get(ps));
            }
        });
    }


    @Override
    public int getItemCount() {
        return activityList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView activityName;
        private TextView activityDateTime;
        private TextView activityDescription;
        private CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            activityName = itemView.findViewById(R.id.personName);
            activityDateTime = itemView.findViewById(R.id.activityEmail);
            activityDescription = itemView.findViewById(R.id.activityDescription);
            cardView = itemView.findViewById(R.id.childactivitydetails);

        }
    }
}
