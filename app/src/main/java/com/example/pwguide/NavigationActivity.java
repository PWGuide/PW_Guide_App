package com.example.pwguide;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class NavigationActivity extends AppCompatActivity {
    TextView targetText, sourceText;
    String target = "Gmach Główny\nSala 123";
    String source = "Budynek Wydziału Elektrycznego\nSala 312";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        getSupportActionBar().setTitle("Nawigacja");
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        BottomSheetBehavior<View> sheetBehavior = BottomSheetBehavior.from(findViewById(R.id.sheet_navigation));
        sheetBehavior.setPeekHeight(300);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        targetText = findViewById(R.id.targetText);
        sourceText = findViewById(R.id.sourceText);

        addTargetName(target);
        sourceText.setText(source);


        ImageButton backButton = findViewById(R.id.back_navigation);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void addTargetName(String target) {
        targetText.setText(target);
    }

    public void addSourceName(String source) {
        targetText.setText(source);
    }

}