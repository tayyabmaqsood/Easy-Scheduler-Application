package com.example.parentalcontrolapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FrontPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);

    }

    public void goToLogin(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void gotoSignup(View view) {
        startActivity(new Intent(this, SignUpActivity.class));

    }
}