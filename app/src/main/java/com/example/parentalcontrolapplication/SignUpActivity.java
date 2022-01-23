package com.example.parentalcontrolapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText usernameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    TextView usertypeTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Spinner spinner = (Spinner) findViewById(R.id.userTypeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.userTypeArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void loginAccount(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
       TextView userTypeTextView = findViewById(R.id.userTypeTextview);
        if(!adapterView.getItemAtPosition(i).toString().equals("Select"))
            userTypeTextView.setText(adapterView.getItemAtPosition(i).toString());
        else
        userTypeTextView.setText("User Type");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void registerNewUser(View view) {
         usernameEditText = findViewById(R.id.editTextUsername);
         emailEditText = findViewById(R.id.editTextEmailAddress);
         passwordEditText = findViewById(R.id.editTextPassword);
         usertypeTextview = findViewById(R.id.userTypeTextview);
         User usr = new User(usernameEditText.getText().toString(), emailEditText.getText().toString(),passwordEditText.getText().toString());
    }
}