package com.example.lenovo.healthmax;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class historyDisease extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private RecyclerView mNotesList;
    private GridLayoutManager gridLayoutManager;
    private DatabaseReference fNotesDatabase;
    private FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_disease);

        Toolbar toolbar = findViewById(R.id.new_note_toolbar);
        setSupportActionBar(toolbar);

        mNotesList = findViewById(R.id.main_notes_list);

       gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);

       mNotesList.setHasFixedSize(true);
       mNotesList.setLayoutManager(gridLayoutManager);

       firebaseAuth = FirebaseAuth.getInstance();
       if(firebaseAuth.getCurrentUser() != null){
           fNotesDatabase = FirebaseDatabase.getInstance().getReference().child("Notes").child(firebaseAuth.getCurrentUser().getUid());
       }

        updateUI();
        fetch();
    }

    public void fetch(){
        Query query = FirebaseDatabase.getInstance().getReference().child("Notes").child(firebaseAuth.getCurrentUser().getUid());

        FirebaseRecyclerOptions<noteModel> options = new FirebaseRecyclerOptions.Builder<noteModel>().setQuery(query, new SnapshotParser<noteModel>()
        {
            @NonNull
            @Override
            public noteModel parseSnapshot(@NonNull DataSnapshot snapshot){
                Log.d("Note model", snapshot.toString());
                noteModel model = new noteModel(snapshot.child("judul").getValue().toString(),
                        snapshot.child("timestamp").getValue().toString());
                model.id = snapshot.getKey();
                return model;
            }

        }).build();

            adapter = new FirebaseRecyclerAdapter<noteModel, NoteViewHolder>(options){
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull noteModel model) {
                holder.setNoteTitle(model.getNoteTitle());
                holder.setNoteTime(model.getNoteTime());
                holder.id = model.id;
            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.single_note_layout, parent,false);

                return new NoteViewHolder(view);
            }
        };

            mNotesList.setAdapter(adapter);
    }


    private void updateUI(){
        if(firebaseAuth.getCurrentUser()!=null){
            Log.i("historyDisease","firebaseAuth!=null");
        }else{
            Intent startIntent = new Intent(historyDisease.this, ProfileActivity.class);
            startActivity(startIntent);
            finish();
            Log.i("historyDisease", "firebaseAuth == null");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.main_new_note_btn :
                Intent newIntent = new Intent(historyDisease.this, formHistory.class);
                startActivity(newIntent);
                break;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}



