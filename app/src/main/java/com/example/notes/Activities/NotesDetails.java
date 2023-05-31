package com.example.notes.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.notes.databinding.ActivityNotesDetailsBinding;

public class NotesDetails extends AppCompatActivity {
ActivityNotesDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNotesDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent data=getIntent();
        binding.noteTitleDetail.setText(data.getStringExtra("tittle"));
        binding.noteContentDetail.setText(data.getStringExtra("note"));
        binding.fabEdit.setOnClickListener(view -> {
            Intent intent=new Intent(view.getContext(),EditNote.class);
            intent.putExtra("tittle",data.getStringExtra("tittle"));
            intent.putExtra("note",data.getStringExtra("note"));
            intent.putExtra("id",data.getStringExtra("id"));
            view.getContext().startActivity(intent);


        });
    }
}