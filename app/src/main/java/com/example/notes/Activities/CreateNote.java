package com.example.notes.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.notes.databinding.ActivityCreateNoteBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class CreateNote extends AppCompatActivity {
    ActivityCreateNoteBinding binding;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    FirebaseFirestore fireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();
        binding.fabSaveNote.setOnClickListener(view -> {
            String tittle = String.valueOf(binding.etTittle.getText());
            String note = String.valueOf(binding.etNote.getText());
            if (TextUtils.isEmpty(tittle) || TextUtils.isEmpty(note)) {
                Toast.makeText(this, "Both fields are required!", Toast.LENGTH_SHORT).show();
            } else {
                DocumentReference documentReference = fireStore.collection("Notes")
                        .document(firebaseUser.getUid())
                        .collection("My Notes")
                        .document();
                HashMap<String, Object> notes = new HashMap<>();
                notes.put("tittle", tittle);
                notes.put("note", note);
                documentReference.set(notes).addOnSuccessListener(unused -> {
                    Toast.makeText(CreateNote.this, "Notes Added Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getBaseContext(), Notes.class));
                }).addOnFailureListener(e -> Toast.makeText(CreateNote.this, "Failed to create note", Toast.LENGTH_SHORT).show());
            }

        });

    }
}