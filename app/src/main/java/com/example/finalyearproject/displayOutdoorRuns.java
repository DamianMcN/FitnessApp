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

public class displayOutdoorRuns extends AppCompatActivity {

    ResultsAdaptor4 adaptor;
    RecyclerView outdoorResultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_outdoor_runs);

        outdoorResultList = findViewById(R.id.outdoorRunResultsList);

        // get current user
        String currentUser = ParseUser.getCurrentUser().getUsername();

        // query the outDoor object and if current user equals username in object display the objects in the recyclerview
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("OutdoorRuns");
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
        adaptor = new ResultsAdaptor4(this, objects);
        outdoorResultList.setLayoutManager(new LinearLayoutManager(this));
        outdoorResultList.setAdapter(adaptor);
    }
}