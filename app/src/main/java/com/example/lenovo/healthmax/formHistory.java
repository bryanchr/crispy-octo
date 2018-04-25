package com.example.lenovo.healthmax;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;


import java.util.HashMap;
import java.util.Map;

public class formHistory extends AppCompatActivity {


    private Button buttonAdd;
    private EditText namaDokter,judulCatatan, catatanPemeriksaan;
    private String noteID;
    private DatabaseReference noteDatabase;
    private boolean isExist;
    private Toolbar mToolbar;

    private FirebaseAuth firebaseAuth;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.new_note_menu, menu);
        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_history);

        try {
            noteID = getIntent().getStringExtra("noteId");
            Log.d("FORM HISTORY", noteID);

//            Toast.makeText(this, noteID, Toast.LENGTH_SHORT).show();

            if (!noteID.trim().equals("")) {
                isExist = true;
            } else {
                isExist = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        namaDokter = (EditText) findViewById(R.id.namaDokter);
        judulCatatan = (EditText) findViewById(R.id.judulCatatan);
        catatanPemeriksaan = (EditText) findViewById(R.id.catatanPemeriksaan);
        buttonAdd = (Button)findViewById(R.id.buttonAdd);
        mToolbar = (Toolbar)findViewById(R.id.new_note_toolbar);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        noteDatabase = FirebaseDatabase.getInstance().getReference().child("Notes").child(firebaseAuth.getCurrentUser().getUid());

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dokter = namaDokter.getText().toString().trim();
                String judul = judulCatatan.getText().toString().trim();
                String catatan = catatanPemeriksaan.getText().toString().trim();

                if(!TextUtils.isEmpty(dokter) && !TextUtils.isEmpty(catatan)&& !TextUtils.isEmpty(judul)){
                    createNote(dokter, judul, catatan);
                }
                else{
                    Snackbar.make(v," Fill empty fields", Snackbar.LENGTH_SHORT ).show();
                }
            }
        });

        putData();
    }

    private void createNote(String dokter,String judul, String catatan){

        if(firebaseAuth.getCurrentUser() != null){

            if(isExist){
                //update
                Map updateMap = new HashMap();
                updateMap.put("dokter",namaDokter.getText().toString().trim());
                updateMap.put("judul",judulCatatan.getText().toString().trim());
                updateMap.put("catatan",catatanPemeriksaan.getText().toString().trim());
                updateMap.put("timestamp", ServerValue.TIMESTAMP);

                noteDatabase.child(noteID).updateChildren(updateMap);

                Toast.makeText(this,"Note telah diperbaharui",Toast.LENGTH_SHORT).show();
            }else {
                //create note
                final DatabaseReference newNote = noteDatabase.push();

                final Map noteMap = new HashMap();
                noteMap.put("dokter", dokter);
                noteMap.put("judul", judul);
                noteMap.put("catatan", catatan);
                noteMap.put("timestamp", ServerValue.TIMESTAMP);

                Thread mainThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        newNote.setValue(noteMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(formHistory.this, "Catatan telah tersimpan", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(formHistory.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                mainThread.start();
            }

        }else{
            Toast.makeText(this, " Users is not Signed In",Toast.LENGTH_SHORT).show();
        }

    }

    private void putData() {

        if (isExist) {
            noteDatabase.child(noteID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("dokter") && dataSnapshot.hasChild("judul") && dataSnapshot.hasChild("catatan")) {
                        String dokter = dataSnapshot.child("dokter").getValue().toString();
                        String judul = dataSnapshot.child("judul").getValue().toString();
                        String catatan = dataSnapshot.child("catatan").getValue().toString();

                        namaDokter.setText(dokter);
                        judulCatatan.setText(judul);
                        catatanPemeriksaan.setText(catatan);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.new_note_delete_btn:
                if (isExist) {
                    deleteNote();
                } else {
                    Toast.makeText(this, "Nothing to delete", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        return true;
    }

    private void deleteNote() {

        noteDatabase.child(noteID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(formHistory.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                    noteID = "no";
                    finish();
                } else {
                    Log.e("NewNoteActivity", task.getException().toString());
                    Toast.makeText(formHistory.this, "ERROR: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
