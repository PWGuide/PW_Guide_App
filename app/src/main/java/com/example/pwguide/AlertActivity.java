package com.example.pwguide;

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AlertActivity extends AppCompatActivity {
    public void createAlert(AlertDialog.Builder builder, String title, String short_txt, String long_txt) {

        builder.setTitle(title);
        builder.setMessage(short_txt);
        builder.setCancelable(true);
        builder.setPositiveButton("Więcej", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                builder.setTitle(title);
                builder.setMessage(long_txt);
                builder.setCancelable(false);
                builder.setPositiveButton("Ok", null);
                builder.create().show();
            }

        }).setNegativeButton("Zamknij", null);

        builder.create().show();
    }
}
