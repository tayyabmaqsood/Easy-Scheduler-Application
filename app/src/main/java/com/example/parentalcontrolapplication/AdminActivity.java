package com.example.parentalcontrolapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
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

public class AdminActivity extends AppCompatActivity implements adminSelectListner{
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

                    if(singleShot.getKey().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
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
                adapter = new AdminAdapter(userList,AdminActivity.this,AdminActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    // Select item from recycler view
    private String clickedEmail = "";
    private int itemClicked = 0;

    @Override
    public void onItemClicked(User user) {
        if (clickedEmail.equals(user.getEmail())) {
            itemClicked++;
            if(itemClicked == 2) {


                itemClicked = 0;
                clickedEmail = "";
            }
        }
        else {
            clickedEmail = user.getEmail();
            itemClicked = 1;
        }

    }

    //  <======= When Long Click Item   ===>
    @Override
    public void onItemLongClicked(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
        builder.setMessage("Are you sure?");
        builder.setTitle("Delete Record!");
        builder.setCancelable(false);
        builder.setPositiveButton(
                "Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            removeUser(user);
                        } catch (FirebaseAuthException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        builder.setNegativeButton(
                "Dismis",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }
        );
        AlertDialog alertDialog  =  builder.create();
        alertDialog.show();
    }

    private void removeUser(User user) throws FirebaseAuthException {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        adapter.notifyDataSetChanged();
    }
}


















