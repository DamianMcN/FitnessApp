package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class RunningPage extends AppCompatActivity {

    private static final DecimalFormat df = new DecimalFormat("00.00");

    boolean timerStarted = false;
    Timer timer;
    TimerTask timerTask;
    TextView stepCounter, runTime, distanceRan, timerText;
    EditText edtSaveRun;
    Double previousMagnitude = 0.0, distance, result, time = 0.0;
    Integer stepCount = 0;
    Button startRun, pauseRun, endRun, saveRun;
    String finalDistance, saveRunName, username, indoorRunTime, indoorRunDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_page);

        edtSaveRun = findViewById(R.id.editTxt_Save_Run);
        edtSaveRun.setVisibility(View.INVISIBLE);
        saveRun = findViewById(R.id.btn_Save_Run);
        saveRun.setVisibility(View.INVISIBLE);
        runTime = findViewById(R.id.runTime);
        distanceRan = findViewById(R.id.distance);
        startRun = findViewById(R.id.btn_Start_Run);
        pauseRun = findViewById(R.id.btn_Pause_Run);
        endRun = findViewById(R.id.btn_End_Run);
        endRun.setVisibility(View.INVISIBLE);
        timerText = findViewById(R.id.txtViewTimerText3);

        // setup step counter using sensors
        stepCounter = findViewById(R.id.stepCounter);
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // setting the xyz to detect acceleration and add plus 1 to counter if the magnitude is over 6
        SensorEventListener stepDetector = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event != null){
                    float x_acceleration = event.values[0];
                    float y_acceleration = event.values[1];
                    float z_acceleration = event.values[2];

                    double magnitude = Math.sqrt(x_acceleration * x_acceleration
                            + y_acceleration * y_acceleration
                            + z_acceleration * z_acceleration);

                    double newMagnitude = magnitude - previousMagnitude;
                    previousMagnitude = magnitude;

                    if (newMagnitude > 6){
                        stepCount++;
                    }
                    stepCounter.setText(stepCount.toString());
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }

        };

        sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        timer = new Timer();

        // starts timer
        startRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endRun.setVisibility(View.VISIBLE);
                startTapped();
                ((Button)findViewById(R.id.btn_Start_Run)).setText("Resume Run");
                ((Button)findViewById(R.id.btn_Start_Run)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startTapped();
                    }
                });
            }
        });

        // pauses timer
        pauseRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTapped();
            }
        });

        endRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTapped();
                timerText.setVisibility(View.INVISIBLE);
                startRun.setVisibility(View.INVISIBLE);
                pauseRun.setVisibility(View.INVISIBLE);
                endRun.setVisibility(View.INVISIBLE);
                edtSaveRun.setVisibility(View.VISIBLE);
                saveRun.setVisibility(View.VISIBLE);

                String runDistance = stepCounter.getText().toString();

                distance = Double.parseDouble(runDistance);

                // calculate distance travelled based on number of steps taken
                result = (distance * 75) / 100000;

                finalDistance = df.format(result);

                distanceRan.setText(finalDistance + " Km");

                runTime.setText(timerText.getText().toString());
            }
        });

        saveRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveRunName = edtSaveRun.getText().toString();
                if (TextUtils.isEmpty(saveRunName)){
                    edtSaveRun.setError("Please enter a run name");
                } else {
                    indoorRunTime = runTime.getText().toString();
                    indoorRunDistance = distanceRan.getText().toString();
                    username = ParseUser.getCurrentUser().getUsername();
                    addToDatabase(indoorRunTime, indoorRunDistance, username, saveRunName);
                }

            }
        });
    }

    public void startTapped(){
        if (!timerStarted){
            timerStarted = true;
            startTimer();
        }
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

    // save indoor run data to database
    private void addToDatabase(String indoorRunTime, String indoorRunDistance, String username, String runName){
        // create a new object in database called indoorRuns
        ParseObject indoorRuns = new ParseObject("IndoorRuns");

        // populate object
        indoorRuns.put("username", username);
        indoorRuns.put("indoorRunTime", indoorRunTime);
        indoorRuns.put("indoorRunDistance", indoorRunDistance);
        indoorRuns.put("runName", runName);

        // save data to database and display toast if successful
        indoorRuns.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    Toast.makeText(getApplicationContext(), "Indoor Run added to Database", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}