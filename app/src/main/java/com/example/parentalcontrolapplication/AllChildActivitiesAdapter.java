package com.example.parentalcontrolapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AllChildActivitiesAdapter extends RecyclerView.Adapter<AllChildActivitiesAdapter.ViewHolder>{
    private Context context;
    private List<ChildActivities> activityList;
    private AllChildActivitiesInterface listner;

    public AllChildActivitiesAdapter( List<ChildActivities> activityList,Context context) {
        this.context = context;
        this.activityList = activityList;
    }

    public AllChildActivitiesAdapter(List<ChildActivities> activityList, Context context, AllChildActivitiesInterface listner) {
        this.activityList = activityList;
        this.context = context;
        this.listner = listner;
    }

    @NonNull
    @Override
    public AllChildActivitiesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.showactivity_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AllChildActivitiesAdapter.ViewHolder holder, int position) {
        int ps = position;
        ChildActivities childActivities = activityList.get(position);
        holder.textChildName.setText(childActivities.getChildName());
        holder.textActivityName.setText(childActivities.getChildActivityName());
        holder.textActivityDescription.setText(childActivities.getActivityDescription());
        holder.getTextActivityTime.setText(childActivities.getActivityTime());
        holder.getTextActivityDate.setText(childActivities.getActivityDate());
//        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//
//                listner.onItemLongClicked(activityList.get(ps));
//                return false;
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return activityList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        private  TextView textChildName;
        private TextView textActivityName;
        private TextView textActivityDescription;
        private TextView getTextActivityTime;
        private TextView getTextActivityDate;
        private LinearLayout linearLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textChildName = itemView.findViewById(R.id.showChildName);
            textActivityName  = (TextView) itemView.findViewById(R.id.showActivityName);
            textActivityDescription = itemView.findViewById(R.id.showChildActivityDescription);
            getTextActivityTime = itemView.findViewById(R.id.showChildActivityTime);
            getTextActivityDate = itemView.findViewById(R.id.showChildActivityDate);
            linearLayout = itemView.findViewById(R.id.childShowAllRecyclerViewLinearlayout);

        }
    }
}
