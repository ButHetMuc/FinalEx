package com.example.finalex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Application extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;

    private  String userID;
    private Button btnLogOut,btnDelete,btnUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_auth);

        btnLogOut = (Button) findViewById(R.id.btnSignOut);
        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnUpdate = (Button) findViewById(R.id.btn_update);

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Application.this,LoginUser.class));
            }
        });
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView welCome = (TextView) findViewById(R.id.id_tv_wellcome);
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null){
                    String name = userProfile.name;
                    String email = userProfile.email;
                    welCome.setText("Welcome "+ name +"!");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Application.this, "Something wrong happened", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child(userID).removeValue();
                Toast.makeText(Application.this, "User has been delete!", Toast.LENGTH_SHORT).show();
                startActivity( new Intent(Application.this,LoginUser.class ));
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = findViewById(R.id.id_name_appli).toString();
                String email = findViewById(R.id.id_email_appli).toString();
                updateData(name,email);
            }
        });
    }

    private void updateData(String name,String email) {
        HashMap user = new HashMap();
        user.put("name",name);
        user.put("email",email);

        reference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        reference.updateChildren(user).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    Toast.makeText(Application.this, "update successfully!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Application.this, "Update failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}