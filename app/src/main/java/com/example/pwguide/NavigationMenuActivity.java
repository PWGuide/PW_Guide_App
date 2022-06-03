package com.example.pwguide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class NavigationMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_menu);
        getSupportActionBar().setTitle("Nawigacja");

        Button button_plan = findViewById(R.id.btn_plan);
        Button button_manual = findViewById(R.id.btn_manual);

        button_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavigationMenuActivity.this, OutsideNavigation.class);
                startActivity(intent);
            }
        });

        button_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavigationMenuActivity.this, InsideNavigation.class);
                startActivity(intent);
            }
        });

    }

}
