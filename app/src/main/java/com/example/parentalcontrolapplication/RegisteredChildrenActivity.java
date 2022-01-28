package com.example.parentalcontrolapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class RegisteredChildrenActivity extends AppCompatActivity implements personSelectedListner, childActionSelectListner  {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Child> childList;
    private FirebaseAuth auth;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_children);
        auth = FirebaseAuth.getInstance();
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
                    child.setChildId(singleShot.getKey().toString());
                    childList.add(child);
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //ToolBar Code
        toolbar = (Toolbar) findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this,MainActivity.class));
        finish();
        return true;
    }

    List<ChildActivities>childActionActivities;
    RecyclerView fullInfoRecyclerView;
    RecyclerView.Adapter fullInfoAdapter;
    int dataCountInDb = 0, t1Houre = 0, t1Minute =0;
    Button dateDialogButton;
    DatePickerDialog datePickerDialog;

    //On Click Show Name Recycler view Item click listner
    @Override
    public void clickedItem(Child child) {

        // Open Full Details dialog.................../
        Dialog dialog = new Dialog(RegisteredChildrenActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.viewfulldetails_layout);

        Button btn = dialog.findViewById(R.id.button_close);
        TextView name = dialog.findViewById(R.id.personsNameTextview);
        TextView age = dialog.findViewById(R.id.personsAgeTextview);
        TextView gender = dialog.findViewById(R.id.personsGenderTextview);
        TextView email = dialog.findViewById(R.id.personsEmailTextView);
        TextView dob = dialog.findViewById(R.id.personsDobTextview);


        // Full Info Recycler view.........../
        fullInfoRecyclerView = (RecyclerView) dialog.findViewById(R.id.showActivitiesRecyclerView);
        fullInfoRecyclerView.setHasFixedSize(true);
        fullInfoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        childActionActivities = new ArrayList<>();
        fullInfoAdapter = new ChlidActionsAdapter(childActionActivities, this, this);
        fullInfoRecyclerView.setAdapter(fullInfoAdapter);

        name.setText(child.getChildName());
        age.setText(child.getChildAge());
        gender.setText(child.getChildGender());
        email.setText(child.getChildEmail());
        dob.setText(child.getChildDOB());

        // Close Button Listner
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        //Populating Activities RecyclerView
        DatabaseReference reference = FirebaseDatabase.getInstance("https://parental-control-applica-de957-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Activities");
        Query query = reference.orderByChild("childEmail").equalTo(child.getChildEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                childActionActivities.clear();
                Iterator shots = snapshot.getChildren().iterator();
                dataCountInDb = (int) snapshot.getChildrenCount();
                while (shots.hasNext()) {
                    DataSnapshot singleShot = (DataSnapshot) shots.next();
                    ChildActivities childActivity = new ChildActivities();

                    try {
                        childActivity.setChildActivityName(singleShot.child("childActivityName").getValue().toString());
                        childActivity.setActivityDescription(singleShot.child("activityDescription").getValue().toString());
                        childActivity.setActivityTime(singleShot.child("activityTime").getValue().toString());
                        childActivity.setActivityDate(singleShot.child("activityDate").getValue().toString());
                        childActivity.setActivityStatus(singleShot.child("activityStatus").getValue().toString());
                        childActivity.setChildEmail(singleShot.child("childEmail").getValue().toString());
                        childActivity.setActivityId(singleShot.getKey());
                        childActivity.setCompletedDate(singleShot.child("completedDate").getValue().toString());
                        childActionActivities.add(childActivity);
                        fullInfoRecyclerView.setAdapter(fullInfoAdapter);
                    } catch (Exception e) {
                        Toast.makeText(RegisteredChildrenActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegisteredChildrenActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add More Activites to Child
        FloatingActionButton floatingActionButton = dialog.findViewById(R.id.addNewActivity);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog2 = new Dialog(RegisteredChildrenActivity.this);
                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog2.setCancelable(false);
                dialog2.setContentView(R.layout.child_activities);

                Button timePickerBtn = dialog2.findViewById(R.id.childActivityTime);

                //Activity Selection Spinner
                Spinner spinnerActivityName =  dialog2.findViewById(R.id.childActivitySelection);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(RegisteredChildrenActivity.this,R.array.childActivities, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerActivityName.setAdapter(adapter);

                //Description, Date, Time
                EditText description = dialog2.findViewById(R.id.childActivityDescription);
                Button date = dialog2.findViewById(R.id.childActivityDate);
                Button time = dialog2.findViewById(R.id.childActivityTime);

                //<=================== Time Picker Code
                timePickerBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        TimePickerDialog timePickerDialog = new TimePickerDialog(
                                RegisteredChildrenActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                        t1Houre = i;
                                        t1Minute = i1;
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.set(0,0,0,t1Houre,t1Minute);
                                        timePickerBtn.setText(DateFormat.format("hh:mm aa",calendar));
                                    }
                                },12,0,false

                        );
                        timePickerDialog.updateTime(t1Houre,t1Minute);
                        timePickerDialog.show();
                    }
                });

                // Cancel button click Listner
                Button cancelBtn = dialog2.findViewById(R.id.cancelRegisterActivity);
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog2.dismiss();
                    }
                });

                // <<<=================== Add Date chlid activity listner ======================>>>>>>
                // Date Selector =====================================>
                dateDialogButton = dialog2.findViewById(R.id.childActivityDate);
                dateDialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        initDatePicker();
                        datePickerDialog.show();
                    }
                });

                // Add chlid activities button listner
                Button addChildActivity = dialog2.findViewById(R.id.addChildActivity);
                addChildActivity.setOnClickListener(new View.OnClickListener() {
                    @Override

                    public void onClick(View view) {
                        if(spinnerActivityName.getSelectedItemId() == 0){
                        }
                        else if(description.getText().length() == 0) {
                            description.setError("Enter valid description");
                            description.requestFocus();
                        }
                        else if(date.getText().equals("Select Date")){
                            date.setError("Select Date");
                            date.requestFocus();
                        }
                        else if(time.getText().equals("Select Time")){
                            time.setError("Select Time");
                            time.requestFocus();
                        }
                        else {
                            ChildActivities childActivity = new ChildActivities();
                            String ActivityName = spinnerActivityName.getSelectedItem().toString();
                            String desc = description.getText().toString();
                            String ActivityDate = date.getText().toString();
                            String ActivityTime = time.getText().toString();
                            childActivity.setActivityDate(ActivityDate);
                            childActivity.setActivityTime(ActivityTime);
                            childActivity.setActivityDescription(desc);
                            childActivity.setChildActivityName(ActivityName);
                            childActivity.setActivityStatus("pending");
                            childActivity.setChildEmail(child.getChildEmail());
                            childActivity.setCompletedDate("None");

                            //Add Data To firebase
                            FirebaseDatabase.getInstance("https://parental-control-applica-de957-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                    .getReference("Activities");
                            Query query1 = reference.orderByChild("childEmail").equalTo(child.getChildEmail());
                                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            dataCountInDb = (int)snapshot.getChildrenCount();
                                            DatabaseReference reference = FirebaseDatabase.getInstance("https://parental-control-applica-de957-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                                    .getReference("Activities")
                                                    .child( child.getChildId()+"|activity"+(dataCountInDb+1));
                                            reference.setValue(childActivity);
                                            childActionActivities.add(childActivity);
                                            fullInfoRecyclerView.setAdapter(fullInfoAdapter);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(RegisteredChildrenActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            Toast.makeText(RegisteredChildrenActivity.this, "Activity Successfully added", Toast.LENGTH_SHORT).show();
                            dialog2.dismiss();
                        }
                    }

                });
                dialog2.show();
            }
        });

        dialog.show();

    }

    public void addNewChildParentIndexView(View view) {
        startActivity(new Intent(this, registerChild.class));
    }

    @Override
    public void onItemClicked(ChildActivities activity) {
        Dialog dialog = new Dialog(RegisteredChildrenActivity.this,R.style.Base_Theme_AppCompat_DialogWhenLarge);
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
    }

    @Override
    public void onItemLongClicked(ChildActivities activity) {

    }

    private String getTodaysDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month = month + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day,month,year);
    }
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month+=1;
                String date = makeDateString(day,month,year);
                dateDialogButton.setText(date);
            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int style = android.app.AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this,style,dateSetListner,year,month,day);
    }

    private String makeDateString(int day, int month, int year) {
        return day+" - "+getmonthFormate(month)+" - "+year;
    }
    private String getmonthFormate(int month) {
        if(month == 1)
            return "Jan";
        if(month == 2)
            return "Feb";
        if(month == 3)
            return "March";
        if(month == 4)
            return "April";
        if(month == 5)
            return "May";
        if(month == 6)
            return "June";
        if(month == 7)
            return "July";
        if(month == 8)
            return "Aug";
        if(month == 9)
            return "Sept";
        if(month == 10)
            return "Oct";
        if(month == 11)
            return "Nov";
        if(month == 12)
            return "Dec";
        return "Jan";
    }
    public void openDatePicker(View view) {
        datePickerDialog.show();
    }
}