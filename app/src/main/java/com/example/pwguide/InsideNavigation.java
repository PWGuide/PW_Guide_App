package com.example.pwguide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pwguide.dijkstraAlgorithm.ProgramAlgorithm;

import java.io.IOException;
import java.io.InputStream;

public class InsideNavigation extends AppCompatActivity {

    Button b_select_plan;
    Button b_take_photo;
    Button b_select_plan2;
    Button b_rozpocznij;

    private final ProgramAlgorithm programAlgorithm = new ProgramAlgorithm();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_navigation);

        String[] buildings = getResources().getStringArray(R.array.buildings);
        String[] halls = getResources().getStringArray(R.array.halls);
        //getSupportActionBar().setTitle("Plan");
        AutoCompleteTextView editText1 = findViewById(R.id.build_list1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, buildings);
        editText1.setAdapter(adapter1);
        //editText.setThreshold(1);
        //String input = editText.getText().toString();
        //getSupportActionBar().setTitle("Plan");
        AutoCompleteTextView editText2 = findViewById(R.id.build_list2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, buildings);
        editText2.setAdapter(adapter2);

        AutoCompleteTextView editText3 = findViewById(R.id.hall_list1);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, halls);
        editText3.setAdapter(adapter3);

        AutoCompleteTextView editText4 = findViewById(R.id.hall_list2);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, halls);
        editText4.setAdapter(adapter4);


        ImageView arrow1 = (ImageView)findViewById(R.id.drop_down3);
        ImageView arrow2 = (ImageView)findViewById(R.id.drop_down4);
        ImageView arrow3 = (ImageView)findViewById(R.id.drop_down5);
        ImageView arrow4 = (ImageView)findViewById(R.id.drop_down6);
        final AutoCompleteTextView build_list1 = (AutoCompleteTextView)findViewById(R.id.build_list1);
        final AutoCompleteTextView hall_list1 = (AutoCompleteTextView)findViewById(R.id.hall_list1);
        final AutoCompleteTextView build_list2 = (AutoCompleteTextView)findViewById(R.id.build_list2);
        final AutoCompleteTextView hall_list2 = (AutoCompleteTextView)findViewById(R.id.hall_list2);
        build_list1.setThreshold(1);
        hall_list1.setThreshold(1);
        build_list2.setThreshold(1);
        hall_list2.setThreshold(1);

        arrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                build_list2.showDropDown();
            }
        });
        arrow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                build_list1.showDropDown();
            }
        });

        arrow3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hall_list2.showDropDown();
            }
        });
        arrow4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hall_list1.showDropDown();
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
                Intent intent = new Intent(InsideNavigation.this, NavigationActivity.class);
                startActivity(intent);

                String buldingName = build_list1.getText().toString();

                buldingName = buldingName.replaceAll("\\s+","");
                buldingName = buldingName + ".txt";
                System.out.println(buldingName);

                try {
                    InputStream input = getBaseContext().getAssets().open(buldingName);
                    programAlgorithm.programExcute(hall_list1.getText().toString(),input);
                } catch (IOException e) {
                       // throw new IllegalArgumentException("File has to be accessible!");
                }

            }
        });





    }




}
