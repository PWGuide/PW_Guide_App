package com.example.pwguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pwguide.navigation.FetchURL;
import com.example.pwguide.navigation.TaskLoadedCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapOutsideActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    GoogleMap map;
    MarkerOptions place1, place2;
    Polyline currentPolyline;
    String temp_slat, temp_slon, dest_address, source_address;
    double slat, slon, dlat, dlon;
    Button btn_google_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_outside);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFrag);
        mapFragment.getMapAsync(this);

        Intent intent = this.getIntent();
        temp_slat = intent.getStringExtra("slat");
        temp_slon = intent.getStringExtra("slon");
        slat = Double.parseDouble(temp_slat);
        slon = Double.parseDouble(temp_slon);
        dlat = intent.getDoubleExtra("dlat", 0);
        dlon = intent.getDoubleExtra("dlon", 0);
        source_address = intent.getStringExtra("source_address");
        dest_address = intent.getStringExtra("dest_address");
        place1 = new MarkerOptions().position(new LatLng(slat, slon)).title("Location 1");
        place2 = new MarkerOptions().position(new LatLng(dlat, dlon)).title("Location 2");

        String url = getUrl(place1.getPosition(), place2.getPosition(), "walking");
        new FetchURL(MapOutsideActivity.this).execute(url,"walking");

        btn_google_map = findViewById(R.id.btn_google_map);
        btn_google_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisplayTrack(source_address.trim(),dest_address.trim());
            }
        });


    }
    @Override
    public void onMapReady(GoogleMap googleMap){
        map = googleMap;
        map.addMarker(place1);
        map.addMarker(place2);

    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String mode = "mode=" + directionMode;
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" +  getResources().getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if(currentPolyline!= null)
            currentPolyline.remove();
        currentPolyline = map.addPolyline((PolylineOptions) values[0]);
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