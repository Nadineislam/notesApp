package com.example.notes.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.notes.NotesActivity;
import com.example.notes.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {
    ActivitySignUpBinding binding;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        binding.txtLogin.setOnClickListener(view -> startActivity(new Intent(getBaseContext(), NotesActivity.class)));
        binding.signBtn.setOnClickListener(view -> {
            String email = String.valueOf(binding.etEmail.getText());
            String password = String.valueOf(binding.etPassword.getText());
            String name = String.valueOf(binding.etName.getText());
            if (TextUtils.isEmpty(email)) {
                binding.etEmail.setError("Email is required");
                binding.etEmail.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                binding.etPassword.setError("Email is required");
                binding.etPassword.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(name)) {
                binding.etName.setError("Name is required");
                binding.etName.requestFocus();
                return;
            }
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(SignUp.this, "Check Your Email To Verify Your Account", Toast.LENGTH_SHORT).show();
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    if (firebaseUser != null) {
                        firebaseUser.sendEmailVerification();
                        finish();
                    }
                } else {
                    Toast.makeText(getBaseContext(), String.valueOf(task.getException()), Toast.LENGTH_SHORT).show();
                }
            });


        });


    }
}