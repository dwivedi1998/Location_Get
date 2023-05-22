package com.example.location_get;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.maps.model.LatLng;

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
import android.widget.Button;
import android.widget.Toast;
import com.google.maps.android.SphericalUtil;

import androidx.appcompat.app.AlertDialog;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;


import com.example.location_get.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Double lat=28.532659863371958;
    Double lon=77.27565374295638;
    private LatLng mPickupLocation = new LatLng( lat,lon);
    private LatLng mDropoffLocation;
    LatLng latLng1;
    double latitude;
    Button next;
    double longitude;
    LatLng latLng2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setdata();
        check();
        getLocation();
        latLng1 = new LatLng(lat,lon);


        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity.this,MainActivity2.class);
                startActivity(i);
            }
        });


// Calculate the distance between the two LatLng points in meters
      //  double distance = SphericalUtil.computeDistanceBetween(latLng1, latLng2);

// Check if the distance is within a 200 meter range
       /* if (distance <= 200) {
            // Distance is within 200 meters
            Toast.makeText(this, "Under The Line", Toast.LENGTH_SHORT).show();
        } else {
            // Distance is greater than 200 meters
            Toast.makeText(this, "Not Under The Line", Toast.LENGTH_SHORT).show();
        }*/
    }

    private void setdata() {
        binding.textView1.setText(lat.toString());
        binding.textView2.setText(lon.toString());
    }

    private void check() {
        binding.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            1);
                } else {
                    // Get current location
                    getLocation();
                    binding.textView3.setText(String.valueOf(latitude));
                    binding.textView4.setText(String.valueOf(longitude));
                    try {
                        double distance = SphericalUtil.computeDistanceBetween(mPickupLocation, mDropoffLocation);
                        String distanceText  = String.format("%.1f km", distance / 500.0);
                        Toast.makeText(MainActivity.this, "Distance: " + distance, Toast.LENGTH_SHORT).show();
                        if(distance<=500){
                             Toast.makeText(MainActivity.this, "You are in : "+distance +" meter", Toast.LENGTH_SHORT).show();



                            new android.app.AlertDialog.Builder(MainActivity.this).setMessage("you are in range!").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
//
                        }
                        else{
                            new android.app.AlertDialog.Builder(MainActivity.this).setMessage("out of range!").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

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
                    Toast.makeText(MainActivity.this, "Latitude: " + latitude + "\nLongitude: " + longitude, Toast.LENGTH_SHORT).show();
                    binding.textView3.setText(String.valueOf(latitude));
                    binding.textView4.setText(String.valueOf(longitude));
                    mDropoffLocation=new LatLng(latitude,longitude);
                }
            }, Looper.getMainLooper());
        }

    }


}