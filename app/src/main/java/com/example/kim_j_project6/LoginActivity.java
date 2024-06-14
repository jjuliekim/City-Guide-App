package com.example.kim_j_project6;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
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
        // Load theme preference
        SharedPreferences sharedPreferences = getSharedPreferences("Theme", MODE_PRIVATE);
        boolean isDarkTheme = sharedPreferences.getBoolean("isDarkTheme", false);
        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // from xml
        Button logInButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);
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
        mAuth.signInWithEmailAndPassword(username + "@email.com", password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // log in success
                        Log.i("HERE LOGIN", "log in success");
                        Toast.makeText(this, "Logging In", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent nextIntent = new Intent(LoginActivity.this, MainPageActivity.class);
                        Log.i("HERE LOGIN", "user: " + user.getEmail());
                        nextIntent.putExtra("user", user);
                        startActivity(nextIntent);
                    } else {
                        // log in failed
                        Log.i("HERE LOGIN", "log in failed");
                        Toast.makeText(LoginActivity.this, "Incorrect Login", Toast.LENGTH_SHORT).show();
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
        mAuth.createUserWithEmailAndPassword(username + "@email.com", password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // sign up success
                        Log.i("HERE LOGIN", "registration success");
                        Toast.makeText(this, "New Account Created", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent nextIntent = new Intent(LoginActivity.this, MainPageActivity.class);
                        nextIntent.putExtra("user", user);
                        Log.i("HERE LOGIN", "user: " + user.getEmail());
                        startActivity(nextIntent);
                    } else {
                        // sign up failed
                        Log.i("HERE LOGIN", "registration failed");
                        Toast.makeText(LoginActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 101) {
            // Check if the permission was granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("HERE RECEIVER", "perms granted");
            } else {
                Log.i("HERE RECEIVER", "perms denied");
            }
        }
    }

}