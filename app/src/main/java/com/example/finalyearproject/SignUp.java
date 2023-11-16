package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;

public class SignUp extends AppCompatActivity {

    EditText ageText, heightText, weightText, genderText, usernameText, passwordText, confirmPasswordText;
    Button regButton;
    String username, password, conf_password, age, gender, height, weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameText = findViewById(R.id.editText_ca_uname);
        passwordText = findViewById(R.id.editText_ca_password);
        confirmPasswordText = findViewById(R.id.editText_ca_cpassword);
        ageText = findViewById(R.id.editText_age);
        heightText = findViewById(R.id.editText_height);
        weightText = findViewById(R.id.editText_weight);
        genderText = findViewById(R.id.editText_gender);
        regButton = findViewById(R.id.button_ca);

        // when pressed and all fields are valid add user details to database
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameText.getText().toString();
                password = passwordText.getText().toString();
                conf_password = confirmPasswordText.getText().toString();
                gender = genderText.getText().toString();
                age = ageText.getText().toString();
                height = heightText.getText().toString();
                weight = weightText.getText().toString();

                // validation to make sure fields are correctly filled out returns error if not
                if (TextUtils.isEmpty(username)){
                    usernameText.setError("Please enter username");
                } else if (TextUtils.isEmpty(gender)){
                    genderText.setError("Please enter gender");
                } else if (TextUtils.isEmpty(age)){
                    ageText.setError("Please enter age");
                } else if (TextUtils.isEmpty(height)){
                    heightText.setError("Please enter height");
                } else if (TextUtils.isEmpty(weight)){
                    weightText.setError("Please enter weight");
                } else if (!password.equals(conf_password)){
                    passwordText.setError("Passwords must match");
                } else{
                    addDataToDatabase(username, password, gender, age, height, weight);
                }
            }
        });
    }

    // method to add user details to database and display toast if successful
    public void addDataToDatabase(String name, String password, String gender, String age, String height, String weight) {
        ParseUser user = new ParseUser();

        user.setUsername(name);
        user.setPassword(password);
        user.put("gender", gender);
        user.put("age", age);
        user.put("height", height);
        user.put("weight", weight);

        user.signUpInBackground(e ->{
                if (e == null){
                    Toast.makeText(SignUp.this, "Account Successfully Created",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUp.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    ParseUser.logOut();
                    Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
        });
    }

}