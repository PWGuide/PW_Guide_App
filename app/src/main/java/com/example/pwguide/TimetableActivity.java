package com.example.pwguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.viewpager.widget.ViewPager;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pwguide.adapters.FragmentsTabAdapter;
import com.example.pwguide.fragments.FridayFragment;
import com.example.pwguide.fragments.MondayFragment;
import com.example.pwguide.fragments.SaturdayFragment;
import com.example.pwguide.fragments.SundayFragment;
import com.example.pwguide.fragments.ThursdayFragment;
import com.example.pwguide.fragments.TuesdayFragment;
import com.example.pwguide.fragments.WednesdayFragment;
import com.example.pwguide.model.Week;
import com.example.pwguide.model.WeekDay;
import com.example.pwguide.timetable_download.TimetableDownload;
import com.example.pwguide.timetable_download.TimetableISOD;
import com.example.pwguide.utils.AlertDialogsHelper;
import com.example.pwguide.utils.DbHelper;
import com.google.android.material.tabs.TabLayout;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimetableActivity extends AppCompatActivity {


    private FragmentsTabAdapter adapter;
    private ViewPager viewPager;
    private AlertDialog isodAlert;
    private AlertDialog clearTimetableAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_timetable);
        getSupportActionBar().setTitle("Plan zajęć");
        //getSupportActionBar().hide();
        initAll();
    }

    private void initAll() {
        setupFragments();
        setupCustomDialog();

        final View addFromISODAlertLayout = getLayoutInflater().inflate(R.layout.download_from_isod_dialog, null);
        isodAlert = AlertDialogsHelper.createAddFromISODDialog(this, addFromISODAlertLayout, adapter);

        final View clearTimetableAlertLayout = getLayoutInflater().inflate(R.layout.dialog_clear_timetable, null);
        clearTimetableAlert = AlertDialogsHelper.createClearTimetableDialog(this, clearTimetableAlertLayout, adapter);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timetable_menu, menu);
        MenuItem item = menu.getItem(3);
        SpannableString spannableString = new SpannableString(item.getTitle().toString());
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#CE0000")), 0, spannableString.length(), 0);
        item.setTitle(spannableString);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_from_isod:
                isodAlert.show();
                return true;
            case R.id.add_from_usos:
                //pobieranie z USOSa
                return true;
            case R.id.edit_days:
                //przejść do ustawień jakie dni są widoczne
                return true;
            case R.id.clear_timetable:
                clearTimetableAlert.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}