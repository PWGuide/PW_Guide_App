package com.example.pwguide.adapters;

import com.example.pwguide.model.WeekDay;

import java.util.Comparator;

public class FragmentComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        if (o1 == null || o2 == null) {
            throw new IllegalArgumentException("Fragment passed to the comparator should not be null");
        }

        int value1 = WeekDay.valueByPL(o1);
        int value2 = WeekDay.valueByPL(o2);

        if (value1 > value2) {
            return 1;
        } else if (value1 < value2) {
            return -1;
        } else
            return 0;
    }
}
