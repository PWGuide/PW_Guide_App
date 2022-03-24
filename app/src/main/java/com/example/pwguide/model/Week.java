package com.example.pwguide.model;

public class Week {

    private String subject, fragment, building, room, fromtime, totime, type;
    private int id, color;

    public Week() {}

    public Week(String subject, String type, String building, String room, String fromtime, String totime, int color) {
        this.subject = subject;
        this.type = type;
        this.building = building;
        this.room = room;
        this.fromtime = fromtime;
        this.totime = totime;
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        String t = type.length() > 2 ? type.substring(0,3) : type;
        switch (t) {
            case "Wyk":
                this.type = "W";
                break;
            case "Ćwi":
                this.type = "C";
                break;
            case "Lab":
                this.type = "L";
                break;
            case "Wyc":
                this.type = "WF";
                break;
            case "Pro":
                this.type = "P";
                break;
            case "Jęz":
                this.type = "JO";
                break;
            default:
                this.type = type;
        }
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFragment() {
        return fragment;
    }

    public void setFragment(String fragment) {
        this.fragment = fragment;
    }

    public String getFromTime() {
        return fromtime;
    }

    public void setFromTime(String fromtime) {
        this.fromtime = fromtime;
    }

    public String getToTime() {
        return totime;
    }

    public void setToTime(String totime) {
        this.totime = totime;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String toString() {
        return subject;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
