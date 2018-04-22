package com.example.lenovo.healthmax;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;


import java.util.HashMap;
import java.util.Map;

public class formHistory extends AppCompatActivity {


    private  Button buttonAdd;
    private EditText namaDokter,judulCatatan, catatanPemeriksaan;

    private DatabaseReference noteDatabase;

    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_history);

        namaDokter = (EditText) findViewById(R.id.namaDokter);
        judulCatatan = (EditText) findViewById(R.id.judulCatatan);
        catatanPemeriksaan = (EditText) findViewById(R.id.catatanPemeriksaan);
        buttonAdd = (Button)findViewById(R.id.buttonAdd);

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

    }

    private void createNote(String dokter,String judul, String catatan){

        if(firebaseAuth.getCurrentUser() != null){

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
                            if(task.isSuccessful()){
                                Toast.makeText(formHistory.this, "Catatan telah tersimpan", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(formHistory.this, "Error" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
            mainThread.start();


        }else{
            Toast.makeText(this, " Users is not Signed In",Toast.LENGTH_SHORT).show();
        }

    }

}
