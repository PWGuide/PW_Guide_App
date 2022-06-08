package com.example.pwguide;

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AlertActivity extends AppCompatActivity {
    public void createAlert(AlertDialog.Builder builder, String title, String short_txt, String long_txt, boolean recognised) {

        builder.setTitle(title);
        builder.setMessage(short_txt);
        builder.setCancelable(true);
        if (!recognised) {
            builder.setPositiveButton("Więcej", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    builder.setTitle(title);
                    builder.setMessage(long_txt);
                    builder.setCancelable(false);
                    builder.setPositiveButton(null, null);
                    builder.create().show();
                }

            }).setNegativeButton("Zamknij", null);
        } else {
            builder.setNegativeButton("Zamknij", null);
        }


        builder.create().show();
    }
}
