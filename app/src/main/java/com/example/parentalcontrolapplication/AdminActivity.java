package com.example.parentalcontrolapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class AdminActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        recyclerView = (RecyclerView) findViewById(R.id.adminRecyclerView);
        // means every item of recyclerview has fixed size
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        userList = new ArrayList<>();


        DatabaseReference reference = FirebaseDatabase.getInstance("https://parental-control-applica-de957-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                Iterator shots = snapshot.getChildren().iterator();
                while (shots.hasNext()) {

                    DataSnapshot singleShot = (DataSnapshot) shots.next();

                    if(singleShot.getKey().toString().equals(FirebaseAuth.getInstance().getUid().toString())) {
                        TextView showUserName = findViewById(R.id.showUsername);
                        TextView showUserRole = findViewById(R.id.showUserRole);
                        showUserName.setText(singleShot.child("username").getValue().toString().toUpperCase(Locale.ROOT));
                        showUserRole.setText(singleShot.child("userType").getValue().toString().toUpperCase(Locale.ROOT));
                        continue;
                    }

                    User usr = new User();
                    usr.setUsername(singleShot.child("username").getValue().toString().toUpperCase(Locale.ROOT));
                    usr.setEmail(singleShot.child("email").getValue().toString());
                    usr.setId(singleShot.getKey().toString());
                    usr.setUserType(singleShot.child("userType").getValue().toString());
                    userList.add(usr);
                }
                adapter = new AdminAdapter(userList,AdminActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}