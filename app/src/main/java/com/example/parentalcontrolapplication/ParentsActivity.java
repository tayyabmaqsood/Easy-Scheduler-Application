package com.example.parentalcontrolapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ParentsActivity extends AppCompatActivity {

    TextView userRole;
    TextView userName;
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
}