package com.example.pwguide;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.pwguide.dijkstraAlgorithm.ProgramAlgorithm;
import com.example.pwguide.dijkstraAlgorithm.Vertex;
import com.example.pwguide.navigation.Building;
import com.example.pwguide.navigation.ReadNavigaionsFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;


public class OutsideNavigation extends AppCompatActivity implements LocationListener {

    Button b_rozpocznij;
    ArrayList<Building> buildings = new ArrayList<>();

    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String source_latitude, source_longitude;
    String dest_latitude, dest_longitude;
    private final ProgramAlgorithm programAlgorithm = new ProgramAlgorithm();
    private final ReadNavigaionsFile readNavigaionsFile = new ReadNavigaionsFile();
    int main_index = 0;
    private LinkedList<Vertex> pathInBuilding;
    private String buildingNameTo;
    private String buildingName;
    private String roomTo;
    ArrayList<String> entrances = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outside_navigation);
        try {
            InputStream input = getBaseContext().getAssets().open("navigation.txt");
            buildings = readNavigaionsFile.loadNavigationData(input);
        } catch (IOException e) {

        }

        ArrayList<String> build_name = new ArrayList<>();
        for (Building building : buildings) {
            build_name.add(building.getName());
        }

        AutoCompleteTextView editText1 = findViewById(R.id.build_list3);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, build_name);
        editText1.setAdapter(adapter1);

        ImageView arrow1 = (ImageView) findViewById(R.id.drop_down);

        final AutoCompleteTextView build_list = (AutoCompleteTextView) findViewById(R.id.build_list3);
        final AutoCompleteTextView hall_list = (AutoCompleteTextView) findViewById(R.id.hall_list3);

        build_list.setThreshold(1);

        arrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                build_list.showDropDown();

                String building = build_list.getText().toString();
                int index = 0;
                for (int i = 0; i < buildings.size(); i++) {
                    if (buildings.get(i).getName().equals(building)) {
                        index = i;
                    }
                }
                main_index = index;
                AutoCompleteTextView editText2 = findViewById(R.id.hall_list3);
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_dropdown_item_1line, buildings.get(index).getRooms());
                editText2.setAdapter(adapter2);
                ImageView arrow2 = (ImageView) findViewById(R.id.drop_down2);
                hall_list.setThreshold(1);

                arrow2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hall_list.showDropDown();
                        String building = build_list.getText().toString();
                        int index = 0;
                        for (int i = 0; i < buildings.size(); i++) {
                            if (buildings.get(i).getName().equals(building)) {
                                index = i;
                            }
                        }
                        main_index = index;
                        AutoCompleteTextView editText2 = findViewById(R.id.hall_list3);
                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_dropdown_item_1line, buildings.get(index).getRooms());
                        editText2.setAdapter(adapter2);
                        hall_list.setThreshold(1);
                    }
                });
            }
        });

        editText1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String building = build_list.getText().toString();
                int index = 0;
                for (int i = 0; i < buildings.size(); i++) {
                    if (buildings.get(i).getName().equals(building)) {
                        index = i;
                    }
                }
                main_index = index;
                AutoCompleteTextView editText2 = findViewById(R.id.hall_list3);
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_dropdown_item_1line, buildings.get(index).getRooms());
                editText2.setAdapter(adapter2);
                ImageView arrow2 = (ImageView) findViewById(R.id.drop_down2);
                hall_list.setThreshold(1);

                arrow2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hall_list.showDropDown();
                    }
                });
            }
        });

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        b_rozpocznij = findViewById(R.id.b_rozpocznij2);
        b_rozpocznij.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (build_list.getText().toString().isEmpty()) {
                    Toast.makeText(getBaseContext(), "Musisz podać nazwę budynku", Toast.LENGTH_SHORT).show();
                } else if (!build_name.contains(build_list.getText().toString())) {
                    Toast.makeText(getBaseContext(), "Niepoprawna nazwa budynku", Toast.LENGTH_SHORT).show();

                } else if (!buildings.get(main_index).getRooms().contains(hall_list.getText().toString())) {
                    Toast.makeText(getBaseContext(), "Niepoprawny numer sali dla wybranego budynku", Toast.LENGTH_SHORT).show();

                } else {

                    String building = build_list.getText().toString();
                    System.out.println(building);

                    buildingName = buildings.get(main_index).getName();
                    String buildingNameAbbr = buildingName.toLowerCase(Locale.ROOT);
                    //System.out.println(building);
                    StringBuilder sb = new StringBuilder();
                    for (String s : buildingNameAbbr.split(" ")) {
                        sb.append(s.charAt(0));
                    }
                    //buildingName = buildingName.replaceAll("\\s+", "");
                    buildingNameAbbr = sb + ".txt";
                    buildingNameTo = sb.toString();
                    roomTo = hall_list.getText().toString();
                    System.out.println(buildingNameAbbr);

                    //Trzeba wyliczyć na podstawie współrzędnych gdzie ktoś się znajduje i zapisanych wejść, które
                    //jest najbliższe i upewnić się że da się dojść z tego wejścia do sali (czy zwrócona ścieżka
                    // nie jest pusta, jeśli tak to sprawdzić kolejne wejście

                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        OnGPS();
                    } else {
                        ArrayList<Double> coordinates = getLocation();

                        if (!coordinates.isEmpty()) {
                            source_latitude = String.valueOf(coordinates.get(0));
                            source_longitude = String.valueOf(coordinates.get(1));
                            double lat1 = Math.toRadians(coordinates.get(0));
                            double lon1 = Math.toRadians(coordinates.get(1));
                            double dlon, dlat, lat2, lon2, dest;
                            double lastDest = 0;
                            String tmp_name;
                            for (int i = 0; i < buildings.get(main_index).getEntrances().size(); i++) {
                                lat2 = buildings.get(main_index).getEntrances().get(i).toRadiansLat(buildings.get(main_index).getEntrances().get(i).getLatitude());
                                lon2 = buildings.get(main_index).getEntrances().get(i).toRadiansLon(buildings.get(main_index).getEntrances().get(i).getLongitude());
                                dlon = lon2 - lon1;
                                dlat = lat2 - lat1;
                                dest = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
                                dest = 2 * Math.asin(Math.sqrt(dest));
                                if (dest > lastDest) {
                                    entrances.add(buildings.get(main_index).getEntrances().get(i).getName());
                                } else {
                                    tmp_name = entrances.get(i - 1);
                                    entrances.set(i - 1, buildings.get(main_index).getEntrances().get(i).getName());
                                    entrances.add(tmp_name);
                                }
                                lastDest = dest;
                            }

                            //w zmiennej entrances są nazwy wejsc ulozone wedlug dystansu od biezacej lokalizacji
                            //od najblizszego wejscia do najdalszego
                            String entrance_name = entrances.get(0);
                            try {
                                InputStream input = getBaseContext().getAssets().open(buildingNameAbbr);
                                pathInBuilding = programAlgorithm.programExcute(hall_list.getText().toString(), input, entrance_name);
                            } catch (IOException e) {
//                        throw new IllegalArgumentException("File has to be accessible!");
                            }

                            int entrance_index = 0;


                            for (int i = 0; i < buildings.get(main_index).getEntrances().size(); i++) {
                                if (buildings.get(main_index).getEntrances().get(i).getName().equals(entrance_name)) {
                                    entrance_index = i;
                                    break;
                                }
                            }
                            dest_latitude = String.valueOf(buildings.get(main_index).getEntrances().get(entrance_index).getLatitude());
                            dest_longitude = String.valueOf(buildings.get(main_index).getEntrances().get(entrance_index).getLongitude());


                            DisplayTrack(source_latitude, source_longitude, dest_latitude, dest_longitude);
                            alertDialog();


                        } else {
                            Toast.makeText(getBaseContext(), "Nie można znaleźć twojej lokalizacji.", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Udziel dostępu do GPSa").setCancelable(false).setPositiveButton("Tak", new DialogInterface.OnClickListener() {
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

    private ArrayList<Double> getLocation() {
        ArrayList<Double> coordinates = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(
                OutsideNavigation.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                OutsideNavigation.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, (LocationListener) this, null);
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (locationGPS == null) {
                //locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
                locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
                List<String> providers = locationManager.getProviders(true);
                Location bestLocation = null;
                for (String provider : providers) {
                    Location l = locationManager.getLastKnownLocation(provider);
                    if (l == null) {
                        continue;
                    }
                    if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                        // Found best last known location: %s", l);
                        bestLocation = l;
                    }
                }
                locationGPS = bestLocation;
                //locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                coordinates.add(lat);
                coordinates.add(longi);

            }

        }
        return coordinates;
    }

    private void DisplayTrack(String source_latitude, String source_longitude, String dest_latitude, String dest_longitude) {
        try {
            Uri uri = Uri.parse("http://maps.google.com/maps?saddr=" + source_latitude + "," + source_longitude + "&daddr=" + dest_latitude + "," + dest_longitude);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
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

    private void alertDialog() {
        AlertDialog alertDialog1 = new AlertDialog.Builder(
                OutsideNavigation.this).create();

        //alertDialog1.setTitle("Alert Dialog");

        alertDialog1.setMessage("Kliknij dalej jeśli jest już w budynku");

        alertDialog1.setButton(Dialog.BUTTON_POSITIVE, "DALEJ", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(OutsideNavigation.this, NavigationActivity.class);
                intent.putExtra("pathFrom", pathInBuilding);
                intent.putExtra("buildingFrom", buildingNameTo);
                intent.putExtra("buildingToName", buildingName + "\n" + "Sala " + roomTo);
                intent.putExtra("buildingFromName", "Bieżąca\nlokalizacja");
                startActivity(intent);
            }
        });
        alertDialog1.show();
    }
}