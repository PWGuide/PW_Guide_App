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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.pwguide.dijkstraAlgorithm.Vertex;
import com.example.pwguide.navigation.NavigationCanvas;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.view.View.INVISIBLE;

public class NavigationActivity extends AppCompatActivity {
    TextView targetText, sourceText;
    String target = "Gmach Główny\nSala 123";
    String source = "Budynek Wydziału Elektrycznego\nSala 312";
    private NavigationCanvas canvas;
    private Button googleMaps;
    public static boolean GOOGLE_MAPS_VIS = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        initGoogleMapsButton();
        getSupportActionBar().setTitle("Nawigacja");
        ArrayList<Vertex> pathFrom = (ArrayList<Vertex>) getIntent().getSerializableExtra("pathFrom");
        ArrayList<Vertex> pathTo = (ArrayList<Vertex>) getIntent().getSerializableExtra("pathTo");
        String buildingFrom = getIntent().getStringExtra("buildingFrom");
        String buildingTo = getIntent().getStringExtra("buildingTo");
        String source_latitude = getIntent().getStringExtra("source_latitude");
        String source_longitude = getIntent().getStringExtra("source_longitude");
        String dest_latitude = getIntent().getStringExtra("dest_latitude");
        String dest_longitude = getIntent().getStringExtra("dest_longitude");
        String buildingToName = getIntent().getStringExtra("buildingToName");
        String buildingFromName = getIntent().getStringExtra("buildingFromName");
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        if(pathTo != null) {
            googleMaps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DisplayTrack(source_latitude, source_longitude, dest_latitude, dest_longitude);
                    alertDialog(pathTo, buildingTo);
                }
            });
            GOOGLE_MAPS_VIS = true;
        } else {
            googleMaps.setVisibility(INVISIBLE);
            GOOGLE_MAPS_VIS = false;
        }

//        BottomSheetBehavior<View> sheetBehavior = BottomSheetBehavior.from(findViewById(R.id.sheet_navigation));
//        sheetBehavior.setPeekHeight(300);
//        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        canvas = findViewById(R.id.navigation_canvas);
        ConstraintLayout layout = findViewById(R.id.navigation_layout);
        canvas.setGoogleMapsButton(googleMaps);
        canvas.addButton(layout);

        canvas.setPath(pathFrom, buildingFrom);
        targetText = findViewById(R.id.targetText);
        sourceText = findViewById(R.id.sourceText);

        addTargetName(buildingToName);
        addSourceName(buildingFromName);


        ImageButton backButton = findViewById(R.id.back_navigation);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initGoogleMapsButton() {
        googleMaps = new Button(this);
        googleMaps.setText("Przejdź do\nGoogle Maps");
        googleMaps.setBackgroundColor(getResources().getColor(R.color.button));
        googleMaps.setTextColor(getResources().getColor(R.color.button_text));
        googleMaps.setPadding(10, 5, 5, 5);
        googleMaps.setAllCaps(false);
        Drawable googleMapsDrawable = AppCompatResources.getDrawable(this, R.drawable.google_maps);
        googleMapsDrawable.setBounds(0, 0, 60, 60);
        googleMaps.setCompoundDrawables(null, null, googleMapsDrawable, null);
        googleMaps.setVisibility(INVISIBLE);
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

    private void alertDialog(List<Vertex> pathTo, String buildingTo) {
        AlertDialog alertDialog1 = new AlertDialog.Builder(
                NavigationActivity.this).create();

        alertDialog1.setMessage("Kliknij dalej jeśli jest już w budynku");

        alertDialog1.setButton(Dialog.BUTTON_POSITIVE, "DALEJ", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                canvas.setPath(pathTo, buildingTo);
            }
        });
        alertDialog1.show();
    }

    public void addTargetName(String target) {
        targetText.setText(target);
    }

    public void addSourceName(String source) {
        sourceText.setText(source);
    }

}