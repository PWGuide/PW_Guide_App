package com.example.pwguide.navigation;

public class Entrance {

    private String name;
    private double latitude;
    private double longitude;

    public Entrance(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
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

    @Override
    public String toString() {
        return "Entrance{" +
                "name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
    public double toRadiansLat(double latitude){
        return Math.toRadians(latitude);
    }
    public double toRadiansLon(double longitude){
        return Math.toRadians(longitude);
    }


}
