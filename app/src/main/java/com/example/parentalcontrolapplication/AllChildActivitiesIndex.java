package com.example.parentalcontrolapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class AllChildActivitiesIndex extends AppCompatActivity implements childActionSelectListner{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ChildActivities> childActionActivities;

    private  List<ChildActivities> pendingList;
    private  List<ChildActivities> completeList;
    private RecyclerView.Adapter pendingAdapter;
    private RecyclerView.Adapter completeAdapter;
    Boolean isAlreadyShow = false;
    String selectedTab = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_child_activities_index);


        recyclerView = (RecyclerView) findViewById(R.id.showAllChildActivitiesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        childActionActivities = new ArrayList<>();
        adapter = new AllChildActivitiesAdapter(childActionActivities, this, this);
        recyclerView.setAdapter(adapter);


        pendingList = new ArrayList<>();
        pendingAdapter = new AllChildActivitiesAdapter(pendingList, this,this);
        completeList = new ArrayList<>();
        completeAdapter = new AllChildActivitiesAdapter(completeList, this, this);

        //final boolean[] isAlreadyShow = {false};
        DatabaseReference reference = FirebaseDatabase.getInstance("https://parental-control-applica-de957-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("children");
        reference.orderByChild("parentId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        childActionActivities.clear();
                        Iterator shots = snapshot.getChildren().iterator();
                        while (shots.hasNext()) {

                            DataSnapshot singleShot = (DataSnapshot) shots.next();
                            String email = singleShot.child("childEmail").getValue().toString();
                            FirebaseDatabase.getInstance("https://parental-control-applica-de957-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                    .getReference("Activities").orderByChild("childEmail").equalTo(email)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot Ssnapshot) {
                                            Iterator Sshots = Ssnapshot.getChildren().iterator();
                                            while (Sshots.hasNext()) {
                                                DataSnapshot SsingleShot = (DataSnapshot) Sshots.next();
                                                ChildActivities childActivity = new ChildActivities();
                                                try {
                                                    childActivity.setChildActivityName(SsingleShot.child("childActivityName").getValue().toString());
                                                    childActivity.setActivityDescription(SsingleShot.child("activityDescription").getValue().toString());
                                                    childActivity.setActivityTime(SsingleShot.child("activityTime").getValue().toString());
                                                    childActivity.setActivityDate(SsingleShot.child("activityDate").getValue().toString());
                                                    childActivity.setActivityStatus(SsingleShot.child("activityStatus").getValue().toString());
                                                    childActivity.setChildEmail(SsingleShot.child("childEmail").getValue().toString());
                                                    childActivity.setActivityId(SsingleShot.getKey());
                                                    childActivity.setCompletedDate(SsingleShot.child("completedDate").getValue().toString());
                                                    childActivity.setChildName(singleShot.child("childName").getValue().toString());
                                                    childActionActivities.add(childActivity);

//                                                    adapter = new AllChildActivitiesAdapter(childActionActivities, AllChildActivitiesIndex.this, AllChildActivitiesIndex.this);
//                                                    recyclerView.setAdapter(adapter);
                                                    if (!isAlreadyShow) {
                                                        recyclerView.setAdapter(adapter);
                                                        isAlreadyShow = true;
                                                    }
                                                }catch (Exception e){
                                                    Toast.makeText(AllChildActivitiesIndex.this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                                                }

                                                }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(AllChildActivitiesIndex.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void pendingActivities(View view) {
        try {
            selectedTab = "pending";
            RelativeLayout todoActivity = findViewById(R.id.todaysAcativityCardView);
            todoActivity.setBackgroundColor(getResources().getColor(R.color.whiteshade));
            RelativeLayout restcard = findViewById(R.id.completedActivitiesCardView);
            restcard.setBackgroundColor(Color.TRANSPARENT);
            updatePendingActivitiesList();
        }catch (Exception e){
            Toast.makeText(AllChildActivitiesIndex.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void completedActivities(View view) {
        RelativeLayout carcard = findViewById(R.id.todaysAcativityCardView);
        carcard.setBackgroundColor(Color.TRANSPARENT);
        RelativeLayout restcard = findViewById(R.id.completedActivitiesCardView);
        restcard.setBackgroundColor(getResources().getColor(R.color.whiteshade));
        updateCompleteList();
    }


    private void updatePendingActivitiesList() {
        pendingList.clear();
        pendingList = getActivitiesToDo();
        pendingAdapter = new AllChildActivitiesAdapter(pendingList,this,this);
        recyclerView.setAdapter(pendingAdapter);
    }

    private List<ChildActivities> getActivitiesToDo() {
        List<ChildActivities>list = new ArrayList<ChildActivities>();
        for(int i = 0;i<childActionActivities.size();i++) {
            if (childActionActivities.get(i).getActivityStatus().equals("pending"))
                list.add(childActionActivities.get(i));
        }
        return list;
    }

    private void updateCompleteList() {
        completeList.clear();
        completeList = getCompletedActivities();
        completeAdapter = new AllChildActivitiesAdapter(completeList,AllChildActivitiesIndex.this,AllChildActivitiesIndex.this);
        recyclerView.setAdapter(completeAdapter);
    }

    private List<ChildActivities> getCompletedActivities() {
        List<ChildActivities>list = new ArrayList<ChildActivities>();
        for(int i = 0;i<childActionActivities.size();i++) {
            if (childActionActivities.get(i).getActivityStatus().equals("completed"))
                list.add(childActionActivities.get(i));
        }
        return list;
    }

    private String clickedEmail = "";
    private int itemClicked = 0;

    @Override
    public void onItemClicked(ChildActivities activity) {
        if (clickedEmail.equals(activity.getChildEmail())) {
            itemClicked++;
            if(itemClicked == 2) {
                //Do thing on double click
                Dialog dialog = new Dialog(AllChildActivitiesIndex.this,R.style.Base_Theme_AppCompat_DialogWhenLarge);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.showactivity_layout);

                TextView name =  dialog.findViewById(R.id.showActivityName);
                TextView description =  dialog.findViewById(R.id.showChildActivityDescription);
                TextView date =  dialog.findViewById(R.id.showChildActivityDate);
                TextView status =  dialog.findViewById(R.id.showChildActivityStatus);
                TextView completeDate =  dialog.findViewById(R.id.showChildActivityCompletedDate);
                Button closeButton = dialog.findViewById(R.id.button_close);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                Log.d("Completed", activity.getCompletedDate());
                if(activity.getCompletedDate().equals(null) || activity.getCompletedDate().equals("None"))
                    completeDate.setText("None");
                else
                    completeDate.setText(activity.getCompletedDate());
                name.setText(activity.getChildActivityName());
                description.setText(activity.getActivityDescription());
                date.setText(activity.getActivityDate() + " , "+activity.getActivityTime());
                status.setText(activity.getActivityStatus());

                dialog.show();

                itemClicked = 0;
                clickedEmail = "";
            }
        }
        else {
            clickedEmail = activity.getChildEmail();
            itemClicked = 1;
            Toast.makeText(this, "Press one more time to see full activity", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemLongClicked(ChildActivities activity) {

    }

    public void destroyActivity(View view) {
        finish();
    }
}

