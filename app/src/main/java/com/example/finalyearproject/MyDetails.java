package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class MyDetails extends AppCompatActivity {

    EditText edtUsername, edtGender, edtAge, edtHeight, edtWeight;
    Button btnUpdateDetails;
    String username, gender, age, height, weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_details);

        String currentUser = ParseUser.getCurrentUser().getUsername();

        edtUsername = findViewById(R.id.editTextUsername);
        edtGender = findViewById(R.id.editTextGender);
        edtAge = findViewById(R.id.editTextAge);
        edtHeight = findViewById(R.id.editTextHeight);
        edtWeight = findViewById(R.id.editTextWeight);
        btnUpdateDetails = findViewById(R.id.btn_UpdateDetails);

        // query to get the current user and display their details
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", currentUser);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        edtUsername.setText(objects.get(i).getString("username"));
                        edtGender.setText(objects.get(i).getString("gender"));
                        edtAge.setText(objects.get(i).getString("age"));
                        edtHeight.setText(objects.get(i).getString("height"));
                        edtWeight.setText(objects.get(i).getString("weight"));
                    }
                } else {

                }
            }
        });

        // when pressed and all fields valid saves to database
        btnUpdateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = edtUsername.getText().toString();
                gender = edtGender.getText().toString();
                age = edtAge.getText().toString();
                height = edtHeight.getText().toString();
                weight = edtWeight.getText().toString();

                // validation to make sure all fields are correctly filled out
                if(username.matches("")){
                    edtUsername.requestFocus();
                    edtUsername.setError("Please enter a username");
                } else if (gender.matches("")){
                    edtGender.requestFocus();
                    edtGender.setError("Please enter gender");
                }  else if (age.matches("")) {
                    edtAge.requestFocus();
                    edtAge.setError("Please enter age");
                } else if (height.matches("")) {
                    edtHeight.requestFocus();
                    edtHeight.setError("Please enter height");
                } else if (weight.matches("")) {
                    edtWeight.requestFocus();
                    edtWeight.setError("Please enter weight");
                } else {
                    updateDataToDatabase(username, gender, age, height, weight);
            }
            }
        });
    }

    // method to add new user data to database and display toast if successful
    private void updateDataToDatabase(String username, String gender, String age, String height, String weight){
        String currentUser = ParseUser.getCurrentUser().getUsername();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereMatches("username", currentUser);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {
                if (e == null){
                    object.put("username", username);
                    object.put("gender", gender);
                    object.put("age", age);
                    object.put("height", height);
                    object.put("weight", weight);
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null){
                                Toast.makeText(getApplicationContext(), "Details updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}