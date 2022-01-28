package com.example.parentalcontrolapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    EditText usernameEditText ;
    EditText emailEditText;
    EditText passwordEditText;
    TextView usertypeTextview;
    Button registerBtn;
    String emailValidationPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.userTypeArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        usernameEditText = findViewById(R.id.editTextChildname);
        emailEditText = findViewById(R.id.personsEmail);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        progressDialog = new ProgressDialog(SignUpActivity.this);
        mAuth = FirebaseAuth.getInstance();
    }

    public void loginAccount(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }


    public void registerNewUser(View view) {
        performAuthentication();
    }

    private void performAuthentication() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String usertype = "Parent";
        Pattern pattern = Pattern.compile(emailValidationPattern);
        Matcher matcher = pattern.matcher(email);

        if ( username.isEmpty()) {
            usernameEditText.setError("Please Enter Valid Username");
            usernameEditText.requestFocus();
        }
        else if(usertype.equals("Select")) {
            usertypeTextview.setError("Please Select valid User");
            usertypeTextview.requestFocus();
        }
        else if (!matcher.matches()) {
            emailEditText.setError("Enter correct email");
            emailEditText.requestFocus();
        }
        else if (password.isEmpty() || password.length() < 6) {
            passwordEditText.setError("Please Enter Valid Password");
            passwordEditText.requestFocus();
        }
        else{
            Log.d("Debug","String");

            progressDialog.setMessage("Please Wait While Registration...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                // we will store additional fields in firebase
                                // like username, usertype
                                User user = new User(username,email,password,usertype);

                                FirebaseDatabase.getInstance("https://parental-control-applica-de957-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {

                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Toast.makeText(SignUpActivity.this, "Successfully Register", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                                        }
                                        else{
                                            if(progressDialog.isShowing())
                                                progressDialog.dismiss();
                                        }
                                    }
                                });
                            }
                            else {
                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                if(progressDialog.isShowing())
                                    progressDialog.dismiss();
                            }
                        }
                    });
        }

    }
}