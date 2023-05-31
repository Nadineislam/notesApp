package com.example.notes.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.notes.NotesActivity;
import com.example.notes.databinding.ActivityForgetPasswordBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
ActivityForgetPasswordBinding binding;
FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        binding.tvBackToLogin.setOnClickListener(view -> startActivity(new Intent(getBaseContext(), NotesActivity.class)));
        binding.btnClickToRecover.setOnClickListener(view -> {
            String email=String.valueOf(binding.etEmailForget.getText());
            if(TextUtils.isEmpty(email)){
                binding.etEmailForget.setError("enter your email");
                binding.etEmailForget.requestFocus();
                return;
            }
           auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
               if(task.isSuccessful()){
                   Toast.makeText(ForgetPassword.this, "Go check your email to reset your password", Toast.LENGTH_SHORT).show();
                   finish();
               }
               else{
                   Toast.makeText(ForgetPassword.this, String.valueOf(task.getException()), Toast.LENGTH_SHORT).show();
               }
           });

        });



    }
}