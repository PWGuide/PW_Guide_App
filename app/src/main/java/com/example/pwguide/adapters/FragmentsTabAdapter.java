package com.example.pwguide.adapters;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentsTabAdapter  extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private final Map<String, Fragment> activeDays = new HashMap<>();

    public FragmentsTabAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        activeDays.put(title, fragment);
        mFragmentTitleList.add(title);
        Collections.sort(mFragmentTitleList, new FragmentComparator());
        mFragmentList.add(mFragmentTitleList.indexOf(title), fragment );
    }

    public void removeFragment(String title) {
        mFragmentTitleList.remove(title);
        if(activeDays.get(title) != null) {
            mFragmentList.remove(activeDays.get(title));
        }
        activeDays.remove(title);
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    public int getPosition(int day) {
        if(day == 2) {
            return mFragmentTitleList.indexOf("Poniedziałek");
        } else if(day == 3) {
            return mFragmentTitleList.indexOf("Wtorek");
        } else if(day == 4) {
            return mFragmentTitleList.indexOf("Środa");
        } else if(day == 5) {
            return mFragmentTitleList.indexOf("Czwartek");
        } else if(day == 6) {
            return mFragmentTitleList.indexOf("Piątek");
        } else if(day == 7) {
            return mFragmentTitleList.indexOf("Sobota");
        } else if(day == 1) {
            return mFragmentTitleList.indexOf("Niedziela");
        }
        return -1;
    }

}