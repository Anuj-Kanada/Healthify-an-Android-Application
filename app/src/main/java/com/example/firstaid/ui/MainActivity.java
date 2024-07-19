package com.example.firstaid.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import android.Manifest;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.firstaid.R;
import com.example.firstaid.dashboard;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private String address = "No GPS Found";
    private Button abutton ;
    private String questionDataListView;
    private Button dbutton ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().setTitle("Activity 1");
//        getSupportActionBar().s

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        Button startButton = findViewById(R.id.startButton);
        Button viewReportsButton = findViewById(R.id.viewReportsButton);
        abutton = findViewById(R.id.alButton);
        dbutton = findViewById(R.id.dbutton);



        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();

                Toast.makeText(MainActivity.this, "Successfully clicked..", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MainActivity.this, FirstAidActivity.class);
//                startActivity(intent);


            }

        });


        viewReportsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SavedReportsActivity.class);
                startActivity(intent);

            }
        });

        dbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, dashboard.class));

            }


        });





        abutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage("6351387338", null, "Emergency", null, null);
                Toast.makeText(MainActivity.this, "Successfully sent..", Toast.LENGTH_SHORT).show();
            }
        });


    }
    public void openNewActivity(){
                Intent intent = new Intent(this, dashboard.class);
                 startActivity(intent);


    }



    private void startFirstAid(String location) {
        Intent intent = new Intent(this, FirstAidActivity.class);
        intent.putExtra("Address", location);
        startActivity(intent);
    }


    private void getLocation() {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double lat;
            double lng;
            List<Address> addresses;
            if(location != null) {
                lat = location.getLatitude();
                lng = location.getLongitude();
                addresses = geocoder.getFromLocation(lat, lng, 1);
                address = addresses.get(0).getAddressLine(0);
            }else{
                lat = 51.279643;
                lng = 1.089364;
                addresses = geocoder.getFromLocation(lat, lng, 1);
                address = addresses.get(0).getAddressLine(0);
            }
        }
        catch(SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        startFirstAid(address);
    }

    public void opendashboard(View view) {

        startActivity(new Intent(this,dashboard.class));
    }

}
