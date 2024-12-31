package com.example.androidstudiofinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//LOGIN SCREEN
public class MainActivity extends AppCompatActivity {
    //CheckBox rememberme;

    Button loginButton;
    Button signUpButton;
    EditText emailField;
    EditText passwordField;
    FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        //rememberme = findViewById(R.id.id_RememberMe);
        loginButton = findViewById(R.id.id_loginButton);
        signUpButton = findViewById(R.id.id_signUpButton);
        emailField = findViewById(R.id.id_emailField);
        passwordField = findViewById(R.id.id_passwordField);
        mAuth = FirebaseAuth.getInstance();


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                loginUser(email, password);

            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                signUpUser(email, password);
            }
        });
    }



    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                Log.d("Login", "signInWithEmail:success");
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("emailfrommain", email);
                intent.putExtra("email", email);
                intent.putExtra("passowrd", password);
                startActivity(intent);
                finish();
            } else {
                Log.w("Login", "signInWithEmail:failure", task.getException());
                Toast.makeText(this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signUpUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                saveUsers();
                Log.d("SignUp", "createUserWithEmail:success");
                Toast.makeText(MainActivity.this, "Registration successful.", Toast.LENGTH_SHORT).show();
            } else {
                Log.w("SignUp", "createUserWithEmail:failure", task.getException());
                Toast.makeText(MainActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUsers() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        UserModel userModel = new UserModel(FirebaseAuth.getInstance().getUid(), emailField.getText().toString(), passwordField.getText().toString());
        databaseReference.child("Users").push().setValue(userModel)
                .addOnSuccessListener(aVoid -> Log.d("sendMessages", "User sent successfully"))
                .addOnFailureListener(e -> Log.e("sendMessages", "Failed to send user", e));
    }

}
