package com.example.pwguide;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;;
import android.widget.Toast;

import com.example.pwguide.navigation.Building;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class OutsideNavigation extends AppCompatActivity {

    private static final int PERMISSIONS_FINE_LOCATION = 99;
    Button b_select_plan;
    Button b_rozpocznij;
    String lat, lon, altitude, accuracy, speed, address;
    String source_address, dest_address;

    LocationRequest locationRequest;
    LocationCallback locationCallBack;
    FusedLocationProviderClient fusedLocationProviderClient;

    ArrayList<Building> buildings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outside_navigation);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(30000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        updateGPS();

        locationCallBack = new LocationCallback(){

            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                updateUIValues(locationResult.getLastLocation());
            }
        };

        b_select_plan = findViewById(R.id.b_select_plan3);
        b_select_plan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OutsideNavigation.this, TimetableActivity.class);
                startActivity(intent);
            }
        });

        buildings.add(new Building("Gmach Główny", 52.22082029751707, 21.010043041354443));
        buildings.add(new Building("Gmach Elektrotechniki", 52.221491504548844, 21.00610232600991));
        buildings.add(new Building("Gmach Mechaniki",  52.22096539698025, 21.008062654846032));
        buildings.add(new Building("Stara Kotłownia",  52.220968233478914, 21.00848696833765));

        ArrayList<String> build_name = new ArrayList<>();
        for(Building building:buildings ){
            build_name.add(building.getName());
        }
        //String[] buildings = getResources().getStringArray(R.array.buildings);
        String[] halls = getResources().getStringArray(R.array.halls);
        //getSupportActionBar().setTitle("Plan");
        AutoCompleteTextView editText1 = findViewById(R.id.build_list3);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, build_name);
        editText1.setAdapter(adapter1);
        //editText.setThreshold(1);
        //String input = editText.getText().toString();
        //getSupportActionBar().setTitle("Plan");
        AutoCompleteTextView editText2 = findViewById(R.id.hall_list3);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, halls);
        editText2.setAdapter(adapter2);


        ImageView arrow1 = (ImageView)findViewById(R.id.drop_down);
        ImageView arrow2 = (ImageView)findViewById(R.id.drop_down2);
        final AutoCompleteTextView build_list = (AutoCompleteTextView)findViewById(R.id.build_list3);
        final AutoCompleteTextView hall_list = (AutoCompleteTextView)findViewById(R.id.hall_list3);
        build_list.setThreshold(1);
        hall_list.setThreshold(1);

        arrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                build_list.showDropDown();
            }
        });
        arrow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hall_list.showDropDown();
            }
        });

        b_rozpocznij = findViewById(R.id.b_rozpocznij2);
        b_rozpocznij.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startLocationUpdates();
                String building = editText2.getText().toString();

                int index = 0;
                for(int i = 0; i < buildings.size(); i++) {
                    if (buildings.get(i).getName().equals(building)) {
                        index = i;
                    }
                }
                source_address = address;
                dest_address = buildings.get(index).getName();
                DisplayTrack(source_address.trim(),dest_address.trim());
                //Toast.makeText(getBaseContext(), "Szerokość geograficzna: "+ lat + " długość: " + lon + " Adres: " + address , Toast.LENGTH_SHORT ).show();
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSIONS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    updateGPS();
                }
                else{
                    Toast.makeText(this, "Ta aplikacja wymaga zezwolenia, aby mogła działać poprawnie", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
            updateGPS();
        }
    }

    private void updateGPS(){

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(OutsideNavigation.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    updateUIValues(location);

                }
            });
        }
        else{
            if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},PERMISSIONS_FINE_LOCATION);
            }
        }
    }

    private void updateUIValues(Location location){
        lat = String.valueOf(location.getLatitude());
        lon = String.valueOf(location.getLongitude());
        accuracy = String.valueOf(location.getAccuracy());

        if (location.hasAltitude()){
            altitude = String.valueOf(location.getAltitude());
        }
        else{
            altitude = "Niedostępne";
        }

        if (location.hasSpeed()){
            speed = String.valueOf(location.getSpeed());
        }
        else{
            speed = "Niedostępne";
        }

        Geocoder geocoder = new Geocoder(OutsideNavigation.this);

        try{
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            address = addresses.get(0).getAddressLine(0);

        }
        catch(Exception e){
            address = "Nie można uzyskać adresu ulicy";
        }

    }

    private void DisplayTrack(String sSource, String sDestination){
        try{
            Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + sSource + "/" + sDestination);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }catch (ActivityNotFoundException e){
            //google map nie jest zainstalowane
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
