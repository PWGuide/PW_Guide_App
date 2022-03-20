package com.example.pwguide.timetable_download;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TimetableUSOS {
    @JsonProperty("start_time")
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    Date startTime;

    @JsonProperty("end_time")
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    Date endTime;

    String courseName;
    String classtype;
    String building;

    @JsonProperty("building_id")
    String buildingId;

    @JsonProperty("room_number")
    String room;

    @JsonProperty("course_name")
    private void unpackNested(Map<String,String> course_name) {
        this.courseName = (String)course_name.get("pl");
    }

    @JsonProperty("classtype_name")
    private void unpackNested2(Map<String,String> classtype_name) {
        this.classtype = (String)classtype_name.get("pl");
    }

    @JsonProperty("building_name")
    private void unpackNested3(Map<String,String> building_name) {
        this.building = (String)building_name.get("pl");
    }

    public TimetableUSOS() {
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getClasstype() {
        return classtype;
    }

    public void setClasstype(String classtype) {
        this.classtype = classtype;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    @Override
    public String toString() {
        return "TimetableUSOS{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", courseName='" + courseName + '\'' +
                ", classtype='" + classtype + '\'' +
                ", building='" + building + '\'' +
                ", buildingId='" + buildingId + '\'' +
                ", room='" + room + '\'' +
                '}';
    }
}
