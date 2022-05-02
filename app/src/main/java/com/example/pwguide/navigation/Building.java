package com.example.pwguide.navigation;

import java.util.ArrayList;


public class Building {

    private String name;
    private ArrayList <String> rooms;
    private ArrayList <Entrance> entrances;

    public Building(String name, ArrayList <String> rooms, ArrayList <Entrance> entrances){
        this.name = name;
        this.rooms = rooms;
        this.entrances = entrances;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getRooms() {return rooms;}

    public ArrayList<Entrance> getEntrances() {return entrances;}

    @Override
    public String toString() {
        return "Building{" +
                "name='" + name + '\'' +
                ", rooms=" + rooms +
                '}';
    }
}
