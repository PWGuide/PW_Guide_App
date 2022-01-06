package com.example.pwguide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class OutsideNavigation extends AppCompatActivity {

    Button b_select_plan;
    Button b_rozpocznij;

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
        b_rozpocznij = findViewById(R.id.b_rozpocznij2);
        b_rozpocznij.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OutsideNavigation.this, NavigationActivity.class);
                startActivity(intent);
            }
        });

        String[] buildings = getResources().getStringArray(R.array.buildings);
        String[] halls = getResources().getStringArray(R.array.halls);
        //getSupportActionBar().setTitle("Plan");
        AutoCompleteTextView editText1 = findViewById(R.id.build_list3);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, buildings);
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


    }

}
