package com.example.pwguide.navigation;

import java.util.ArrayList;

public class Building {

    private String name;
    private double latitude;
    private double longitude;
    private ArrayList <String> rooms;

    public Building(String name, double latitude, double longitude, ArrayList <String> rooms){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rooms = rooms;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public ArrayList<String> getRooms() {return rooms;}

    @Override
    public String toString() {
        return "Building{" +
                "name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
