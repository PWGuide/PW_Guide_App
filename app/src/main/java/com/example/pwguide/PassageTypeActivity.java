package com.example.pwguide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PassageTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passage);
        //getSupportActionBar().setTitle("Wybór przejścia");

        Button button_plan = findViewById(R.id.btn_plan);
        Button button_manual = findViewById(R.id.btn_manual);

        button_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassageTypeActivity.this, TimetableActivity.class);
                startActivity(intent);
            }
        });

        button_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassageTypeActivity.this, NamesActivity.class);
                startActivity(intent);
            }
        });

    }

}
