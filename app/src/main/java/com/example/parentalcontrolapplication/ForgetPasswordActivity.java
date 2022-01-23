package com.example.parentalcontrolapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgetPasswordActivity extends AppCompatActivity {

    EditText emailEditText;
    String emailValidationPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        emailEditText = findViewById(R.id.forgetPasswordEmailEditText);
    }

    public void forgetSubmitClick(View view) {
        resetPassword();
    }

    private void resetPassword() {
        String email = emailEditText.getText().toString();
        Pattern pattern = Pattern.compile(emailValidationPattern);
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches()){
            emailEditText.setError("Enter correct email");
            emailEditText.requestFocus();
        }else{
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ForgetPasswordActivity.this,"Reset password email successfully sent to your emaill address",Toast.LENGTH_LONG).show();
                                finish();
                            }else
                                Toast.makeText(ForgetPasswordActivity.this,task.getException().getMessage().toString(),Toast.LENGTH_LONG).show();

                        }
                    });
        }
    }
}