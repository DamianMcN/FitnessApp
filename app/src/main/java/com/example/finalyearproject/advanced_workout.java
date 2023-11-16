package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class advanced_workout extends AppCompatActivity {

    TextView timerText, activityText;
    EditText workoutName;
    Button btnStartTimer, btnPauseTimer, btnNextWorkout;
    boolean timerStarted = false;
    Timer timer;
    TimerTask timerTask;
    Double time = 0.0;
    ImageView imageView;
    String fileName, advancedWorkouts, username, saveWorkoutName;
    Bitmap bitmap;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_workout);

        timerText = findViewById(R.id.txt_Timer3);
        activityText = findViewById(R.id.txtView_CurrentActivity3);
        btnStartTimer = findViewById(R.id.btn_Start3);
        btnPauseTimer = findViewById(R.id.btn_Pause3);
        btnNextWorkout = findViewById(R.id.btn_NextActivity3);
        btnNextWorkout.setVisibility(View.INVISIBLE);
        imageView = findViewById(R.id.imageView3);
        workoutName = findViewById(R.id.editTxtSaveWorkoutName3);
        workoutName.setVisibility(View.INVISIBLE);

        String pushUps = "Perform 50 pushups then continue!";
        String sitUps = "Perform 50 situps then continue!";
        String jumpingJacks = "Perform 50 jumping jacks then continue!";
        String squats = "Perform 50 squats then continue!";

        activityText.setText(pushUps);

        timer = new Timer();

        pushUps();

        btnStartTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTapped();
                btnNextWorkout.setVisibility(View.VISIBLE);
                ((Button)findViewById(R.id.btn_Start3)).setText("Resume");
            }
        });

        btnPauseTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTapped();
            }
        });

        btnNextWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityText.setText(sitUps);
                sitUps();

                ((Button)findViewById(R.id.btn_NextActivity3)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activityText.setText(jumpingJacks);
                        jumpingJacks();

                        btnNextWorkout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                activityText.setText(squats);
                                squats();
                                ((Button)findViewById(R.id.btn_NextActivity3)).setText("End workout");
                                ((Button)findViewById(R.id.btn_NextActivity3)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        workoutName.setVisibility(View.VISIBLE);
                                        btnStartTimer.setVisibility(View.INVISIBLE);
                                        btnPauseTimer.setVisibility(View.INVISIBLE);
                                        pauseTapped();
                                        ((Button)findViewById(R.id.btn_NextActivity3)).setText("Save Workout");
                                        ((Button)findViewById(R.id.btn_NextActivity3)).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                saveWorkoutName = workoutName.getText().toString();
                                                if(TextUtils.isEmpty(saveWorkoutName)){
                                                    workoutName.setError("Please enter a workout name");
                                                } else {
                                                    advancedWorkouts = timerText.getText().toString();
                                                    username = ParseUser.getCurrentUser().getUsername();
                                                    updateDataToDatabase(advancedWorkouts, username, saveWorkoutName);
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    public void pushUps(){
        Random rnd_img = new Random();
        int imageId = rnd_img.nextInt(2);
        String imgName = "workout" + imageId + ".jpg";

        System.out.println("imageId = " + imageId);

        fileName = "/storage/emulated/0/my_images/pushups/" + imgName;
        System.out.println("fileName = " + fileName);
        bitmap = BitmapFactory.decodeFile(fileName);
        imageView.setImageBitmap(bitmap);
        countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {}
            @Override
            public void onFinish() {} //pushUps();
        }.start();
    }

    public void sitUps(){
        Random rnd_img = new Random();
        int imageId = rnd_img.nextInt(2);
        String imgName = "workout" + imageId + ".jpg";

        System.out.println("imageId = " + imageId);

        fileName = "/storage/emulated/0/my_images/situps/" + imgName;
        System.out.println("fileName = " + fileName);
        bitmap = BitmapFactory.decodeFile(fileName);
        imageView.setImageBitmap(bitmap);
        countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {}
            @Override
            public void onFinish() {} //sitUps();
        }.start();
    }

    public void jumpingJacks(){
        Random rnd_img = new Random();
        int imageId = rnd_img.nextInt(2);
        String imgName = "workout" + imageId + ".jpg";

        System.out.println("imageId = " + imageId);

        fileName = "/storage/emulated/0/my_images/jumpingjacks/" + imgName;
        System.out.println("fileName = " + fileName);
        bitmap = BitmapFactory.decodeFile(fileName);
        imageView.setImageBitmap(bitmap);
        countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {}
            @Override
            public void onFinish() {} //jumpingJacks();
        }.start();
    }

    public void squats(){
        Random rnd_img = new Random();
        int imageId = rnd_img.nextInt(2);
        String imgName = "workout" + imageId + ".jpg";

        System.out.println("imageId = " + imageId);

        fileName = "/storage/emulated/0/my_images/squats/" + imgName;
        System.out.println("fileName = " + fileName);
        bitmap = BitmapFactory.decodeFile(fileName);
        imageView.setImageBitmap(bitmap);
        countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {}
            @Override
            public void onFinish() {} //squats();
        }.start();
    }

    public void startTapped(){
        if (!timerStarted){
            timerStarted = true;
            startTimer();
        } //else {
        //timerStarted = false;
        //timerTask.cancel();
        //}
    }

    public void pauseTapped(){
        if (timerStarted){
            timerStarted = false;
            timerTask.cancel();
        }
    }

    public void startTimer(){
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        timerText.setText(getTimerText());
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    public String getTimerText(){
        int roundTime = (int) Math.round(time);

        int seconds = ((roundTime % 86400) % 3600) % 60;
        int minutes = ((roundTime % 86400) % 3600) / 60;
        int hours = ((roundTime % 86400) / 3600);

        return formatTime(seconds, minutes, hours);
    }

    public String formatTime(int seconds, int minutes, int hours) {
        return  String.format("%02d", hours) + " : " + String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
    }

    private void updateDataToDatabase(String advancedWorkouts, String username, String workoutName){
        ParseObject advancedWorkout = new ParseObject("AdvancedWorkouts");

        advancedWorkout.put("username", username);
        advancedWorkout.put("advancedWorkouts", advancedWorkouts);
        advancedWorkout.put("workoutName", workoutName);

        advancedWorkout.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    Toast.makeText(getApplicationContext(), "Workout added to Database", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}