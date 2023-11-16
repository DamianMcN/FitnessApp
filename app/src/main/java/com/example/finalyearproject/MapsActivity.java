package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.finalyearproject.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final DecimalFormat df = new DecimalFormat("00.00");

    GoogleMap mMap;
    ActivityMapsBinding binding;
    public static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    Button startRun, pauseRun, resumeRun, endRun, getRunResults;
    TextView runDistance, runTime, timerText, distanceTitle, timeTitle, runName;
    EditText edtRunName;
    boolean timerStarted = false;
    Timer timer;
    TimerTask timerTask;
    String saveRunName, outdoorRunTime, outdoorRunDistance, username, totalDistance;
    LatLng startPoint, endPoint;
    float distance;
    Double startLat, startLon, endLat, endLon, time = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // get permission to access location details
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        // initialise variables
        distanceTitle = findViewById(R.id.txtViewRunDistanceTxt);
        timeTitle = findViewById(R.id.txtViewRunTime);
        runTime = findViewById(R.id.textView_RunTime);
        runDistance = findViewById(R.id.textView_RunDistance);
        timerText = findViewById(R.id.txtViewTimerText);
        runName = findViewById(R.id.txtViewRunName);
        edtRunName = findViewById(R.id.editTxtRunName);
        startRun = findViewById(R.id.btn_goForRun);
        endRun = findViewById(R.id.btn_endYourRun);

        // make certain variables invisible when activity first run
        runName.setVisibility(View.INVISIBLE);
        edtRunName.setVisibility(View.INVISIBLE);
        distanceTitle.setVisibility(View.INVISIBLE);
        timeTitle.setVisibility(View.INVISIBLE);
        runTime.setVisibility(View.INVISIBLE);
        runDistance.setVisibility(View.INVISIBLE);
        endRun.setVisibility(View.INVISIBLE);

        // when pause button is pushed calls the pause timer method
        pauseRun = findViewById(R.id.btn_PauseRun);
        pauseRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTapped();
            }
        });

        // when resume button is clicked calls the start timer method
        resumeRun = findViewById(R.id.btn_resumeRun);
        resumeRun.setVisibility(View.INVISIBLE);
        resumeRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTapped();
            }
        });

        // start new timer
        timer = new Timer();

        //when get results button clicked the get run distance method is called and some variables become visible
        getRunResults = findViewById(R.id.btn_RunResults);
        getRunResults.setVisibility(View.INVISIBLE);
        getRunResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDistanceRun();
                distanceTitle.setVisibility(View.VISIBLE);
                timeTitle.setVisibility(View.VISIBLE);
                runTime.setVisibility(View.VISIBLE);
                runTime.setText(timerText.getText().toString());
                runDistance.setVisibility(View.VISIBLE);
                runName.setVisibility(View.VISIBLE);
                edtRunName.setVisibility(View.VISIBLE);
                drawLine();

                // change button name to save run
                ((Button) findViewById(R.id.btn_RunResults)).setText("Save Run");
                // when clicked and a run name entered store the data to the database
                ((Button) findViewById(R.id.btn_RunResults)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveRunName = edtRunName.getText().toString();
                        if (TextUtils.isEmpty(saveRunName)) {
                            edtRunName.setError("Please enter a run name");
                        } else {
                            outdoorRunTime = runTime.getText().toString();
                            outdoorRunDistance = runDistance.getText().toString();
                            username = ParseUser.getCurrentUser().getUsername();
                            addToDatabase(outdoorRunTime, outdoorRunDistance, username, saveRunName);
                        }

                    }
                });
            }
        });

    }

    public void getLocationStart() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(MapsActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(MapsActivity.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            startLat = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            startLon = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.map);
                            mapFragment.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(GoogleMap googleMap) {

                                    startPoint = new LatLng(startLat, startLon);

                                    // add marker based on the start latLng
                                    googleMap.addMarker(new MarkerOptions().position(startPoint)
                                            .title("Start Point").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                                    // zoom the map to the currentUserLocation
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startPoint, 15));
                                }
                            });
                        }
                        }
                    }, Looper.getMainLooper());
    }

    public void getLocationEnd(){
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(MapsActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(MapsActivity.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            endLat = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            endLon = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.map);
                            mapFragment.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(GoogleMap googleMap) {

                                    endPoint = new LatLng(endLat, endLon);

                                    // add marker based on the start latLng
                                    googleMap.addMarker(new MarkerOptions().position(endPoint)
                                            .title("End Point").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                                    // zoom the map to the currentUserLocation
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(endPoint, 17));
                                }
                            });
                        }
                    }
                }, Looper.getMainLooper());
    }

    // method to ensure we get permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION && grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLocationStart();
                getLocationEnd();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // method gets the distance from start point and end point
    public void getDistanceRun(){

        Location startLocation = new Location("Start Point");
        startLocation.setLatitude(startLat);
        startLocation.setLongitude(startLon);

        Location endLocation = new Location("End Point");
        endLocation.setLatitude(endLat);
        endLocation.setLongitude(endLon);

        // distance to returns meters to get km divide by 1000
        distance = startLocation.distanceTo(endLocation) / 1000;

        // format result to 2 decimal places
        totalDistance = df.format(distance);

        // display distance
        runDistance.setText(totalDistance + " km");
    }

    // method draws a line from start point to end point
    public void drawLine(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.addPolyline((new PolylineOptions()).add(startPoint, endPoint).width(5).color(Color.GREEN).geodesic(true));
            }
        });
    }

    // method to start/resume timer
    public void startTapped(){
        if (!timerStarted){
            timerStarted = true;
            startTimer();
        }
    }

    // method to pause timer
    public void pauseTapped(){
        if (timerStarted){
            timerStarted = false;
            timerTask.cancel();
        }
    }

    // starts timer adds 1 to timerText every 1000ms
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

    // method formats the timer
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

    // method to add data to database
    private void addToDatabase(String outdoorRunTime, String outdoorRunDistance, String username, String runName){
        // create a new object in the database
        ParseObject outdoorRuns = new ParseObject("OutdoorRuns");

        // populate database
        outdoorRuns.put("username", username);
        outdoorRuns.put("outdoorRunTime", outdoorRunTime);
        outdoorRuns.put("outdoorRunDistance", outdoorRunDistance);
        outdoorRuns.put("runName", runName);

        outdoorRuns.saveInBackground(new SaveCallback() {
            @Override
            // if no exceptions add data to database and display toast
            public void done(ParseException e) {
                if (e == null){
                    Toast.makeText(getApplicationContext(), "Outdoor Run added to Database", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    // method to control the map
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // when start button pressed call the getStartLocation method and start timer
        startRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MapsActivity.this, new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                }
                else {
                    getLocationStart();
                }
                startTapped();
                startRun.setVisibility(View.INVISIBLE);
                resumeRun.setVisibility(View.VISIBLE);
                endRun.setVisibility(View.VISIBLE);
            }
        });

        // when end button pressed call the getEndLocation method and stop the timer
        endRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MapsActivity.this, new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                }
                else {
                    getLocationEnd();
                }
                pauseTapped();
                getRunResults.setVisibility(View.VISIBLE);
                resumeRun.setVisibility(View.INVISIBLE);
                pauseRun.setVisibility(View.INVISIBLE);
                endRun.setVisibility(View.INVISIBLE);
                timerText.setVisibility(View.INVISIBLE);
            }
        });
    }
}


// this method gets the starting position of the user when start run button is pressed
//    public void getStartLocation() {
//
//        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{
//                    android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//        } else {
//            // getting last know user's location
//            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//
//            if (location != null) {
//                // set start latLng
//                startLat = location.getLatitude();
//                startLon = location.getLongitude();
//
//                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                        .findFragmentById(R.id.map);
//                mapFragment.getMapAsync(new OnMapReadyCallback() {
//                    @Override
//                    public void onMapReady(GoogleMap googleMap) {
//
//                        startPoint = new LatLng(startLat, startLon);
//
//                        // add marker based on the start latLng
//                        googleMap.addMarker(new MarkerOptions().position(startPoint)
//                                .title("Start Point").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
//
//                        // zoom the map to the currentUserLocation
//                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startPoint, 15));
//                    }
//                });
//            } else {
//                Toast.makeText(this, "Unable to find location", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

// this method gets the ending position of the user when end run button is pressed
//    public void getEndLocation() {
//
//        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{
//                    android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//        } else {
//            // getting last know user's location
//            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//
//            if (location != null) {
//                // set end latLng
//                endLat = location.getLatitude();
//                endLon = location.getLongitude();
//
//                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                        .findFragmentById(R.id.map);
//                mapFragment.getMapAsync(new OnMapReadyCallback() {
//                    @Override
//                    public void onMapReady(GoogleMap googleMap) {
//
//                        endPoint = new LatLng(endLat, endLon);
//
//                        // add marker based on the end latLng
//                        googleMap.addMarker(new MarkerOptions().position(endPoint)
//                                .title("End Point").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//
//                        // zoom the map to the currentUserLocation
//                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(endPoint, 17));
//                    }
//                });
//            } else {
//                Toast.makeText(this, "Unable to find location", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }