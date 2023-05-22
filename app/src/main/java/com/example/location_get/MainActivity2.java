package com.example.location_get;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.location_get.databinding.ActivityMain2Binding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

public class MainActivity2 extends AppCompatActivity {
ActivityMain2Binding main2Binding;
    private LatLng mDropoffLocation1;
    private LatLng mpickupLocation;
    LatLng latLng1;
    double latitude;
    double longitude;
    LatLng latLng2;
    String lat1,lon2;
    Double latr,longr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main2Binding=ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(main2Binding.getRoot());
        getLocation();
        mDropoffLocation1=new LatLng(latitude,longitude);
        submit();
    }

    private void submit() {
        main2Binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latr= Double.valueOf(main2Binding.edt1.getText().toString().trim());
                longr= Double.valueOf(main2Binding.edt2.getText().toString().trim());
mpickupLocation=new LatLng(latr,longr);
                try {
                    double distance = SphericalUtil.computeDistanceBetween(mDropoffLocation1, mpickupLocation);
                    String distanceText  = String.format("%.1f km", distance / 500.0);
                    Toast.makeText(MainActivity2.this, "Distance: " + distance, Toast.LENGTH_SHORT).show();
                    if(distance<=500){
                        Toast.makeText(MainActivity2.this, "You are in : "+distance +" meter", Toast.LENGTH_SHORT).show();



                        new android.app.AlertDialog.Builder(MainActivity2.this).setMessage("you are in range!").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
//
                    }
                    else{
                        new android.app.AlertDialog.Builder(MainActivity2.this).setMessage("out of range!").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();



                    }
                }
                catch (Exception e)
                {
                    Log.i("#error", String.valueOf(e));
                }

            }
        });
    }

    private void getLocation() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Check if GPS is enabled
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Show alert dialog to enable GPS
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("GPS Disabled");
            builder.setMessage("Please enable GPS to use location services");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Open location settings
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Cancel", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            // Request location updates
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(0);
            locationRequest.setFastestInterval(0);
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    // Get current location
                    Location location = locationResult.getLastLocation();
                    latitude= location.getLatitude();
                    longitude = location.getLongitude();
                    latLng2 = new LatLng(latitude, longitude);
                    Toast.makeText(MainActivity2.this, "Latitude: " + latitude + "\nLongitude: " + longitude, Toast.LENGTH_SHORT).show();
                    mDropoffLocation1=new LatLng(latitude,longitude);
                }
            }, Looper.getMainLooper());
        }

    }
}