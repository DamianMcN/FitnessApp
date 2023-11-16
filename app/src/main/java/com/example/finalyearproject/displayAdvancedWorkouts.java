package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class displayAdvancedWorkouts extends AppCompatActivity {

    ResultsAdaptor3 adaptor;
    RecyclerView advancedResultsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_advanced_workouts);

        advancedResultsList = findViewById(R.id.advancedResultsList);

        // get current user
        String currentUser = ParseUser.getCurrentUser().getUsername();

        // query the advancedWorkout object and if current user equals username in object display the objects in the recyclerview
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("AdvancedWorkouts");
        query.whereEqualTo("username", currentUser);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null){
                    System.out.println("Size " + objects.size());
                    initData(objects);
                } else {
                    Log.d("ParseQuery", e.getMessage());
                }
            }
        });
    }

    // method to set the adaptor layout
    public void initData(List<ParseObject> objects){
        adaptor = new ResultsAdaptor3(this, objects);
        advancedResultsList.setLayoutManager(new LinearLayoutManager(this));
        advancedResultsList.setAdapter(adaptor);
    }
}