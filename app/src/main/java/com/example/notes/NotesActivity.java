package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.notes.Activities.ForgetPassword;
import com.example.notes.Activities.Notes;
import com.example.notes.Activities.SignUp;
import com.example.notes.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NotesActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        binding.tvWantToSign.setOnClickListener(view -> startActivity(new Intent(getBaseContext(), SignUp.class)));
        binding.tvForgetPassword.setOnClickListener(view -> startActivity(new Intent(getBaseContext(), ForgetPassword.class)));
        binding.btnLogIn.setOnClickListener(view -> {
            String email=String.valueOf(binding.logEmail.getText());
            String password=String.valueOf(binding.logPassword.getText());
            if (TextUtils.isEmpty(email)){
                binding.logEmail.setError("email is required!");
                binding.logEmail.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(password)){
                binding.logPassword.setError("password is required!");
                binding.logPassword.requestFocus();
                return;
            }

            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    checkEmailVerification();
                }else{
                    Toast.makeText(NotesActivity.this,String.valueOf(task.getException()), Toast.LENGTH_SHORT).show();
                }
            });
        });


    }

    private void checkEmailVerification() {
        FirebaseUser firebaseUser=auth.getCurrentUser();
        if(firebaseUser!=null){
        if(firebaseUser.isEmailVerified()){
            Toast.makeText(NotesActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getBaseContext(), Notes.class));}

        }
    }
}