package com.example.kim_j_project6;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private Button logInButton;
    private Button registerButton;
    private EditText usernameText;
    private EditText passwordText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();

        // from xml
        logInButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        usernameText = findViewById(R.id.usernameEditText);
        passwordText = findViewById(R.id.passwordEditText);

        // button actions
        logInButton.setOnClickListener(v -> logInUser());
        registerButton.setOnClickListener(v -> registerUser());
    }

    // validates username and password
    private void logInUser() {
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Empty Entries", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // log in success
                        Log.i("HERE LOGIN", "log in success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        // updateUI(user);
                    } else {
                        // log in failed
                        Log.i("HERE LOGIN", "log in failed");
                        Toast.makeText(LoginActivity.this, "Log In Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // create new user
    private void registerUser() {
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Empty Entries", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // sign up success
                        Log.i("HERE LOGIN", "registration success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        // updateUI(user)
                    } else {
                        // sign up failed
                        Log.i("HERE LOGIN", "registration failed");
                        Toast.makeText(LoginActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}