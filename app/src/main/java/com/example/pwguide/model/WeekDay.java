package com.example.pwguide.model;

import java.util.HashMap;
import java.util.Map;

public enum WeekDay {
    MONDAY("Monday", 1),
    TUESDAY("Tuesday", 2),
    WEDNESDAY("Wednesday", 3),
    THURSDAY("Thursday", 4),
    FRIDAY("Friday", 5),
    SATURDAY("Saturday", 6),
    SUNDAY("Sunday", 7);

    private String dayName;
    private int dayNumber;
    private static Map<Integer, WeekDay> map = new HashMap<>();

    WeekDay(String dayName, int dayNumber) {
        this.dayName = dayName;
        this.dayNumber = dayNumber;
    }

    public String getDayName() {
        return dayName;
    }

    static {
        for (WeekDay weekDay : WeekDay.values()) {
            map.put(weekDay.dayNumber, weekDay);
        }
    }

    public static WeekDay valueOf(int dayNumber) {
        System.out.println(dayNumber);
        return (WeekDay) map.get(dayNumber);
    }
}
