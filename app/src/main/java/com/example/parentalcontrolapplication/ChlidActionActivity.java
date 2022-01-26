package com.example.parentalcontrolapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ChlidActionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ChildActivities> childActionActivities;

    private  List<ChildActivities> randomList;
    private RecyclerView recyclerViewRandom;
    private RecyclerView.Adapter adapterRandom;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chlid_action);
        recyclerView = (RecyclerView) findViewById(R.id.childActivitiesIndexRecyclerView);
        // means every item of recyclerview has fixed size
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        childActionActivities = new ArrayList<>();
        adapter = new ChlidActionsAdapter(childActionActivities,this);
        recyclerView.setAdapter(adapter);



        randomList = new ArrayList<>();
        adapterRandom =  new ChlidActionsAdapter(randomList,this);



        String childId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance("https://parental-control-applica-de957-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Activities");

        Query query = reference.orderByChild("childEmail").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                childActionActivities.clear();


                Iterator shots = snapshot.getChildren().iterator();

                while (shots.hasNext()) {


                    DataSnapshot singleShot = (DataSnapshot) shots.next();
                    ChildActivities childActivity = new ChildActivities();

                    try {
                        Log.d("string", "12346546546123");
                        childActivity.setChildActivityName(singleShot.child("childActivityName").getValue().toString());

                        childActivity.setActivityDescription(singleShot.child("activityDescription").getValue().toString());
                        childActivity.setActivityTime(singleShot.child("activityTime").getValue().toString());
                        childActivity.setActivityDate(singleShot.child("activityDate").getValue().toString());
                        childActivity.setActivityStatus(singleShot.child("activityStatus").getValue().toString());
                        childActivity.setChildEmail(singleShot.child("childEmail").getValue().toString());
                        Log.d("str",childActivity.toString());
                        childActionActivities.add(childActivity);
                        adapter.notifyDataSetChanged();

                    }catch (Exception e){ }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public void showAllActivities(View view){

        randomList.clear();
        randomList = getActivitiesToDo();
        adapterRandom = new ChlidActionsAdapter(randomList,this);
        recyclerView.setAdapter(adapterRandom);

    }

    public void completedActivities(View view) {

        randomList.clear();
        randomList = getCompletedActivities();
        adapterRandom = new ChlidActionsAdapter(randomList,this);
        recyclerView.setAdapter(adapterRandom);

    }


    public void todaysActivity(View view) {

        randomList.clear();
        randomList = getTodaysActivities();
        adapterRandom = new ChlidActionsAdapter(randomList,this);
        recyclerView.setAdapter(adapterRandom);    }


    private List<ChildActivities> getTodaysActivities() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date today = new Date();

        List<ChildActivities>list = new ArrayList<ChildActivities>();
        for(int i = 0;i<childActionActivities.size();i++) {
            if (childActionActivities.get(i).getActivityDate().equals(formatter.format(today))) ;
            list.add(childActionActivities.get(i));
            Log.d("DATE cal",formatter.format(today));
            Log.d("Date ACT",childActionActivities.get(i).getActivityDate());
        }
        return list;
    }

    private List<ChildActivities> getActivitiesToDo() {
        List<ChildActivities>list = new ArrayList<ChildActivities>();
        for(int i = 0;i<childActionActivities.size();i++) {
            if (childActionActivities.get(i).getActivityStatus().equals("pending"))
                list.add(childActionActivities.get(i));
        }
        return list;
    }


    private List<ChildActivities> getCompletedActivities() {
        List<ChildActivities>list = new ArrayList<ChildActivities>();
        for(int i = 0;i<childActionActivities.size();i++) {
            if (childActionActivities.get(i).getActivityStatus().equals("completed"))
                list.add(childActionActivities.get(i));
        }
        Log.d("SIZE",Integer.toString(list.size()));
        return list;
    }

}