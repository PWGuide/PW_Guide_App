package com.example.pwguide.timetable_download;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TimetableISOD {
    List<Subject> planItems;

    public TimetableISOD() {
    }

    public TimetableISOD(List<Subject> planItems) {
        this.planItems = planItems;
    }

    public List<Subject> getPlanItems() {
        return planItems;
    }

    public void setPlanItems(List<Subject> planItems) {
        this.planItems = planItems;
    }

    @Override
    public String toString() {
        return "TimeTable{" +
                "planItems=" + planItems +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Subject {
        String courseName;
        String startTime;
        String endTime;
        int dayOfWeek;
        String building;
        String room;
        String buildingShort;
        String typeOfClasses;

        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getDayOfWeek() {
            return dayOfWeek;
        }

        public void setDayOfWeek(int dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public String getBuilding() {
            return building;
        }

        public void setBuilding(String building) {
            this.building = building;
        }

        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }

        public String getBuildingShort() {
            return buildingShort;
        }

        public void setBuildingShort(String buildingShort) {
            this.buildingShort = buildingShort;
        }

        public String getTypeOfClasses() {
            return typeOfClasses;
        }

        public void setTypeOfClasses(String typeOfClasses) {
            this.typeOfClasses = typeOfClasses;
        }

        @Override
        public String toString() {
            return "Subject{" +
                    "courseName='" + courseName + '\'' +
                    ", startTime='" + startTime + '\'' +
                    ", endTime='" + endTime + '\'' +
                    ", dayOfWeek='" + dayOfWeek + '\'' +
                    ", building='" + building + '\'' +
                    ", room='" + room + '\'' +
                    ", buildingShort='" + buildingShort + '\'' +
                    ", typeOfClasses='" + typeOfClasses + '\'' +
                    '}';
        }
    }
}

