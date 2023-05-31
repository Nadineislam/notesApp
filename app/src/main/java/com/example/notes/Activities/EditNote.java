package com.example.notes.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.notes.databinding.ActivityEditNoteBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditNote extends AppCompatActivity {
    Intent data;
    FirebaseFirestore fireStore;
    FirebaseAuth auth;
    FirebaseUser user;
    ActivityEditNoteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fireStore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        data = getIntent();
        binding.etTittleEdit.setText(data.getStringExtra("tittle"));
        binding.etNoteEdit.setText(data.getStringExtra("note"));
        binding.fabSaveEditedNote.setOnClickListener(view -> {
            String newNote = String.valueOf(binding.etNoteEdit.getText());
            String newTittle = String.valueOf(binding.etTittleEdit.getText());
            DocumentReference documentReference = fireStore.collection("Notes")
                    .document(user.getUid()).collection("My Notes").document(data.getStringExtra("id"));
            Map<String, Object> notes = new HashMap<>();
            notes.put("new tittle", newTittle);
            notes.put("new note", newNote);
            documentReference.set(notes).addOnSuccessListener(unused -> startActivity(new Intent(getBaseContext(), Notes.class)));
        });


    }
}