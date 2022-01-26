package com.example.parentalcontrolapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity  {
    EditText emailEditText;
    EditText passwordEditText;
    String emailValidationPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailEditText = findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        progressDialog = new ProgressDialog(MainActivity.this);
        mAuth = FirebaseAuth.getInstance();

    }



    public void createNewAccount(View view) {
        startActivity(new Intent(this,SignUpActivity.class));
    }

    public void loginUser(View view) {
        authenticationLogin();
    }

    private void authenticationLogin() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        Pattern pattern = Pattern.compile(emailValidationPattern);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            emailEditText.setError("Enter correct email");
            emailEditText.requestFocus();
        }
        else if (password.isEmpty() || password.length() < 6) {
            passwordEditText.setError("Please Enter Valid Password");
            passwordEditText.requestFocus();
        }
        else{
            progressDialog.setMessage("Please Wait...");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            //      <====== SignIn with email and password  ===>
            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final boolean[] isParent = {true};
                                //      <=====  Login is Successful then add UserRoll   ===>
                                DatabaseReference reference = FirebaseDatabase.getInstance("https://parental-control-applica-de957-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
                                Query getUser =   reference.orderByChild("email").equalTo(email);

                                getUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){

                                            String userType =  snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userType").getValue(String.class);
                                             if(userType.equals("Parent"))
                                                startActivity(new Intent(MainActivity.this,ParentsActivity.class));
                                        }
                                        else
                                            isParent[0] = false;
                                        if(!isParent[0]) {
                                            progressDialog.dismiss();
                                            startActivity(new Intent(MainActivity.this, ChlidActionActivity.class));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    public void forgetPassword(View view) {
        startActivity(new Intent(this, ForgetPasswordActivity.class));
    }
}