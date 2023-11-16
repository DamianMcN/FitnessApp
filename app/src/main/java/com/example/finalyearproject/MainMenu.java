package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;

public class MainMenu extends AppCompatActivity {

    TextView txtName;
    Button myDetails, startWorkout, goForRun, leaderboards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);



        txtName = findViewById(R.id.textView_welcome);

        Intent intent = getIntent();
        String loginName = intent.getStringExtra("Name");
        txtName.setText("Welcome, " + loginName);

        myDetails = findViewById(R.id.btn_MyDetails);
        myDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, MyDetails.class);
                startActivity(intent);
            }
        });

        startWorkout = findViewById(R.id.btn_StartaWorkout);
        startWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, StartWorkout.class);
                startActivity(intent);
            }
        });

        goForRun = findViewById(R.id.btn_GoForaRun);
        goForRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, GoForRunMenu.class);
                startActivity(intent);
            }
        });

        leaderboards = findViewById(R.id.btn_TrackProgress);
        leaderboards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, TrackMyProgress.class);
                startActivity(intent);
            }
        });
    }

    public void LogOut(View view){
        Intent intent = new Intent(MainMenu.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

