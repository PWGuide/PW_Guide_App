package com.example.pwguide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.viewpager.widget.ViewPager;


import android.os.Bundle;
import android.view.View;

import com.example.pwguide.adapters.FragmentsTabAdapter;
import com.example.pwguide.fragments.FridayFragment;
import com.example.pwguide.fragments.MondayFragment;
import com.example.pwguide.fragments.SaturdayFragment;
import com.example.pwguide.fragments.SundayFragment;
import com.example.pwguide.fragments.ThursdayFragment;
import com.example.pwguide.fragments.TuesdayFragment;
import com.example.pwguide.fragments.WednesdayFragment;
import com.example.pwguide.utils.AlertDialogsHelper;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

public class TimetableActivity extends AppCompatActivity {


    private FragmentsTabAdapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        getSupportActionBar().setTitle("Plan zajęć");
        //getSupportActionBar().hide();
        initAll();
    }

    private void initAll() {
        setupFragments();
        setupCustomDialog();

    }

    private void setupFragments() {
        adapter = new FragmentsTabAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        adapter.addFragment(new MondayFragment(), getResources().getString(R.string.monday));
        adapter.addFragment(new TuesdayFragment(), getResources().getString(R.string.tuesday));
        adapter.addFragment(new WednesdayFragment(), getResources().getString(R.string.wednesday));
        adapter.addFragment(new ThursdayFragment(), getResources().getString(R.string.thursday));
        adapter.addFragment(new FridayFragment(), getResources().getString(R.string.friday));
        adapter.addFragment(new SaturdayFragment(), getResources().getString(R.string.saturday));
        adapter.addFragment(new SundayFragment(), getResources().getString(R.string.sunday));
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(day == 1 ? 6 : day - 2, true);
        tabLayout.setupWithViewPager(viewPager);
    }


    private void setupCustomDialog() {
        final View alertLayout = getLayoutInflater().inflate(R.layout.dialog_add_subject, null);
        AlertDialogsHelper.getAddSubjectDialog(TimetableActivity.this, alertLayout, adapter, viewPager);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}