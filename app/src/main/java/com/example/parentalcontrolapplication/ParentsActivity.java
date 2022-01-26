package com.example.parentalcontrolapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class ParentsActivity extends AppCompatActivity {

    TextView userRole;
    TextView userName;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapterCh;
    List<Child> childList = new ArrayList<Child>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents);
        userRole = findViewById(R.id.showParentUserRole);
        userName = findViewById(R.id.showParentUsername);
        User user = new User();

        DatabaseReference reference = FirebaseDatabase.getInstance("https://parental-control-applica-de957-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userRole.setText(snapshot.child("username").getValue().toString());
                userName.setText(snapshot.child("username").getValue().toString());
                user.setUsername(snapshot.child("username").getValue().toString());
                user.setEmail(snapshot.child("email").getValue().toString());
                user.setUserType(snapshot.child("userType").getValue().toString());
                user.setId(snapshot.getKey().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addNewChild(View view) {
        startActivity(new Intent(this,registerChild.class));
    }

    public void showPersonInfo(View view) {
        Dialog dialog = new Dialog(ParentsActivity.this, android.R.style.Widget_DeviceDefault);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.showallchild_activities);


        recyclerView = (RecyclerView) dialog.findViewById(R.id.showAllChildActivitiesRecyclerView);
        // means every item of recyclerview has fixed size
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        adapterCh = new AllChildNameAdapter(childList,this);

        DatabaseReference reference = FirebaseDatabase.getInstance("https://parental-control-applica-de957-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Kids");
        reference.orderByChild("parentId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                childList.clear();
                Iterator shots = snapshot.getChildren().iterator();
                while (shots.hasNext()) {

                    DataSnapshot singleShot = (DataSnapshot) shots.next();
                    Child child = new Child();


                    try {
                            child.setChildName(singleShot.child("childName").getValue().toString());

                            child.setChildGender(singleShot.child("childGender").getValue().toString());
                            child.setChildDOB(singleShot.child("childDOB").getValue().toString());
                            child.setChildAge(singleShot.child("childAge").getValue().toString());
                            child.setParentId(singleShot.child("parentId").getValue().toString());
                            Log.d("str",child.toString());
                            childList.add(child);
                            adapterCh = new AllChildNameAdapter(childList, ParentsActivity.this);
                            recyclerView.setAdapter(adapterCh);
                            adapterCh.notifyDataSetChanged();

                    }catch (Exception e){ }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dialog.show();
    }

    public void showAllChildActivities(View view) {
        Dialog dialog = new Dialog(ParentsActivity.this, android.R.style.Widget_DeviceDefault);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.showallchild_activities);

        RecyclerView recyclerView;
        final RecyclerView.Adapter[] adapter = new RecyclerView.Adapter[1];
        List<ChildActivities> childActivities;

        recyclerView = (RecyclerView) dialog.findViewById(R.id.showAllChildActivitiesRecyclerView);
        // means every item of recyclerview has fixed size
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        childActivities = new ArrayList<ChildActivities>();

        DatabaseReference reference = FirebaseDatabase.getInstance("https://parental-control-applica-de957-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Kids");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                childActivities.clear();
                Iterator shots = snapshot.getChildren().iterator();
                while (shots.hasNext()) {

                    DataSnapshot singleShot = (DataSnapshot) shots.next();
                    singleShot = singleShot.child("activities");
                    ChildActivities activity = new ChildActivities();
                    try {
                        if (!singleShot.getValue().equals(null)) {
                            HashMap<String, String> value = (HashMap) singleShot.getValue();

                            activity.setChildActivityName(value.get("childActivityName"));
                            activity.setActivityDescription(value.get("activityDescription"));
                            activity.setActivityDate(value.get("activityDate"));
                            activity.setActivityTime(value.get("activityTime"));
                            activity.setChildName(value.get("childName"));
                            childActivities.add(activity);
                            adapter[0] = new AllChildActivitiesAdapter(childActivities, ParentsActivity.this);
                            recyclerView.setAdapter(adapter[0]);
                        }
                    }catch (Exception e){ }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        dialog.show();
    }

//    @Override
//    public void onItemLongClicked(ChildActivities activity) {
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("activityDate", null);
//        result.put("activityDescription", null);
//        result.put("activityTime", null);
//        result.put("childActivityName", null);
//        result.put("childName", null);
//
//        ChildActivities childActivities = new ChildActivities();
//        FirebaseDatabase.getInstance("https://parental-control-applica-de957-default-rtdb.asia-southeast1.firebasedatabase.app/")
//                .getReference("Kids")
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid() + activity.getChildName())
//                .child("activities").updateChildren(result);
//        Toast.makeText(ParentsActivity.this, "Activity Deleted", Toast.LENGTH_SHORT).show();
//    }
}