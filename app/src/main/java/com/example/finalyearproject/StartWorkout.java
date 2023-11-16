package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartWorkout extends AppCompatActivity {

    Button beginner, intermediate, advanced;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_workout);

        beginner = findViewById(R.id.btn_beginner);
        intermediate = findViewById(R.id.btn_intermediate);
        advanced = findViewById(R.id.btn_advanced);

        beginner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartWorkout.this, beginner_workout.class);
                startActivity(intent);
            }
        });

        intermediate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartWorkout.this, intermediate_workout.class);
                startActivity(intent);
            }
        });

        advanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartWorkout.this, advanced_workout.class);
                startActivity(intent);
            }
        });
    }
}