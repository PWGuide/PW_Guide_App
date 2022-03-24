package com.example.pwguide.model;

import java.util.HashMap;
import java.util.Map;

public enum WeekDay {
    MONDAY("Poniedziałek","Monday", 1),
    TUESDAY("Wtorek", "Tuesday", 2),
    WEDNESDAY("Środa", "Wednesday", 3),
    THURSDAY("Czwartek","Thursday", 4),
    FRIDAY("Piątek","Friday", 5),
    SATURDAY("Sobota", "Saturday", 6),
    SUNDAY("Niedziela", "Sunday", 7);

    private final String dayNameEN;
    private final String dayNamePL;
    private final int dayNumber;
    private final static Map<Integer, WeekDay> map = new HashMap<>();
    private final static Map<String, Integer> mapByNum = new HashMap<>();

    WeekDay(String dayNamePL, String dayNameEN, int dayNumber) {
        this.dayNamePL = dayNamePL;
        this.dayNameEN = dayNameEN;
        this.dayNumber = dayNumber;
    }

    public String getDayNameEN() {
        return dayNameEN;
    }

    public String getDayNamePL() {
        return dayNamePL;
    }

    static {
        for (WeekDay weekDay : WeekDay.values()) {
            map.put(weekDay.dayNumber, weekDay);
            mapByNum.put(weekDay.dayNamePL, weekDay.dayNumber);
        }
    }

    public static WeekDay valueOf(int dayNumber) {
        return map.get(dayNumber);
    }

    public static int valueByPL(String dayNamePolish) {
        return mapByNum.get(dayNamePolish);
    }
}
