package com.example.parentalcontrolapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class registerChild extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private Button dateDialogButton;
    private ListView listView;
    private Spinner spinner;
    int t1Houre;
    int  t1Minute;


    private List<ChildActivities> childActivitiesList = new ArrayList<ChildActivities>();
    ArrayAdapter<ChildActivities> activitiesAdpater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_child);

        spinner = (Spinner) findViewById(R.id.childGanderSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.genderArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        initDatePicker();
        dateButton = (Button) findViewById(R.id.childDOB);
        listView = findViewById(R.id.activitiesListView);
        activitiesAdpater = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,childActivitiesList);
        listView.setAdapter(activitiesAdpater);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                childActivitiesList.remove(i);
                Toast.makeText(registerChild.this, "Activity Deleted", Toast.LENGTH_SHORT).show();
                activitiesAdpater.notifyDataSetChanged();
                return false;
            }
        });
    }

    //  <=== Date Picker functions  ===>
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
                if (dateDialogButton != null){
                   if (dateDialogButton.getText().equals("Select Date"))
                       dateDialogButton.setText(date);
                }
                else
                    dateButton.setText(date);
            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
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


    // <=================== Other Than Date Picker=======================
        //<============= ADD Activities Function    ====================
    public void addActivitiesForChild(View view) {
        String activityName;
        Dialog dialog = new Dialog(registerChild.this);
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
                        registerChild.this,
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
                dateDialogButton = null;
            }
        });

        // Add Date chlid activity listner
        dateDialogButton = dialog.findViewById(R.id.childActivityDate);
        dateDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                        childActivitiesList.add(childActivity);
                        activitiesAdpater.notifyDataSetChanged();
                        dialog.dismiss();
                }
            }
        });

        dialog.show();


    }

    //Register new child onClickListner
    public void registerNewKid(View view) {
        EditText childName = findViewById(R.id.editTextChildNameReg);
        EditText childAge = findViewById(R.id.editTextChildAge);
        String childGender = spinner.getSelectedItem().toString();
        Button childDOB = findViewById(R.id.childDOB);
        EditText childEmail = findViewById(R.id.personsEmail);
        EditText childPassword = findViewById(R.id.childPassword);
        EditText childConfirmPassword = findViewById(R.id.childConfirmPassword);
        addEmailToChildActivities(childEmail.getText().toString());
        String parentId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(childName.getText().toString().equals(null)){
            childName.setError("Please Enter Name");
            childName.requestFocus();
        }
        else if(childAge.getText().toString().equals(null)){
            childAge.setError("Please Enter Age");
            childAge.requestFocus();
        }else if(spinner.getSelectedItemId() == 0){
            spinner.requestFocus();
        }else if(childDOB.getText().toString().equals("Date Of Birth")){
            childDOB.setError("Please Enter DOB");
            childDOB.requestFocus();
        }else if (childEmail.getText().toString().equals(null)){
            childEmail.setError("Please Select DOB");
            childEmail.requestFocus();
        } else if (childPassword.getText().toString().length() < 6){
            childPassword.setError("Enter Valid Password");
            childPassword.requestFocus();
        }else if (!childPassword.getText().toString().equals(childConfirmPassword.getText().toString())){
            childConfirmPassword.setError("Password doesn't match");
            childConfirmPassword.requestFocus();
        }
        else {
            ProgressDialog progressDialog = new ProgressDialog(registerChild.this);
//            progressDialog.setMessage("Please Wait...");
//            progressDialog.setTitle("Registration");

            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            Child childUser = new Child(childName.getText().toString(),
                    childAge.getText().toString(),
                    childDOB.getText().toString(),
                    childGender,
                    parentId,
                    childEmail.getText().toString(),
                    childPassword.getText().toString()
                    );

            // Add Child To firebase auth
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(childUser.getChildEmail(),childUser.getChildPassword())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            //Add Child to Children table
                            DatabaseReference reference = FirebaseDatabase.getInstance("https://parental-control-applica-de957-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                    .getReference("children")
                                    .child(auth.getCurrentUser().getUid());

                            reference.setValue(childUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        //Add Activites to Activity table


                                        if(childActivitiesList.size() != 0){
                                            for (int i = 0; i<childActivitiesList.size();i++) {
                                                DatabaseReference reference = FirebaseDatabase.getInstance("https://parental-control-applica-de957-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                                        .getReference("Activities")
                                                        .child(auth.getCurrentUser().getUid() + "|activity"+(i+1) );
                                                reference.setValue(childActivitiesList.get(i));
                                            }
                                            progressDialog.dismiss();
                                            Toast.makeText(registerChild.this, "Kid info is successFully added", Toast.LENGTH_LONG).show();
                                            finish();
                                        }

                                    } else {
                                        if (progressDialog.isShowing())
                                            progressDialog.dismiss();
                                        Toast.makeText(registerChild.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                }
                            });


                        }
                    });
        }



    }

    private void addEmailToChildActivities(String email) {
        for (int i = 0; i<childActivitiesList.size();i++)
           childActivitiesList.get(i).setChildEmail(email);
    }


}

















