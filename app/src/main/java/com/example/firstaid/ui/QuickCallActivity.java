package com.example.firstaid.ui;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.firstaid.R;
import com.example.firstaid.model.Header;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class QuickCallActivity extends FragmentActivity implements OnMapReadyCallback {

    private LocationManager locationManager;
    private Geocoder geocoder;
    private Button call108Button;

    private TextView addressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_call);

        Header header = findViewById(R.id.headerlayout);
        header.initHeader();
        header.imageView.setImageResource(R.drawable.header_logo);

        header.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuickCallActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        addressTextView = findViewById(R.id.addressTextView);
        Button call108Button = findViewById(R.id.call108Button);

        geocoder = new Geocoder(this, Locale.getDefault());
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        call108Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:108"));
                startActivity(intent);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        String address;
        try {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double lat;
            double lng;
            List<Address> addresses;
            if(location != null) {
                lat = location.getLatitude();
                lng = location.getLongitude();
                LatLng currentLocation = new LatLng(lat, lng);
                googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17));

                addresses = geocoder.getFromLocation(lat, lng, 1);
                 address = addresses.get(0).getAddressLine(0);
            }else{
                lat = 51.279643;
                lng = 1.089364;
                LatLng currentLocation = new LatLng(lat, lng);
                googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17));

                addresses = geocoder.getFromLocation(lat, lng, 1);
                address = addresses.get(0).getAddressLine(0);
                 //address = "GPS not found";
            }

            addressTextView.setText(address);

        }
        catch(SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
