package com.example.parentalcontrolapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class RegisteredChildrenActivity extends AppCompatActivity implements personSelectedListner {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Child> childList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_children);
        recyclerView = (RecyclerView) findViewById(R.id.childPersonalInfo);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        childList = new ArrayList<>();
        adapter = new PersonInfoAdapter(childList,this,this);


        DatabaseReference reference = FirebaseDatabase.getInstance("https://parental-control-applica-de957-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("children");
        reference.orderByChild("parentId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                childList.clear();
                Iterator shots = snapshot.getChildren().iterator();
                while (shots.hasNext()) {
                    DataSnapshot singleShot = (DataSnapshot) shots.next();
                    Child child = new Child();
                    child.setChildName(singleShot.child("childName").getValue().toString());
                    child.setChildAge(singleShot.child("childAge").getValue().toString());
                    child.setChildGender(singleShot.child("childGender").getValue().toString());
                    child.setChildDOB(singleShot.child("childDOB").getValue().toString());
                    child.setChildEmail(singleShot.child("childEmail").getValue().toString());
                    child.setParentId(singleShot.child("parentId").getValue().toString());
                    childList.add(child);
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void clickedItem(Child child) {
        Toast.makeText(RegisteredChildrenActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
        CardView cardView = findViewById(R.id.personalInfoCardView);

    }

    public void addNewChild(View view) {
        startActivity(new Intent(this, registerChild.class));
    }
}