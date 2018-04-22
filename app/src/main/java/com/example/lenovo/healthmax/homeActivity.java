package com.example.lenovo.healthmax;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class homeActivity extends AppCompatActivity {

     ImageView profile;
     ImageView medication;
     ImageView history;
     ImageView settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

         profile = (ImageView) findViewById(R.id.profile);
         medication = (ImageView) findViewById(R.id.medication);
         history = (ImageView) findViewById(R.id.history);
         settings = (ImageView) findViewById(R.id.settings);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });

        medication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MedicationActivity.class));
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), historyDisease.class));
            }
        });



    }
}
