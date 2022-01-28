package com.example.parentalcontrolapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class ParentsActivity extends AppCompatActivity{

    TextView userName;
    List<Child> childList = new ArrayList<Child>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents);

        userName = findViewById(R.id.showParentUsername);
        User user = new User();

        DatabaseReference reference = FirebaseDatabase.getInstance("https://parental-control-applica-de957-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

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

    // Register New Child
    public void addNewChild(View view) {
        startActivity(new Intent(this,registerChild.class));
    }




    public void showPersonInfo(View view) {
        startActivity(new Intent(this, RegisteredChildrenActivity.class));
    }
    public void showAllChildActivities(View view) {
        try {
            startActivity(new Intent(this, AllChildActivitiesIndex.class));

        }catch (Exception e)
        { Log.d("Exception", e.getMessage().toString());}
    }
}