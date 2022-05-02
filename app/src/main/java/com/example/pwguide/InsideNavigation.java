package com.example.pwguide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pwguide.dijkstraAlgorithm.ProgramAlgorithm;
import com.example.pwguide.dijkstraAlgorithm.Vertex;
import com.example.pwguide.navigation.Building;
import com.example.pwguide.navigation.ReadNavigaionsFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;

public class InsideNavigation extends AppCompatActivity {

    Button b_select_plan;
    Button b_take_photo;
    Button b_select_plan2;
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
                        System.out.println(buildings.get(i).getName());
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
                                System.out.println(buildings.get(i).getName());
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
                        System.out.println(buildings.get(i).getName());
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
                                System.out.println(buildings.get(i).getName());
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
                        System.out.println(buildings.get(i).getName());
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
                        System.out.println(buildings.get(i).getName());
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


        b_select_plan = findViewById(R.id.b_select_plan);
        b_select_plan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsideNavigation.this, TimetableActivity.class);
                startActivity(intent);
            }
        });
        b_take_photo = findViewById(R.id.b_take_photo);
        b_take_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsideNavigation.this, CameraActivity.class);
                startActivity(intent);
            }
        });

        b_select_plan2 = findViewById(R.id.b_select_plan2);
        b_select_plan2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsideNavigation.this, TimetableActivity.class);
                startActivity(intent);
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
                } else {
                    String buldingName = build_list1.getText().toString();
                    LinkedList<Vertex> path = new LinkedList<>();

                buldingName = buldingName.replaceAll("\\s+","");
                //buldingName = buldingName + ".txt";
                buldingName = "mini" + ".txt";
                System.out.println(buldingName);
                try {
                    InputStream input = getBaseContext().getAssets().open(buldingName);
                   // path = programAlgorithm.programExcute(hall_list1.getText().toString(),input);
                    path = programAlgorithm.programExcute("ee",input);
                    //Toast.makeText(getApplicationContext(), path, Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                       // throw new IllegalArgumentException("File has to be accessible!");
                }
                Intent intent = new Intent(InsideNavigation.this, NavigationActivity.class);
                intent.putExtra("pathList", path);
                startActivity(intent);
            }
        });





            }
        });


    }


}
