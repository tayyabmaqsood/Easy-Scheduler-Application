package com.example.parentalcontrolapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ChlidActionActivity extends AppCompatActivity implements  childActionSelectListner  {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ChildActivities> childActionActivities;

    private  List<ChildActivities> allList;
    private  List<ChildActivities> todaysList;
    private  List<ChildActivities> completeList;
    private RecyclerView recyclerViewRandom;
    private RecyclerView.Adapter allAdapter;
    private RecyclerView.Adapter todaysAdapter;
    private RecyclerView.Adapter completeAdapter;
    private String tabSelection = "";

    private int t1Houre;
    private int t1Minute;
    private int dataCountInDb = 0;
    FirebaseAuth auth= FirebaseAuth.getInstance();
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setContentView(R.layout.activity_chlid_action);
        recyclerView = (RecyclerView) findViewById(R.id.childActivitiesIndexRecyclerView);
        // means every item of recyclerview has fixed size
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        childActionActivities = new ArrayList<>();
        adapter = new ChlidActionsAdapter(childActionActivities,this,this);
        recyclerView.setAdapter(adapter);



        // Intializing different lists and adapter
        allList = new ArrayList<>();
        allAdapter =  new ChlidActionsAdapter(allList,this);
        todaysList = new ArrayList<>();
        todaysAdapter =  new ChlidActionsAdapter(todaysList,this);
        completeList = new ArrayList<>();
        completeAdapter =  new ChlidActionsAdapter(completeList,this);



        String childId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance("https://parental-control-applica-de957-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Activities");

        Query query = reference.orderByChild("childEmail").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        query.addValueEventListener(new ValueEventListener() {
            private View view;
            boolean isAlreadyShow = false;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                childActionActivities.clear();


                Iterator shots = snapshot.getChildren().iterator();
                dataCountInDb = (int)snapshot.getChildrenCount();
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
                        childActivity.setActivityId(singleShot.getKey());
                        childActivity.setCompletedDate(singleShot.child("completedDate").getValue().toString());
                        childActionActivities.add(childActivity);

                        if(!isAlreadyShow) {
                            recyclerView.setAdapter(adapter);
                            isAlreadyShow = true;
                        }
                    }catch (Exception e){ }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this,MainActivity.class));
        return true;
    }

    // <======================= Filters Activities of the basis of Buttons on ChildActivity.xml=============

    public void updateAllActivitiesList(){
        allList.clear();
        allList = getActivitiesToDo();
        allAdapter = new ChlidActionsAdapter(allList,this,this);
        recyclerView.setAdapter(allAdapter);

    }
    public void showAllActivities(View view){
        RelativeLayout todoActivity = findViewById(R.id.allActivitiesCardView);
        todoActivity.setBackgroundColor(getResources().getColor(R.color.whiteshade));
        RelativeLayout carcard = findViewById(R.id.todaysAcativityCardView);
        carcard.setBackgroundColor(Color.TRANSPARENT);
        RelativeLayout restcard = findViewById(R.id.completedActivitiesCardView);
        restcard.setBackgroundColor(Color.TRANSPARENT);
        tabSelection = "allActivities";
        updateAllActivitiesList();

    }

    public void updateCompleteList(){
        completeList.clear();
        completeList = getCompletedActivities();
        completeAdapter = new ChlidActionsAdapter(completeList,this,this);
        recyclerView.setAdapter(completeAdapter);
    }
    public void completedActivities(View view) {

        RelativeLayout todoActivity = findViewById(R.id.allActivitiesCardView);
        todoActivity.setBackgroundColor(Color.TRANSPARENT);
        RelativeLayout carcard = findViewById(R.id.todaysAcativityCardView);
        carcard.setBackgroundColor(Color.TRANSPARENT);
        RelativeLayout restcard = findViewById(R.id.completedActivitiesCardView);
        restcard.setBackgroundColor(getResources().getColor(R.color.whiteshade));
        updateCompleteList();
    }

    public void updateTodaysList(){

        todaysList.clear();
        todaysList = getTodaysActivities();
        todaysAdapter = new ChlidActionsAdapter(todaysList,this,this);
        recyclerView.setAdapter(todaysAdapter);
    }
    public void todaysActivity(View view) {
        RelativeLayout todoActivity = findViewById(R.id.allActivitiesCardView);
        todoActivity.setBackgroundColor(Color.TRANSPARENT);
        RelativeLayout carcard = findViewById(R.id.todaysAcativityCardView);
        carcard.setBackgroundColor(getResources().getColor(R.color.whiteshade));;
        RelativeLayout restcard = findViewById(R.id.completedActivitiesCardView);
        restcard.setBackgroundColor(Color.TRANSPARENT);
        tabSelection = "todaysActivities";
        updateTodaysList();
 }


    private List<ChildActivities> getTodaysActivities() {
        List<ChildActivities>list = new ArrayList<ChildActivities>();
        for(int i = 0;i<childActionActivities.size();i++) {

            if ( childActionActivities.get(i).getActivityDate().equals(getTodaysDate())) {
                if(!childActionActivities.get(i).getActivityStatus().equals("completed"))
                    list.add(childActionActivities.get(i));
            }
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
        return list;
    }


    // <<<<<<<<< ========== Recycler View ClickListners ================== >>>>>>>>>>>>>
    // Double Select item from recycler view
    private String clickedEmail = "";
    private int itemClicked = 0;
    @Override
    public void onItemClicked(ChildActivities activity) {
        if (clickedEmail.equals(activity.getChildEmail())) {
            itemClicked++;
            if(itemClicked == 2) {
                //Do thing on double click
                Dialog dialog = new Dialog(ChlidActionActivity.this,R.style.Base_Theme_AppCompat_DialogWhenLarge);
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
                status.setText(activity.getChildActivityName());

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

    //<<<<<<<<<<<<<<< ===================== Long Item Clicked Listner ====================>>>>>>>

    @Override
    public void onItemLongClicked(ChildActivities activity) {
        if(!activity.getActivityStatus().equals("completed")) {
            activity.setCompletedDate(getTodaysDate());
            AlertDialog.Builder builder = new AlertDialog.Builder(ChlidActionActivity.this);
            builder.setMessage("Are you sure?");
            builder.setTitle("Mark Activity as Complete!");
            builder.setCancelable(false);
            builder.setPositiveButton(
                    "Complete",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {

                                String childId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                DatabaseReference reference = FirebaseDatabase.getInstance("https://parental-control-applica-de957-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Activities");
                                reference.child(activity.getActivityId()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        snapshot.getRef().child("activityStatus").setValue("completed");
                                        snapshot.getRef().child("completedDate").setValue(getTodaysDate());
                                        if(tabSelection.equals("allActivities")) {
                                            updateAllActivitiesList();
                                        }
                                        else if (tabSelection.equals("todaysActivities"))
                                            updateTodaysList();
                                        else
                                            adapter.notifyDataSetChanged();
                                        Toast.makeText(ChlidActionActivity.this, "Activity Successfully updated", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.d("Child",error.getMessage());
                                    }

                                });
                            } catch (Exception e) {
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
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }


    Button dateDialogButton;
    DatePickerDialog datePickerDialog;
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

    //<<<============= Code For Self adding into activiy =========== >>>>>>>>
    public void addActivityForSelf(View view) {
            String activityName;
            Dialog dialog = new Dialog(ChlidActionActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.child_activities);

            Button timePickerBtn = dialog.findViewById(R.id.childActivityTime);

            //Activity Selection Spinner
            Spinner spinnerActivityName =  dialog.findViewById(R.id.childActivitySelection);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.childActivities, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerActivityName.setAdapter(adapter);

            //Description, Date, Time
            EditText description = dialog.findViewById(R.id.childActivityDescription);
            Button date = dialog.findViewById(R.id.childActivityDate);
            Button time = dialog.findViewById(R.id.childActivityTime);

            //<=================== Time Picker Code
            timePickerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            ChlidActionActivity.this,
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
            Button cancelBtn = dialog.findViewById(R.id.cancelRegisterActivity);
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            // <<<=================== Add Date chlid activity listner ======================>>>>>>
            // Date Selector =====================================>
            dateDialogButton = dialog.findViewById(R.id.childActivityDate);
            dateDialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initDatePicker();
                    datePickerDialog.show();
                }
            });


            // Add chlid activities button listner
            Button addChildActivity = dialog.findViewById(R.id.addChildActivity);
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
                        childActivity.setChildEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                        childActivity.setCompletedDate("None");
                        //Add Data To firebase


                        DatabaseReference reference = FirebaseDatabase.getInstance("https://parental-control-applica-de957-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                .getReference("Activities")

                                .child(auth.getCurrentUser().getUid() + "|activity"+(dataCountInDb+1));
                        reference.setValue(childActivity);
                        childActionActivities.add(childActivity);

                        if (tabSelection.equals("todaysActivities"))
                            updateTodaysList();
                        else
                            updateAllActivitiesList();

                        Toast.makeText(ChlidActionActivity.this, "Activity Successfully added", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            });

            dialog.show();


        }


}