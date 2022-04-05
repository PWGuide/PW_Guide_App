package com.example.pwguide;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
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
import android.location.Location;
;
import android.widget.Toast;

import com.example.pwguide.navigation.Building;
;

import java.util.ArrayList;

public class OutsideNavigation extends AppCompatActivity implements LocationListener {

    Button b_select_plan;
    Button b_rozpocznij;
    ArrayList<Building> buildings = new ArrayList<>();
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String source_latitude, source_longitude;
    String dest_latitude, dest_longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outside_navigation);

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

        AutoCompleteTextView editText1 = findViewById(R.id.build_list3);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, build_name);
        editText1.setAdapter(adapter1);

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

        ActivityCompat.requestPermissions( this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        b_rozpocznij = findViewById(R.id.b_rozpocznij2);
        b_rozpocznij.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String building = build_list.getText().toString();
                System.out.println(building);
                int index = 0;
                for(int i = 0; i < buildings.size(); i++) {
                    if (buildings.get(i).getName().equals(building)) {
                        System.out.println(buildings.get(i).getName());
                        index = i;
                    }
                }
                dest_latitude = String.valueOf(buildings.get(index).getLatitude());
                dest_longitude = String.valueOf(buildings.get(index).getLongitude());

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    OnGPS();
                } else {
                    getLocation(dest_latitude, dest_longitude);
                }
            }
        });
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Udziel dostępu do GPSa").setCancelable(false).setPositiveButton("Tak", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation(String dest_latitude, String dest_longitude) {
        if (ActivityCompat.checkSelfPermission(
                OutsideNavigation.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                OutsideNavigation.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, (LocationListener) this, null);
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(locationGPS == null){
                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
                locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                source_latitude = String.valueOf(lat);
                source_longitude = String.valueOf(longi);
                //Toast.makeText(this,"Twoja lokalizacja: " + "\n" + "Latitude: " + source_latitude + "\n" + "Longitude: " + source_longitude, Toast.LENGTH_SHORT).show();
                DisplayTrack(source_latitude, source_longitude, dest_latitude, dest_longitude);
            } else {
                Toast.makeText(this, "Nie można znaleźć twojej lokalizacji.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void DisplayTrack(String source_latitude, String source_longitude, String dest_latitude, String dest_longitude){
        try{
            Uri uri = Uri.parse("http://maps.google.com/maps?saddr=" + source_latitude +"," + source_longitude + "&daddr=" + dest_latitude +"," + dest_longitude);
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

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double lat = location.getLatitude();
        double longi = location.getLongitude();
        source_latitude = String.valueOf(lat);
        source_longitude = String.valueOf(longi);
        //Toast.makeText(this,"Twoja lokalizacja: " + "\n" + "Latitude: " + source_latitude + "\n" + "Longitude: " + source_longitude, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}
