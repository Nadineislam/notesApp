package com.example.notes.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.notes.NotesActivity;
import com.example.notes.NotesModel;
import com.example.notes.R;
import com.example.notes.databinding.ActivityNotesBinding;
import com.example.notes.databinding.NotesLayoutBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Random;

public class Notes extends AppCompatActivity implements MenuProvider {

    ActivityNotesBinding binding;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    FirebaseFirestore fireStore;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    FirestoreRecyclerAdapter<NotesModel, NotesViewHolder> notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        firebaseUser = auth.getCurrentUser();
        addMenuProvider(this);
        binding.fabAddNote.setOnClickListener(view -> startActivity(new Intent(getBaseContext(), CreateNote.class)));
        Query query = fireStore.collection("Notes")
                .document(firebaseUser.getUid())
                .collection("My Notes")
                .orderBy("tittle", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<NotesModel> allUsers = new FirestoreRecyclerOptions.Builder<NotesModel>().setQuery(query, NotesModel.class).build();
        notesAdapter = new FirestoreRecyclerAdapter<NotesModel, NotesViewHolder>(allUsers) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull NotesViewHolder holder, int position, @NonNull NotesModel model) {
                int colorCode = getRandomColor();
                notesBinding.noteBackground.setCardBackgroundColor(holder.itemView.getResources().getColor(colorCode, null));
                notesBinding.noteTitle.setText(String.valueOf(model.getTittle()));
                notesBinding.noteContent.setText(String.valueOf(model.getNote()));
                String documentId = notesAdapter.getSnapshots().getSnapshot(position).getId();
                holder.itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(view.getContext(), NotesDetails.class);
                    intent.putExtra("tittle", model.getTittle());
                    intent.putExtra("note", model.getNote());
                    intent.putExtra("id", documentId);
                    view.getContext().startActivity(intent);
                    notesAdapter.notifyItemChanged(position);

                });
                notesBinding.noteImage.setOnClickListener(view -> {
                    PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                    popupMenu.setGravity(Gravity.END);
                    popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(menuItem -> {
                        Intent intent = new Intent(view.getContext(), EditNote.class);
                        view.getContext().startActivity(intent);
                        return false;
                    });
                    popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(menuItem -> {
                        DocumentReference documentReference = fireStore.collection("Notes")
                                .document(firebaseUser.getUid())
                                .collection("My Notes").document(documentId);
                        documentReference.delete().addOnSuccessListener(unused -> Toast.makeText(Notes.this, "Note Deleted Successfully", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(Notes.this, "Note can't be deleted", Toast.LENGTH_SHORT).show());

                        return false;
                    });
                    popupMenu.show();
                });

            }

            @NonNull
            @Override
            public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout, parent, false);
                return new NotesViewHolder(view);
            }
        };
        binding.rvNotes.setAdapter(notesAdapter);
        binding.rvNotes.setItemAnimator(null);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        binding.rvNotes.setLayoutManager(staggeredGridLayoutManager);
        binding.rvNotes.setAdapter(notesAdapter);
        notesAdapter.notifyDataSetChanged();

    }

    NotesLayoutBinding notesBinding;

    public class NotesViewHolder extends RecyclerView.ViewHolder {
        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            notesBinding = NotesLayoutBinding.bind(itemView);
        }
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        getMenuInflater().inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.log_out) {
            auth.signOut();
            finish();
            startActivity(new Intent(getBaseContext(), NotesActivity.class));

            return true;
        }
        return false;

    }

    @Override
    protected void onStart() {
        super.onStart();
        notesAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (notesAdapter != null) {
            notesAdapter.stopListening();
        }
    }

    private int getRandomColor() {
        ArrayList<Integer> randomColor = new ArrayList<>();
        randomColor.add(R.color.color1);
        randomColor.add(R.color.color2);
        randomColor.add(R.color.color3);
        randomColor.add(R.color.color4);
        randomColor.add(R.color.color5);
        randomColor.add(R.color.light_green);
        randomColor.add(R.color.green);
        randomColor.add(R.color.light_grey);
        randomColor.add(R.color.pink);
        randomColor.add(R.color.sky_blue);
        Random random = new Random();
        int number = random.nextInt(randomColor.size());
        return randomColor.get(number);
    }
}