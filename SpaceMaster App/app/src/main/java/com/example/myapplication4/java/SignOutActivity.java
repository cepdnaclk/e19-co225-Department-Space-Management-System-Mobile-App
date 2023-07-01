package com.example.myapplication4.java;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignOutActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Perform Firebase sign-out
        signOutFirebase();

        // Perform local sign-out operations here

        // Redirect to the login screen
        Intent intent = new Intent(SignOutActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    private void signOutFirebase() {
        mAuth.signOut();
    }
}
