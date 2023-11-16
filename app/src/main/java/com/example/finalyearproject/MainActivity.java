package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    TextView signup;
    EditText usernameText, passwordText;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // brings user to signup page
        signup = findViewById(R.id.textView_signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });

        usernameText = findViewById(R.id.editText_username);
        passwordText = findViewById(R.id.editText_password);
        loginButton = findViewById(R.id.btn_signin);

        // logins the user in
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usrName = usernameText.getText().toString();
                String passWord = passwordText.getText().toString();
                usrLogin(usrName, passWord);
            }
        });
    }

    // if username exists in database log user in
    public void usrLogin(String u_name, String p_word){
        ParseUser.logInInBackground(u_name, p_word, (parseUser, e) ->{
            if (parseUser != null) {
                Toast.makeText(MainActivity.this, "Successful Login, Welcome back "
                + u_name + "!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, MainMenu.class);
                intent.putExtra("Name", u_name);
                startActivity(intent);
            } else {
                ParseUser.logOut();
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}