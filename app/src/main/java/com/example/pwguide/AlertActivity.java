package com.example.pwguide;

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

public class AlertActivity {
    public void createAlert(AlertDialog.Builder builder) {

        builder.setTitle("Gmach Główny Politechniki Warszawskiej");
        builder.setMessage("zabytkowy budynek Politechniki Warszawskiej znajdujący się na placu Politechniki w Warszawie.");
        builder.setCancelable(true);
        builder.setPositiveButton("MORE INFO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                builder.setTitle("Gmach Główny Politechniki Warszawskiej");
                builder.setMessage("Gmach powstał na zadrzewionym skwerze, na którym m.in. w 1896 odbyła się wystawa higieniczna[1]. Budowa gmachu zaprojektowanego dla Instytutu Politechnicznego im. Cara Mikołaja II przez Stefana Szyllera rozpoczęła się w lipcu 1899 roku i trwała 2 lata.\n" +
                        "\n" +
                        "Gmach jest typowym przykładem architektury odwołującej się do stylistyki włoskiego renesansu i baroku. Jego reprezentacyjny charakter podkreśla monumentalna, dwukondygnacyjna fasada o zaokrąglonych narożnikach. Gmach został zaprojektowany według klasycznych reguł przyjmowanych przez ówczesne uczelnie w Europie. Masywne mury nośne budynku stanowiły nie tylko konstrukcję podpierającą stropy i dach, ale kryły w sobie sieć kanałów dawnego systemu ogrzewania ciepłym powietrzem doprowadzanym z kotłowni zewnętrznej. Solidna konstrukcja przetrwała pożary i zniszczenia od pocisków z czasu powstania warszawskiego.\n" +
                        "\n" +
                        "Projektując elewacje Gmachu Głównego, Stefan Szyller najwięcej starań poświęcił ukształtowaniu fasady o wspaniałej ekspozycji od strony placu. Według zasady architecture parlante XIX-wieczne dekoracje budynków stylu wysokiego: rzeźby i płaskorzeźby, malarstwo, freski i sgraffito - przedstawienia alegoryczne i symboliczne, podkreślały, przede wszystkim jednak uwznioślały, przeznaczenie, funkcje i charakter gmachów publicznych.\n" +
                        "\n" +
                        "W 2005, dla upamiętnienia nadania doktoratu honoris causa Politechniki (1926), w gmachu odsłonięto pomnik Marii Skłodowskiej-Curie zaprojektowany przez Maksymiliana Biskupskiego[2]. ");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", null);
                builder.create().show();
            }

        }).setNegativeButton("Cancel", null);

        builder.create().show();
    }
}
