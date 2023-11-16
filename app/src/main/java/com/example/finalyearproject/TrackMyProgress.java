package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TrackMyProgress extends AppCompatActivity {

    Button beginResults, interResults, advancedResults, outdoorResults, indoorResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_my_progress);

        beginResults = findViewById(R.id.btnBeginnerResults);
        interResults = findViewById(R.id.btnIntermediateResults);
        advancedResults = findViewById(R.id.btnAdvancedResults);
        outdoorResults = findViewById(R.id.btnOutdoorRunResults);
        indoorResults= findViewById(R.id.btnIndoorRunResults);

        beginResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrackMyProgress.this, displayBeginWorkouts.class);
                startActivity(intent);
            }
        });

        interResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrackMyProgress.this, displayInterWorkouts.class);
                startActivity(intent);
            }
        });

        advancedResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrackMyProgress.this, displayAdvancedWorkouts.class);
                startActivity(intent);
            }
        });

        outdoorResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrackMyProgress.this, displayOutdoorRuns.class);
                startActivity(intent);
            }
        });

        indoorResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrackMyProgress.this, displayIndoorRuns.class);
                startActivity(intent);
            }
        });
    }
}