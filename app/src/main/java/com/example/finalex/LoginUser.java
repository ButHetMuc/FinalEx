package com.example.finalex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginUser extends AppCompatActivity implements View.OnClickListener {
    private TextView forgotPassword;
    private EditText editTextEmail,editTextPassword;
    private Button btnSingIn;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_userr);
        forgotPassword = (TextView) findViewById(R.id.id_forgot_pass_login);
        forgotPassword.setOnClickListener(this);

        btnSingIn = findViewById(R.id.btn_signin_signin);
        btnSingIn.setOnClickListener(this);

        editTextEmail = findViewById(R.id.id_email_login);
        editTextPassword = findViewById(R.id.id_password_login);

        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

        forgotPassword = (TextView) findViewById(R.id.id_forgot_pass_login);
        forgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_signin_signin:
                userLogin();
                break;
            case R.id.id_forgot_pass_login:
                startActivity(new Intent(LoginUser.this,ForgotPassword.class));
                break;
        }
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if (email.isEmpty()){
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide valid email!");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()){
            editTextPassword.setError("Password is required!");
            editTextPassword.selectAll();
            editTextPassword.requestFocus();
            return;
        }
        if (password.length()<6){
            editTextPassword.setError("Min password length should be 6 characters ");
            editTextPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()){
                        startActivity(new Intent(LoginUser.this,Application.class));
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(LoginUser.this, "Please check your email to verify your account!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginUser.this, "Failed to login! please check your credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}