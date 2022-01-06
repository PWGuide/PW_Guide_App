package com.example.pwguide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PlansActivity extends AppCompatActivity {

    Button b_select_plan;
    Button b_take_photo;
    Button b_select_plan2;
    Button b_rozpocznij;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans);

        String[] buildings = getResources().getStringArray(R.array.buildings);
        String[] halls = getResources().getStringArray(R.array.halls);
        //getSupportActionBar().setTitle("Plan");
        AutoCompleteTextView editText1 = findViewById(R.id.build_list1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, buildings);
        editText1.setAdapter(adapter1);
        //editText.setThreshold(1);
        //String input = editText.getText().toString();
        //getSupportActionBar().setTitle("Plan");
        AutoCompleteTextView editText2 = findViewById(R.id.build_list2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, buildings);
        editText2.setAdapter(adapter2);

        AutoCompleteTextView editText3 = findViewById(R.id.hall_list1);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, halls);
        editText3.setAdapter(adapter3);

        AutoCompleteTextView editText4 = findViewById(R.id.hall_list1);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, halls);
        editText4.setAdapter(adapter4);

        b_select_plan = findViewById(R.id.b_select_plan);
        b_select_plan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlansActivity.this, TimetableActivity.class);
                startActivity(intent);
            }
        });
        b_take_photo = findViewById(R.id.b_select_plan);
        b_take_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlansActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });

        b_select_plan2 = findViewById(R.id.b_select_plan);
        b_select_plan2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlansActivity.this, TimetableActivity.class);
                startActivity(intent);
            }
        });

        b_rozpocznij = findViewById(R.id.b_rozpocznij);
        b_rozpocznij.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlansActivity.this, NavigationActivity.class);
                startActivity(intent);
            }
        });





    }




}
