package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GoForRunMenu extends AppCompatActivity {

    Button outdoorRun, indoorRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_for_run_menu);

        outdoorRun = findViewById(R.id.btn_outdoorRun);
        outdoorRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(GoForRunMenu.this, MapsActivity.class);
                Intent intent = new Intent(GoForRunMenu.this, OutdoorRun.class);
                startActivity(intent);
            }
        });

        indoorRun = findViewById(R.id.btn_indoorRun);
        indoorRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoForRunMenu.this, RunningPage.class);
                startActivity(intent);
            }
        });
    }
}