package com.example.pwguide;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class PlansActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans);
        getSupportActionBar().setTitle("Plan");
    }

}
