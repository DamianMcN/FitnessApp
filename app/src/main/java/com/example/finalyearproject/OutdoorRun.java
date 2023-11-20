package com.example.finalyearproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class OutdoorRun extends AppCompatActivity {

    Button startButton, stopButton;
    GoogleMap googleMap;
    List<LatLng> runPoints = new ArrayList<>();
    boolean isRunning = false;
    boolean isStartMarkerAdded = false;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;


    private static final int LOCATION_PERMISSION_REQUEST_CODE  = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outdoor_run);

        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRun();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRun();
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);

        if (mapFragment != null){
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull GoogleMap map) {
                    googleMap = map;
                    enableMyLocation();
                    zoomToCurrentLocation();
                }
            });
        }

        createLocationRequest();
        createLocationCallBack();
    }

    private void enableMyLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            googleMap.setMyLocationEnabled(true);
            startLocationUpdates();
        }else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && isRunning) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createLocationRequest(){
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void createLocationCallBack(){
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult){
                super.onLocationResult(locationResult);

                if (isRunning){
                    Location location = locationResult.getLastLocation();
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                    if (!isStartMarkerAdded){
                        googleMap.addMarker(new MarkerOptions().position(latLng).title("Start")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        isStartMarkerAdded = true;
                    }
                    runPoints.add(latLng);

                    //googleMap.addMarker(new MarkerOptions().position(latLng));
                }
            }
        };
    }

    private void startRun(){
        runPoints.clear();
        isRunning = true;

        startButton.setVisibility(View.INVISIBLE);

        zoomToCurrentLocation();

        startLocationUpdates();

        updateCamera();
    }

    private void stopRun(){
        isRunning = false;
        stopLocationUpdates();

        if (googleMap != null){
            if (runPoints.size() > 0){
                LatLng stopPoint = runPoints.get(runPoints.size() - 1);
                googleMap.addMarker(new MarkerOptions().position(stopPoint).title("Stop")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.addAll(runPoints);
            googleMap.addPolyline(polylineOptions);
        }
        isStartMarkerAdded = false;
    }

    private void startLocationUpdates(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private void stopLocationUpdates(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void updateCamera(){
        if (googleMap != null && runPoints.size() > 0){
            LatLng lastPoint = runPoints.get(runPoints.size() - 1);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastPoint, 17f), 2000, null);
        }
    }

    private void zoomToCurrentLocation(){
        if (googleMap != null){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED){
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null){
                                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17f));
                                }
                            }
                        });
            }
        }
    }
}
