package com.example.pwguide;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.pwguide.dijkstraAlgorithm.ProgramAlgorithm;
import com.example.pwguide.dijkstraAlgorithm.Vertex;
import com.example.pwguide.navigation.Building;
import com.example.pwguide.navigation.ReadNavigaionsFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;

import static android.view.View.INVISIBLE;

public class InsideNavigation extends AppCompatActivity {

    Button b_rozpocznij;
    ArrayList<Building> buildings = new ArrayList<>();
    private final ReadNavigaionsFile readNavigaionsFile = new ReadNavigaionsFile();
    int main_index1 = 0;
    int main_index2 = 0;

    private final ProgramAlgorithm programAlgorithm = new ProgramAlgorithm();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_navigation);
        getSupportActionBar().setTitle("Nawigacja");

        try {
            InputStream input = getBaseContext().getAssets().open("navigation.txt");
            buildings = readNavigaionsFile.loadNavigationData(input);
        } catch (IOException e) {

        }

        //String[] buildings = getResources().getStringArray(R.array.buildings);
        //String[] halls = getResources().getStringArray(R.array.halls);

        ArrayList<String> build_name = new ArrayList<>();
        for (Building building : buildings) {
            build_name.add(building.getName());
        }


        //getSupportActionBar().setTitle("Plan");
        AutoCompleteTextView editText1 = findViewById(R.id.build_list1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, build_name);
        editText1.setAdapter(adapter1);
        //editText.setThreshold(1);
        //String input = editText.getText().toString();
        //getSupportActionBar().setTitle("Plan");
        AutoCompleteTextView editText2 = findViewById(R.id.build_list2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, build_name);
        editText2.setAdapter(adapter2);

        ImageView arrow1 = (ImageView) findViewById(R.id.drop_down3);
        ImageView arrow2 = (ImageView) findViewById(R.id.drop_down4);
        final AutoCompleteTextView build_list1 = (AutoCompleteTextView) findViewById(R.id.build_list1);
        final AutoCompleteTextView hall_list1 = (AutoCompleteTextView) findViewById(R.id.hall_list1);
        final AutoCompleteTextView build_list2 = (AutoCompleteTextView) findViewById(R.id.build_list2);
        final AutoCompleteTextView hall_list2 = (AutoCompleteTextView) findViewById(R.id.hall_list2);
        build_list1.setThreshold(1);
        build_list2.setThreshold(1);


        arrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                build_list2.showDropDown();

                String building = build_list2.getText().toString();
                int index = 0;
                for (int i = 0; i < buildings.size(); i++) {
                    if (buildings.get(i).getName().equals(building)) {
                        //System.out.println(buildings.get(i).getName());
                        index = i;
                    }
                }
                main_index2 = index;
                AutoCompleteTextView editText4 = findViewById(R.id.hall_list2);
                ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_dropdown_item_1line, buildings.get(index).getRooms());
                editText4.setAdapter(adapter4);
                ImageView arrow3 = (ImageView) findViewById(R.id.drop_down5);
                hall_list2.setThreshold(1);


                arrow3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hall_list2.showDropDown();
                        String building = build_list2.getText().toString();
                        int index = 0;
                        for (int i = 0; i < buildings.size(); i++) {
                            if (buildings.get(i).getName().equals(building)) {
                                //System.out.println(buildings.get(i).getName());
                                index = i;
                            }
                        }
                        main_index2 = index;
                        AutoCompleteTextView editText4 = findViewById(R.id.hall_list2);
                        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_dropdown_item_1line, buildings.get(index).getRooms());
                        editText4.setAdapter(adapter4);
                        hall_list2.setThreshold(1);
                    }
                });

            }
        });
        arrow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                build_list1.showDropDown();

                String building = build_list1.getText().toString();
                int index = 0;
                for (int i = 0; i < buildings.size(); i++) {
                    if (buildings.get(i).getName().equals(building)) {
                        //System.out.println(buildings.get(i).getName());
                        index = i;
                    }
                }
                main_index1 = index;
                AutoCompleteTextView editText3 = findViewById(R.id.hall_list1);
                ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_dropdown_item_1line, buildings.get(index).getRooms());
                editText3.setAdapter(adapter3);
                ImageView arrow4 = (ImageView) findViewById(R.id.drop_down6);
                hall_list1.setThreshold(1);

                arrow4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hall_list1.showDropDown();
                        String building = build_list1.getText().toString();
                        int index = 0;
                        for (int i = 0; i < buildings.size(); i++) {
                            if (buildings.get(i).getName().equals(building)) {
                                //System.out.println(buildings.get(i).getName());
                                index = i;
                            }
                        }
                        main_index1 = index;
                        AutoCompleteTextView editText3 = findViewById(R.id.hall_list1);
                        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_dropdown_item_1line, buildings.get(index).getRooms());
                        editText3.setAdapter(adapter3);
                        hall_list1.setThreshold(1);
                    }
                });
            }
        });


        editText1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String building = build_list1.getText().toString();
                int index = 0;
                for (int i = 0; i < buildings.size(); i++) {
                    if (buildings.get(i).getName().equals(building)) {
                        //System.out.println(buildings.get(i).getName());
                        index = i;
                    }
                }
                main_index1 = index;
                AutoCompleteTextView editText3 = findViewById(R.id.hall_list1);
                ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_dropdown_item_1line, buildings.get(index).getRooms());
                editText3.setAdapter(adapter3);
                ImageView arrow4 = (ImageView) findViewById(R.id.drop_down6);
                hall_list1.setThreshold(1);

                arrow4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hall_list1.showDropDown();
                    }
                });
            }
        });

        editText2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String building = build_list2.getText().toString();
                int index = 0;
                for (int i = 0; i < buildings.size(); i++) {
                    if (buildings.get(i).getName().equals(building)) {
                        //System.out.println(buildings.get(i).getName());
                        index = i;
                    }
                }
                main_index2 = index;
                AutoCompleteTextView editText4 = findViewById(R.id.hall_list2);
                ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_dropdown_item_1line, buildings.get(index).getRooms());
                editText4.setAdapter(adapter4);
                ImageView arrow3 = (ImageView) findViewById(R.id.drop_down5);
                hall_list2.setThreshold(1);

                arrow3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hall_list2.showDropDown();
                    }
                });
            }
        });

        b_rozpocznij = findViewById(R.id.b_rozpocznij);
        b_rozpocznij.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (build_list1.getText().toString().isEmpty() || build_list2.getText().toString().isEmpty()) {
                    Toast.makeText(getBaseContext(), "Musisz podać nazwę budynku", Toast.LENGTH_SHORT).show();
                } else if (hall_list1.getText().toString().isEmpty() || hall_list2.getText().toString().isEmpty()) {
                    Toast.makeText(getBaseContext(), "Musisz podać numer sali", Toast.LENGTH_SHORT).show();

                } else if (!build_name.contains(build_list1.getText().toString()) || !build_name.contains(build_list2.getText().toString())) {
                    Toast.makeText(getBaseContext(), "Niepoprawna nazwa budynku", Toast.LENGTH_SHORT).show();
                } else if (!buildings.get(main_index1).getRooms().contains(hall_list1.getText().toString()) || !buildings.get(main_index2).getRooms().contains(hall_list2.getText().toString())) {
                    Toast.makeText(getBaseContext(), "Niepoprawny numer sali dla wybranego budynku", Toast.LENGTH_SHORT).show();
                } else if (build_list1.getText().toString().equals(build_list2.getText().toString()) &&
                        hall_list1.getText().toString().equals(hall_list2.getText().toString())) {
                    Toast.makeText(getBaseContext(), "Już jesteś na miejscu ;)", Toast.LENGTH_SHORT).show();
                } else if(build_list1.getText().toString().equals(build_list2.getText().toString())){
                    String buildingNameFrom = build_list2.getText().toString();
                    LinkedList<Vertex> pathFrom = new LinkedList<>();

                    buildingNameFrom = buildingNameFrom.toLowerCase(Locale.ROOT);

                    StringBuilder sb = new StringBuilder();
                    for(String s: buildingNameFrom.split(" ")) {
                        sb.append(s.charAt(0));
                    }

                    buildingNameFrom = sb.toString();

                    try {
                        InputStream input = getBaseContext().getAssets().open(buildingNameFrom+ ".txt");
                        // pathFrom = programAlgorithm.programExcute(hall_list1.getText().toString(),input);
                        pathFrom = programAlgorithm.programExcute(hall_list1.getText().toString(), input, hall_list2.getText().toString());
                        //Toast.makeText(getApplicationContext(), pathFrom, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        // throw new IllegalArgumentException("File has to be accessible!");
                    }


                    Intent intent = new Intent(InsideNavigation.this, NavigationActivity.class);
                    intent.putExtra("buildingFrom", buildingNameFrom);
                    intent.putExtra("pathFrom", pathFrom);
                    intent.putExtra("buildingToName", build_list1.getText().toString() + "\n" + "Sala " + hall_list1.getText().toString());
                    intent.putExtra("buildingFromName", build_list2.getText().toString() + "\n" + "Sala " + hall_list2.getText().toString());
                    startActivity(intent);
                } else {
                    String buildingNameFrom = build_list2.getText().toString();
                    LinkedList<Vertex> pathFrom = new LinkedList<>();
                    LinkedList<Vertex> pathTo = new LinkedList<>();

                    buildingNameFrom = buildingNameFrom.toLowerCase(Locale.ROOT);

                    StringBuilder sb = new StringBuilder();
                    for(String s: buildingNameFrom.split(" ")) {
                        sb.append(s.charAt(0));
                    }

                    buildingNameFrom = sb.toString();

                    String buildingNameTo = build_list1.getText().toString();
                    buildingNameTo = buildingNameTo.toLowerCase(Locale.ROOT);

                    sb.setLength(0);
                    for(String s: buildingNameTo.split(" ")) {
                        sb.append(s.charAt(0));
                    }

                    buildingNameTo = sb.toString();

                    try {
                        InputStream input = getBaseContext().getAssets().open(buildingNameFrom+ ".txt");
                        // pathFrom = programAlgorithm.programExcute(hall_list1.getText().toString(),input);
                        pathFrom = programAlgorithm.programExcute(buildingNameTo, input, hall_list2.getText().toString());
                        input = getBaseContext().getAssets().open(buildingNameTo + ".txt");
                        pathTo = programAlgorithm.programExcute(hall_list1.getText().toString(), input, buildingNameFrom);
                        //Toast.makeText(getApplicationContext(), pathFrom, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        // throw new IllegalArgumentException("File has to be accessible!");
                    }
                    if(!build_list1.getText().toString().equals(build_list2.getText().toString())) {
                        for(Vertex s: pathFrom) {
                            System.out.println(s.getName());
                        }
                        for(Vertex s: pathTo) {
                            System.out.println(s.getName());
                        }
                        pathFrom.removeLast();
                        pathTo.removeFirst();
                    }

                    //String entrance_name1 = "w1";
                    String entrance_name1 = pathFrom.getLast().getName();
                    int entrance_index1 = 0;
                    //String entrance_name2 = "w1";
                    String entrance_name2 = pathTo.getFirst().getName();
                    int entrance_index2 = 0;

                    for (int i = 0; i < buildings.get(main_index1).getEntrances().size(); i++) {
                        if (buildings.get(main_index1).getEntrances().get(i).getName().equals(entrance_name1)) {
                            entrance_index1 = i;
                            break;
                        }
                    }

                    for (int i = 0; i < buildings.get(main_index2).getEntrances().size(); i++) {
                        if (buildings.get(main_index2).getEntrances().get(i).getName().equals(entrance_name2)) {
                            entrance_index2 = i;
                            break;
                        }
                    }

                    Intent intent = new Intent(InsideNavigation.this, NavigationActivity.class);
                    intent.putExtra("buildingFrom", buildingNameFrom);
                    intent.putExtra("source_latitude", String.valueOf(buildings.get(main_index2).getEntrances().get(entrance_index2).getLatitude()));
                    intent.putExtra("source_longitude", String.valueOf(buildings.get(main_index2).getEntrances().get(entrance_index2).getLongitude()));
                    intent.putExtra("dest_latitude", String.valueOf(buildings.get(main_index1).getEntrances().get(entrance_index1).getLatitude()));
                    intent.putExtra("dest_longitude", String.valueOf(buildings.get(main_index1).getEntrances().get(entrance_index1).getLongitude()));
                    intent.putExtra("pathFrom", pathFrom);
                    intent.putExtra("pathTo", pathTo);
                    intent.putExtra("buildingTo", buildingNameTo);
                    intent.putExtra("buildingToName", build_list1.getText().toString() + "\n" + "Sala " + hall_list1.getText().toString());
                    intent.putExtra("buildingFromName", build_list2.getText().toString() + "\n" + "Sala " + hall_list2.getText().toString());
                    startActivity(intent);
                }
            }

        });
    }


}
