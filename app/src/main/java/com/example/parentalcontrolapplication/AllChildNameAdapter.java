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

public class AllChildNameAdapter extends RecyclerView.Adapter<AllChildNameAdapter.ViewHolder>{
    private Context context;
    private List<Child> childList;

    public AllChildNameAdapter(List<Child> childList,Context context) {
        this.context = context;
        this.childList = childList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.showpersoninfo_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int ps = position;
        Child child = childList.get(position);
        holder.textCName.setText(child.getChildName());
        holder.textAge.setText(child.getChildAge());
        holder.textDOB.setText(child.getChildDOB());
        holder.gender.setText(child.getChildGender());

    }

    @Override
    public int getItemCount() {
         return childList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textCName;
        private TextView textAge;
        private TextView textDOB;
        private TextView gender;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textCName = itemView.findViewById(R.id.showChildName);
            textAge  = (TextView) itemView.findViewById(R.id.showChildAge);
            textDOB = itemView.findViewById(R.id.showChildDOB);
            gender = itemView.findViewById(R.id.showChildGender);

        }
    }

}
