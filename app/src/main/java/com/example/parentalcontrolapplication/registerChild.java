package com.example.parentalcontrolapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class registerChild extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private Button dateDialogButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_child);

        Spinner spinner = (Spinner) findViewById(R.id.childGanderSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.genderArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        initDatePicker();
        dateButton = (Button) findViewById(R.id.childDOB);
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

        //Activity Selection Spinner
        Spinner spinner =  dialog.findViewById(R.id.childActivitySelection);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.childActivities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Description, Date, Time
        EditText description = dialog.findViewById(R.id.childActivityDescription);
        Button date = dialog.findViewById(R.id.childActivityDate);
        Button time = dialog.findViewById(R.id.childActivityTime);


        Button cancelBtn = dialog.findViewById(R.id.cancelRegisterActivity);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dateDialogButton = null;
            }
        });

        dateDialogButton = dialog.findViewById(R.id.childActivityDate);
        dateDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        Button addChildActivity = dialog.findViewById(R.id.addChildActivity);
        addChildActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spinner.getSelectedItemId() == 0){
                }
                else if(description.getText().length() == 0) {
                    description.setError("Enter valid description");
                    description.requestFocus();
                }
                else if(date.getText().equals("Select Date")){
                    date.setError("Select Date");
                    date.requestFocus();
                }
                else if(time.getText().equals("")){
//                    time.setError("Select Time");
//                    time.requestFocus();
                }else {
                    Toast.makeText(registerChild.this, "Activity Added", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        });

        dialog.show();


    }
}

















