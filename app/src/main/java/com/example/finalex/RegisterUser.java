package com.example.finalex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener{

    private EditText name,email,password,confirmPassword;
    private ProgressBar progressBar;
    private Button btnRegister;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);
        mAuth = FirebaseAuth.getInstance();

        btnRegister = findViewById(R.id.btn_register_register);
        btnRegister.setOnClickListener(this);

        name = findViewById(R.id.id_name_register);
        email = findViewById(R.id.id_mail_register);
        password = findViewById(R.id.id_password_register);
        confirmPassword = findViewById(R.id.id_confirm_password_register);

        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_register_register:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String name = this.name.getText().toString().trim();
        String email = this.email.getText().toString().trim();
        String password = this.password.getText().toString().trim();
        String confirmPassword = this.confirmPassword.getText().toString().trim();

        if (name.isEmpty()){
            this.name.setError("Name is required!");
            this.name.requestFocus();
            return;
        }
        if (email.isEmpty()){
            this.email.setError("Email is required!");
            this.email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            this.email.setError("Please provide valid email!");
            return;
        }
        if (password.isEmpty()){
            this.password.setError("Password is required!");
            this.password.requestFocus();
            return;
        }
        if (password.length()<6){
            this.password.setError("Min password length should be 6 character!");
            this.password.requestFocus();
            return;
        }
        if (confirmPassword.isEmpty()){
            this.confirmPassword.setError("Confirm password is required!");
            this.confirmPassword.requestFocus();
            return;
        }
        if (!confirmPassword.equals(password)){
            this.confirmPassword.setError("Confirm password is not correct!");
            this.confirmPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            User user = new User(name,email,password);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(RegisterUser.this, "User has been register successfully", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    else {
                                        Toast.makeText(RegisterUser.this, "Failed to register! try againt!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(RegisterUser.this, "Failed to register", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });
    }
}