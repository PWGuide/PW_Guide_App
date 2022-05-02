package com.example.pwguide.navigation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class ReadNavigaionsFile {

    ArrayList<Building> new_buildings = new ArrayList<>();

    public ArrayList<Building> loadNavigationData(InputStream input) throws IOException {
        read(input);
        return new_buildings;
    }

    public void read(InputStream input) throws IOException {

        String build_name = null;
        Entrance entrance;
        ArrayList<String> rooms = new ArrayList<>();
        ArrayList<Entrance> entrances_list = new ArrayList<>();
        int flag = 0;


        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            String strLine;

            while ((strLine = br.readLine()) != null) {
                String[] vars = strLine.split(" ");
                if (flag == 0) {
                    build_name = strLine;
                }
                if (strLine.contains("#")) {
                    flag = -1;
                    new_buildings.add(new Building(build_name, rooms, entrances_list));
                    entrances_list = new ArrayList<>();
                }
                if (flag == 1) {
                    rooms = new ArrayList<>(Arrays.asList(vars));
                    Collections.sort(rooms);

                }
                if (flag >= 2) {
                    entrance = new Entrance(vars[0], Double.parseDouble(vars[1]), Double.parseDouble(vars[2]));
                    entrances_list.add(entrance);
                }
                flag++;

            }
            input.close();
        } catch (IOException e) {
            throw new IllegalArgumentException("File has to be accessible!");
        }

    }
}
